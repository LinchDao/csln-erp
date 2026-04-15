package com.lin.csln.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.csln.dto.sys.customer.CustomerPageRespDTO;
import com.lin.csln.dto.sys.customer.CustomerQueryParamDTO;
import com.lin.csln.entity.CustomerDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 客户表 Mapper接口
 * @author 系统生成器
 */
@Mapper
public interface CustomerMapper extends BaseMapper<CustomerDO> {

    IPage<CustomerPageRespDTO> pageCustomer(IPage<CustomerPageRespDTO> page,
                                            @Param("params") CustomerQueryParamDTO queryDTO);
}
