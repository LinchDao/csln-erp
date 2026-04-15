package com.lin.csln.service;

import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.dto.order.CustomerDTO;
import com.lin.csln.dto.sys.customer.CustomerDetailRespDTO;
import com.lin.csln.dto.sys.customer.CustomerPageRespDTO;
import com.lin.csln.dto.sys.customer.CustomerQueryParamDTO;
import com.lin.csln.dto.sys.customer.CustomerSaveReqDTO;
import com.lin.csln.dto.sys.customer.CustomerStatusUpdateDTO;
import com.lin.csln.entity.CustomerDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 客户表 服务接口
 * @author 系统生成器
 */
public interface CustomerService extends IService<CustomerDO> {

    PageRespDTO<CustomerPageRespDTO> pageCustomer(CustomerQueryParamDTO queryDTO);

    CustomerDetailRespDTO getCustomerDetail(String customerId);

    String addCustomer(CustomerSaveReqDTO dto);

    void updateCustomer(CustomerSaveReqDTO dto);

    void updateCustomerStatus(CustomerStatusUpdateDTO dto);

    List<CustomerDTO> listCustomerForSelect();
}
