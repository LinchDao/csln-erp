package com.lin.csln.common.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description: 用户信息DTO（数据传输对象）
 * @Author: linch
 */

@Data
@Schema(description = "用户信息传输对象")
public class UserInfoDTO {

    @Schema(description = "用户ID")
    private String id;

    @Schema(description = "登录账号")
    private String username;

    @Schema(description = "姓名")
    private String realName;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "所属门店ID")
    private String shopId;

    @Schema(description = "所属仓库ID")
    private String warehouseId;

    @Schema(description = "状态 1正常 0禁用")
    private Integer status;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "角色编码列表")
    private List<String> roles;

    @Schema(description = "是否管理员")
    private Boolean isAdmin;

    @Schema(description = "权限编码列表")
    private List<String> permissionList;
}
