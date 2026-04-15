package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.order.CustomerDTO;
import com.lin.csln.dto.sys.customer.CustomerDetailRespDTO;
import com.lin.csln.dto.sys.customer.CustomerPageRespDTO;
import com.lin.csln.dto.sys.customer.CustomerQueryParamDTO;
import com.lin.csln.dto.sys.customer.CustomerSaveReqDTO;
import com.lin.csln.dto.sys.customer.CustomerStatusUpdateDTO;
import com.lin.csln.entity.CustomerDO;
import com.lin.csln.entity.CustomerLevelDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.mapper.CustomerMapper;
import com.lin.csln.service.CustomerService;
import com.lin.csln.service.CustomerLevelService;
import com.lin.csln.service.impl.BaseReadonlyServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 客户表 服务实现类
 * @author 系统生成器
 */
@Service
public class CustomerServiceImpl extends BaseReadonlyServiceImpl<CustomerMapper, CustomerDO> implements CustomerService {

    @Resource
    private CustomerLevelService customerLevelService;

    @Override
    public PageRespDTO<CustomerPageRespDTO> pageCustomer(CustomerQueryParamDTO queryDTO) {
        IPage<CustomerPageRespDTO> page = new Page<>(queryDTO.getPage(), queryDTO.getLimit());
        IPage<CustomerPageRespDTO> result = baseMapper.pageCustomer(page, queryDTO);
        return PageRespDTO.build(result, queryDTO);
    }

    @Override
    public CustomerDetailRespDTO getCustomerDetail(String customerId) {
        if (!StringUtils.hasText(customerId)) {
            throw new BusinessException("客户ID不能为空");
        }

        CustomerDO customer = this.getById(customerId);
        if (customer == null) {
            throw new BusinessException("客户不存在");
        }

        CustomerDetailRespDTO dto = new CustomerDetailRespDTO();
        BeanUtils.copyProperties(customer, dto);
        dto.setCreateTime(formatDateTime(customer.getCreateTime()));
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addCustomer(CustomerSaveReqDTO dto) {
        validateName(dto.getName());
        validateStatus(dto.getStatus());
        validateLevelId(dto.getLevelId());
        checkNameUnique(dto.getName(), null);

        CustomerDO customer = new CustomerDO();
        customer.setName(dto.getName().trim());
        customer.setPhone(trimToNull(dto.getPhone()));
        customer.setLevelId(trimToNull(dto.getLevelId()));
        customer.setAddress(trimToNull(dto.getAddress()));
        customer.setRemark(trimToNull(dto.getRemark()));
        customer.setStatus(dto.getStatus());

        this.save(customer);
        return customer.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCustomer(CustomerSaveReqDTO dto) {
        if (!StringUtils.hasText(dto.getId())) {
            throw new BusinessException("客户ID不能为空");
        }
        validateName(dto.getName());
        validateStatus(dto.getStatus());
        validateLevelId(dto.getLevelId());

        CustomerDO dbCustomer = this.getById(dto.getId());
        if (dbCustomer == null) {
            throw new BusinessException("客户不存在");
        }

        checkNameUnique(dto.getName(), dto.getId());

        CustomerDO updateCustomer = new CustomerDO();
        updateCustomer.setId(dto.getId());
        updateCustomer.setName(dto.getName().trim());
        updateCustomer.setPhone(trimToNull(dto.getPhone()));
        updateCustomer.setLevelId(trimToNull(dto.getLevelId()));
        updateCustomer.setAddress(trimToNull(dto.getAddress()));
        updateCustomer.setRemark(trimToNull(dto.getRemark()));
        updateCustomer.setStatus(dto.getStatus());
        this.updateById(updateCustomer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCustomerStatus(CustomerStatusUpdateDTO dto) {
        if (dto == null || !StringUtils.hasText(dto.getCustomerId())) {
            throw new BusinessException("客户ID不能为空");
        }
        validateStatus(dto.getStatus());

        CustomerDO dbCustomer = this.getById(dto.getCustomerId());
        if (dbCustomer == null) {
            throw new BusinessException("客户不存在");
        }
        if (Objects.equals(dbCustomer.getStatus(), dto.getStatus())) {
            return;
        }

        CustomerDO updateCustomer = new CustomerDO();
        updateCustomer.setId(dto.getCustomerId());
        updateCustomer.setStatus(dto.getStatus());
        this.updateById(updateCustomer);
    }

    @Override
    public List<CustomerDTO> listCustomerForSelect() {
        LambdaQueryWrapper<CustomerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerDO::getStatus, GlobalEnums.YES.getCode());
        wrapper.orderByAsc(CustomerDO::getName);

        List<CustomerDO> list = baseMapper.selectList(wrapper);
        return list.stream().map(item -> {
            CustomerDTO dto = new CustomerDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new BusinessException("客户名称不能为空");
        }
    }

    private void validateStatus(Integer status) {
        if (!Objects.equals(status, GlobalEnums.YES.getCode()) && !Objects.equals(status, GlobalEnums.NO.getCode())) {
            throw new BusinessException("状态应为0或者1");
        }
    }

    private void validateLevelId(String levelId) {
        if (!StringUtils.hasText(levelId)) {
            return;
        }
        CustomerLevelDO level = customerLevelService.getById(levelId.trim());
        if (level == null) {
            throw new BusinessException("客户等级不存在");
        }
    }

    private void checkNameUnique(String name, String excludeId) {
        LambdaQueryWrapper<CustomerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CustomerDO::getName, name.trim());
        if (StringUtils.hasText(excludeId)) {
            wrapper.ne(CustomerDO::getId, excludeId);
        }
        if (baseMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("客户名称已存在");
        }
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
}
