package com.lin.csln.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lin.csln.dto.sys.menu.MenuDTO;
import com.lin.csln.entity.MenuDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单表 Mapper接口
 *
 * @author 系统生成器
 */
@Mapper
public interface MenuMapper extends BaseMapper<MenuDO> {

    List<MenuDTO> selectMenusByRoleCodes(@Param("roles") List<String> roles);
}
