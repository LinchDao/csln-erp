package com.lin.csln.dto.sys.user;


import com.lin.csln.common.dto.PageQueryParamDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description:
 * @Author: linch
 */

@Data
@Schema(name = "UserQueryParamDTO", description = "用户分页查询参数")
public class UserQueryParamDTO extends PageQueryParamDTO {
    @Schema(description = "姓名（模糊查询）")
    private String realName;

    @Schema(description = "用户账号/姓名/手机号")
    private String userKeyWord;

    @Schema(description = "商店/仓库")
    private String storeWarehouseKeyword;

    @Schema(description = "用户状态：1-启用，0-禁用")
    private Integer status;
}
