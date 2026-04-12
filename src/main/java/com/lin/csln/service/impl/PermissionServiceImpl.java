package com.lin.csln.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lin.csln.entity.PermissionDO;
import com.lin.csln.mapper.PermissionMapper;
import com.lin.csln.service.PermissionService;
import com.lin.csln.service.impl.BaseReadonlyServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限表 服务实现类
 * @author 系统生成器
 */
@Service
public class PermissionServiceImpl extends BaseReadonlyServiceImpl<PermissionMapper, PermissionDO> implements PermissionService {

    @Override
    public List<String> listPermCodesByPermIds(List<String> permIds) {
        if (permIds == null || permIds.isEmpty()) {
            return new ArrayList<>();
        }

        LambdaQueryWrapper<PermissionDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(PermissionDO::getId, permIds);
        wrapper.select(PermissionDO::getPermCode);
        List<PermissionDO> permissionList = this.list(wrapper);
        if (permissionList == null || permissionList.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> permCodeSet = permissionList.stream()
                .map(PermissionDO::getPermCode)
                .filter(StringUtils::hasText)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return new ArrayList<>(permCodeSet);
    }
}
