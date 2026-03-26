package com.lin.csln.dto.sys.menu;

import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: linch
 */


@Data
public class MenuDTO {
    private String id;
    private String parentId;
    private String path;
    private String component;
    private String redirect;
    private String name;
    private String title;
    private String icon;
    private Integer sort;
    private Integer hidden;
    private List<MenuDTO> children;
}
