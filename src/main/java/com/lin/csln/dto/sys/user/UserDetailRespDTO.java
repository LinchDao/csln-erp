package com.lin.csln.dto.sys.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "用户详情响应对象")
public class UserDetailRespDTO {

    @Schema(description = "用户ID")
    private String id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "门店ID")
    private String shopId;

    @Schema(description = "门店名称")
    private String shopName;

    @Schema(description = "仓库ID")
    private String warehouseId;

    @Schema(description = "仓库名称")
    private String warehouseName;

    @Schema(description = "角色ID列表")
    private List<String> roleIds;

    @Schema(description = "角色名称列表")
    private List<String> roleNames;

    @Schema(description = "状态：1-启用，0-禁用")
    private Integer status;

    @Schema(description = "创建时间，yyyy-MM-dd HH:mm:ss")
    private String createTime;
}
