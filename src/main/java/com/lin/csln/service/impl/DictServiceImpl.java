package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.csln.service.impl.BaseReadonlyServiceImpl;
import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.dto.sys.dict.DictDTO;
import com.lin.csln.dto.sys.dict.DictPageRespDTO;
import com.lin.csln.dto.sys.dict.DictQueryParamDTO;
import com.lin.csln.entity.DictDO;
import com.lin.csln.mapper.DictMapper;
import com.lin.csln.service.DictService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统数据字典表 服务实现类
 *
 * @author 系统生成器
 */
@Service
public class DictServiceImpl extends BaseReadonlyServiceImpl<DictMapper, DictDO> implements DictService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDict(DictDTO dict) {
        if (dict.getId() == null || dict.getId().trim().isEmpty()) {
            throw new BusinessException("字典ID不能为空");
        }
        if (CollectionUtils.isEmpty(dict.getDictList())) {
            throw new BusinessException("字典列表不能为空");
        }
        //更新主字典
        DictDO mainDict = new DictDO();
        BeanUtils.copyProperties(dict, mainDict);
        mainDict.setParentId("0");
        baseMapper.updateById(mainDict);

        String mainDictId = dict.getId();
        //查找旧的子字典，和前端传的子字典ID进行对比，删除不再需要的子字典
        LambdaQueryWrapper<DictDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DictDO::getParentId, mainDictId);
        wrapper.eq(DictDO::getIsDelete, 0);
        List<DictDO> existChildList = baseMapper.selectList(wrapper);

        Set<String> frontIds = dict.getDictList().stream()
                .map(DictDTO::getId)
                .filter(id -> StringUtils.hasLength(id))
                .collect(Collectors.toSet());

        List<DictDO> needDeleteList = existChildList.stream()
                .filter(child -> !frontIds.contains(child.getId()))
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(needDeleteList)) {
            needDeleteList.forEach(item -> {
                item.setIsDelete(1);
                baseMapper.updateById(item);
            });
        }
        //更新子字典
        List<DictDTO> dictList = dict.getDictList();
        for (DictDTO item : dictList) {
            DictDO child = new DictDO();
            BeanUtils.copyProperties(item, child);
            child.setParentId(mainDictId);
            child.setIsDelete(0);
            if (child.getId() == null || child.getId().trim().isEmpty()) {
                baseMapper.insert(child);
            } else {
                // 更新
                baseMapper.updateById(child);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addDict(DictDTO dict) {
        if (CollectionUtils.isEmpty(dict.getDictList())) {
            throw new BusinessException("字典列表不能为空");
        }

        DictDO mainDict = new DictDO();
        BeanUtils.copyProperties(dict, mainDict);
        baseMapper.insert(mainDict);

        List<DictDTO> dictList = dict.getDictList();
        String mainDictId = mainDict.getId();
        for (DictDTO item : dictList) {
            DictDO child = new DictDO();
            BeanUtils.copyProperties(item, child);
            child.setParentId(mainDictId);
            baseMapper.insert(child);
        }

        return mainDictId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDict(String id) {
        if (!StringUtils.hasLength(id)) {
            throw new BusinessException("字典ID不能为空");
        }

        LambdaUpdateWrapper<DictDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(DictDO::getId, id);
        updateWrapper.set(DictDO::getIsDelete, 1);
        baseMapper.update(null, updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteDict(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException("批量删除：字典ID集合不能为空");
        }

        LambdaUpdateWrapper<DictDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(DictDO::getId, ids);
        wrapper.set(DictDO::getIsDelete, 1);
        baseMapper.update(null, wrapper);
    }

    @Override
    public PageRespDTO<DictPageRespDTO> pageDict(DictQueryParamDTO param) {
        Page<DictDO> page = new Page<>(param.getPage(), param.getLimit());

        LambdaQueryWrapper<DictDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DictDO::getIsDelete, 0);
        wrapper.eq(DictDO::getParentId, '0');


        if (StringUtils.hasText(param.getDictName())) {
            wrapper.like(DictDO::getDictName, param.getDictName());
        }

        if (StringUtils.hasText(param.getDictValue())) {
            wrapper.eq(DictDO::getDictValue, param.getDictValue());
        }

        if (param.getStatus() != null) {
            wrapper.eq(DictDO::getStatus, param.getStatus());
        }

        wrapper.orderByAsc(DictDO::getSort);

        IPage<DictDO> doPage = baseMapper.selectPage(page, wrapper);

        List<DictPageRespDTO> voList = doPage.getRecords().stream().map(item -> {
            DictPageRespDTO vo = new DictPageRespDTO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());

        return PageRespDTO.of(doPage.getTotal(), voList, param.getPage(), param.getLimit());
    }

    @Override
    public DictDTO getDictById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new BusinessException("字典ID不能为空");
        }

        DictDO mainDict = baseMapper.selectById(id);
        if (mainDict == null || mainDict.getIsDelete() == 1) {
            return null;
        }

        DictDTO dictDTO = new DictDTO();
        BeanUtils.copyProperties(mainDict, dictDTO);

        LambdaQueryWrapper<DictDO> childWrapper = new LambdaQueryWrapper<>();
        childWrapper.eq(DictDO::getParentId, id);
        childWrapper.eq(DictDO::getIsDelete, 0);
        childWrapper.orderByAsc(DictDO::getSort);

        List<DictDO> childList = baseMapper.selectList(childWrapper);
        if (!CollectionUtils.isEmpty(childList)) {
            List<DictDTO> childDTOList = childList.stream().map(child -> {
                DictDTO childDTO = new DictDTO();
                BeanUtils.copyProperties(child, childDTO);
                return childDTO;
            }).collect(Collectors.toList());

            dictDTO.setDictList(childDTOList);
        } else {
            dictDTO.setDictList(Collections.emptyList());
        }

        return dictDTO;
    }

}
