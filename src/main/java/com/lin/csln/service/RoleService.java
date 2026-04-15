package com.lin.csln.service;

import com.lin.csln.common.dto.PageRespDTO;
import com.lin.csln.dto.sys.role.RoleDetailRespDTO;
import com.lin.csln.dto.sys.role.RoleListDTO;
import com.lin.csln.dto.sys.role.RolePageRespDTO;
import com.lin.csln.dto.sys.role.RoleQueryParamDTO;
import com.lin.csln.dto.sys.role.RoleSaveReqDTO;
import com.lin.csln.entity.RoleDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 角色表 服务接口
 * @author 系统生成器
 */
public interface RoleService extends IService<RoleDO> {

    List<RoleDO> listRolesByUserId(String id);

    List<RoleListDTO> listRoleForSelect();

    PageRespDTO<RolePageRespDTO> pageRole(RoleQueryParamDTO queryDTO);

    String addRole(RoleSaveReqDTO dto);

    void updateRole(RoleSaveReqDTO dto);

    RoleDetailRespDTO getRoleDetail(String id);

    void deleteRole(String id);
}
