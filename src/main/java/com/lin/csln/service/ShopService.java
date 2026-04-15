package com.lin.csln.service;

import com.lin.csln.entity.ShopDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.dto.sys.shop.ShopListDTO;

import java.util.List;

/**
 * 门店表 服务接口
 * @author 系统生成器
 */
public interface ShopService extends IService<ShopDO> {

    List<ShopListDTO> listShopForSelect();
}
