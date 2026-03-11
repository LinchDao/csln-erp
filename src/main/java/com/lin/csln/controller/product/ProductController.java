package com.lin.csln.controller.product;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lin.csln.common.dto.Result;
import com.lin.csln.dto.product.ProductDTO;
import com.lin.csln.dto.product.ProductQueryDTO;
import com.lin.csln.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 商品管理控制器
 * @Description:
 * @Author: linch
 */


@RestController
@RequestMapping("/product")
@Tag(name = "商品管理接口", description = "商品的增删改查及分页查询接口")
public class ProductController {

    @Resource
    private ProductService productService;

    @PostMapping
    @Operation(summary = "新增商品", description = "新增商品基础信息及关联的颜色图片、SKU信息")
    public Result<Long> addProduct(@Valid @RequestBody ProductDTO productDTO) {
        try {
            Long productId = productService.saveProduct(productDTO);
            return Result.success(productId);
        } catch (Exception e) {
            return Result.fail("新增商品失败：" + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改商品", description = "根据ID修改商品信息及关联的颜色图片、SKU信息")
    public Result<Boolean> updateProduct(
            @Parameter(description = "商品ID", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody ProductDTO productDTO) {
        try {
            productDTO.setId(id);
            boolean success = productService.updateProduct(productDTO);
            return Result.success(success);
        } catch (Exception e) {
            return Result.fail("修改商品失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品", description = "根据ID删除商品（级联删除关联的颜色图片、SKU）")
    public Result<Boolean> deleteProduct(@Parameter(description = "商品ID", required = true, example = "1") @PathVariable Long id) {
        try {
            boolean success = productService.deleteProduct(id);
            return Result.success(success);
        } catch (Exception e) {
            return Result.fail("删除商品失败：" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询商品详情", description = "根据ID查询商品基础信息及关联的颜色图片、SKU信息")
    public Result<ProductDTO> getProductById(@Parameter(description = "商品ID", required = true, example = "1") @PathVariable Long id) {
        try {
            ProductDTO productDTO = productService.getProductById(id);
            return Result.success(productDTO);
        } catch (Exception e) {
            return Result.fail("查询商品失败：" + e.getMessage());
        }
    }

    @PostMapping("/page")
    @Operation(summary = "分页查询商品", description = "根据条件分页查询商品列表（不含关联详情）")
    public Result<IPage<ProductDTO>> pageProduct(@RequestBody ProductQueryDTO queryDTO) {
        try {
            Page<ProductDTO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
            IPage<ProductDTO> productPage = productService.pageProduct(page, queryDTO);
            return Result.success(productPage);
        } catch (Exception e) {
            return Result.fail("分页查询商品失败：" + e.getMessage());
        }
    }
}
