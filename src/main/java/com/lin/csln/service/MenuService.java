package com.lin.csln.service;

import com.lin.csln.dto.sys.menu.MenuDTO;
import com.lin.csln.entity.MenuDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 菜单表 服务接口
 * @author 系统生成器
 */
public interface MenuService extends IService<MenuDO> {

    List<MenuDTO> treeMenuByRoleCode(List<String> roles);
}
