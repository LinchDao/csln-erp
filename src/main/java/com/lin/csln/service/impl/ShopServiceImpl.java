package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.csln.dto.sys.shop.ShopListDTO;
import com.lin.csln.entity.ShopDO;
import com.lin.csln.enums.GlobalEnums;
import com.lin.csln.mapper.ShopMapper;
import com.lin.csln.service.ShopService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 门店表服务实现类
 */
@Service
public class ShopServiceImpl extends BaseReadonlyServiceImpl<ShopMapper, ShopDO> implements ShopService {

    @Override
    public List<ShopListDTO> listShopForSelect() {
        LambdaQueryWrapper<ShopDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShopDO::getStatus, GlobalEnums.YES.getCode());
        wrapper.orderByAsc(ShopDO::getShopName);

        List<ShopDO> list = baseMapper.selectList(wrapper);
        return list.stream().map(shop -> {
            ShopListDTO dto = new ShopListDTO();
            dto.setId(shop.getId());
            dto.setShopName(shop.getShopName());
            return dto;
        }).collect(Collectors.toList());
    }
}