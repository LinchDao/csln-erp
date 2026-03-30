package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.csln.dto.purchase.SupplierDTO;
import com.lin.csln.dto.purchase.SupplierQueryDTO;
import com.lin.csln.entity.SupplierDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.mapper.SupplierMapper;
import com.lin.csln.service.SupplierService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 供应商表 服务实现类
 *
 * @author 系统生成器
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, SupplierDO> implements SupplierService {

    @Override
    public List<SupplierDTO> listSupplier(SupplierQueryDTO queryDTO) {
        LambdaQueryWrapper<SupplierDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(queryDTO.getName() != null, SupplierDO::getName, queryDTO.getName());
        wrapper.eq(SupplierDO::getStatus, GlobalEnums.YES.getCode());
        wrapper.orderByDesc(SupplierDO::getCreateTime);

        List<SupplierDO> list = baseMapper.selectList(wrapper);

        return list.stream().map(item -> {
            SupplierDTO dto = new SupplierDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());
    }
}
