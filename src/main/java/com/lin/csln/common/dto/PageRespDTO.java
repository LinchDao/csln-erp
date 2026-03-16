package com.lin.csln.common.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @param <T>
 * @author linch
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PageRespDTO", description = "分页响应数据模型")
public class PageRespDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "总记录数", example = "100", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "总记录数不能为空")
    private long total;

    @Schema(description = "当前页数据列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "数据列表不能为空")
    private List<T> rows;

    @Schema(description = "每页条数", example = "10", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer limit;

    @Schema(description = "当前页码", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer page;

    @Schema(description = "总页数", example = "10", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long totalPages;

    public static <T> PageRespDTO<T> of(long total, List<T> rows) {
        PageRespDTO<T> pageResp = new PageRespDTO<>();
        pageResp.setTotal(total);
        pageResp.setRows(rows);
        return pageResp;
    }

    public static <T> PageRespDTO<T> of(long total, List<T> rows, int page, int limit) {
        PageRespDTO<T> pageResp = new PageRespDTO<>();
        pageResp.setTotal(total);
        pageResp.setRows(rows);
        pageResp.setLimit(limit);
        pageResp.setPage(page);
        pageResp.setTotalPages(total % limit == 0 ? total / limit : total / limit + 1);
        return pageResp;
    }

    public static <T> PageRespDTO<T> empty() {
        return of(0L, List.of());
    }
}
