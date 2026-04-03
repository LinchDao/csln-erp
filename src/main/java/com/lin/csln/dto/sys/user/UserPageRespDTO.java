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
}
