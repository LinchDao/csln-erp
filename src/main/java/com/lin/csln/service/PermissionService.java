package com.lin.csln.service;

import com.lin.csln.entity.PermissionDO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lin.csln.dto.sys.permission.PermissionListDTO;

import java.util.List;

/**
 * 权限表 服务接口
 * @author 系统生成器
 */
public interface PermissionService extends IService<PermissionDO> {

    List<String> listPermCodesByPermIds(List<String> permIds);

    List<PermissionListDTO> listPermissionForSelect();
}
