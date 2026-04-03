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
}
