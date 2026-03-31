package com.lin.csln.service;

import com.lin.csln.dto.stock.warehouse.WarehouseListDTO;
import com.lin.csln.entity.WarehouseDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 仓库表 服务接口
 * @author 系统生成器
 */
public interface WarehouseService extends IService<WarehouseDO> {

    List<WarehouseListDTO> listWarehouseForSelect();
}
