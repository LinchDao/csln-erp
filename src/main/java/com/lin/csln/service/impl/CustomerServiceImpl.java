package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.csln.dto.order.CustomerDTO;
import com.lin.csln.entity.CustomerDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.mapper.CustomerMapper;
import com.lin.csln.service.CustomerService;
import com.lin.csln.service.impl.BaseReadonlyServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 客户表 服务实现类
 * @author 系统生成器
 */
@Service
public class CustomerServiceImpl extends BaseReadonlyServiceImpl<CustomerMapper, CustomerDO> implements CustomerService {

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
}
