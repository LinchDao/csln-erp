package com.lin.csln.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.dto.sys.dict.DictDTO;
import com.lin.csln.dto.sys.dict.DictPageRespDTO;
import com.lin.csln.dto.sys.dict.DictQueryParamDTO;
import com.lin.csln.entity.DictDO;

import java.util.List;

/**
 * 系统数据字典表 服务接口
 *
 * @author 系统生成器
 */
public interface DictService extends IService<DictDO> {

    void updateDict(DictDTO dict);

    String addDict(DictDTO dict);

    void deleteDict(String id);

    void batchDeleteDict(List<String> ids);

    PageRespDTO<DictPageRespDTO> pageDict(DictQueryParamDTO queryDTO);

    DictDTO getDictById(String id);
}
