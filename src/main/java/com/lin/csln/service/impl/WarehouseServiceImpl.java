package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.csln.service.impl.BaseReadonlyServiceImpl;
import com.lin.csln.dto.stock.warehouse.WarehouseListDTO;
import com.lin.csln.entity.WarehouseDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.mapper.WarehouseMapper;
import com.lin.csln.service.WarehouseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 仓库表 服务实现类
 *
 * @author 系统生成器
 */
@Service
public class WarehouseServiceImpl extends BaseReadonlyServiceImpl<WarehouseMapper, WarehouseDO> implements WarehouseService {

    @Override
    public List<WarehouseListDTO> listWarehouseForSelect() {
        LambdaQueryWrapper<WarehouseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WarehouseDO::getStatus, GlobalEnums.YES.getCode());
        wrapper.eq(WarehouseDO::getIsDelete, GlobalEnums.NO.getCode());
        wrapper.orderByAsc(WarehouseDO::getWarehouseName);

        List<WarehouseDO> list = baseMapper.selectList(wrapper);

        return list.stream().map(warehouse -> {
            WarehouseListDTO dto = new WarehouseListDTO();
            dto.setId(warehouse.getId());
            dto.setWarehouseName(warehouse.getWarehouseName());
            return dto;
        }).collect(Collectors.toList());
    }
}
