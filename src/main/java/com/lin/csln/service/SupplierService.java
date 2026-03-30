package com.lin.csln.service;

import com.lin.csln.dto.purchase.SupplierDTO;
import com.lin.csln.dto.purchase.SupplierQueryDTO;
import com.lin.csln.entity.SupplierDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 供应商表 服务接口
 * @author 系统生成器
 */
public interface SupplierService extends IService<SupplierDO> {

    List<SupplierDTO> listSupplier(SupplierQueryDTO queryDTO);
}
