package com.lin.csln.service;

import com.lin.csln.dto.order.CustomerDTO;
import com.lin.csln.entity.CustomerDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 客户表 服务接口
 * @author 系统生成器
 */
public interface CustomerService extends IService<CustomerDO> {

    List<CustomerDTO> listCustomerForSelect();
}
