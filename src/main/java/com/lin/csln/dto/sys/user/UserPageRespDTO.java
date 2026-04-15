package com.lin.csln.dto.sys.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description:
 * @Author: linch
 */
@Data
@Schema(name = "UserPageRespDTO", description = "用户分页响应数据")
public class UserPageRespDTO {

    @Schema(description = "用户ID")
    private String id;

    @Schema(description = "登录账号")
    private String username;

    @Schema(description = "姓名")
    private String realName;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "商店ID")
    private String shopId;

    @Schema(description = "商店名称")
    private String shopName;

    @Schema(description = "仓库ID")
    private String warehouseId;

    @Schema(description = "仓库名称")
    private String warehouseName;

    @Schema(description = "用户状态：1-启用，0-禁用")
    private Integer status;

    @Schema(description = "创建时间")
    private String createTime;
}
