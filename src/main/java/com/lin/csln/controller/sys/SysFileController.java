package com.lin.csln.controller.sys;

import com.lin.csln.common.constants.ResultCode;
import com.lin.csln.common.dto.Result;
import com.lin.csln.common.exception.BusinessException;
import com.lin.csln.config.FileUploadConfig;
import com.lin.csln.entity.FileDO;
import com.lin.csln.service.FileService;
import com.lin.csln.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;


/**
 * @Description:文件服务
 * @Author: linch
 */
@Slf4j
@RestController
@RequestMapping("/erp/file")
@RequiredArgsConstructor
@Tag(name = "通用文件管理接口", description = "仅包含文件上传、下载核心接口")
public class SysFileController {


    @Resource
    private FileService fileService;
    @Resource
    private FileUploadConfig fileUploadConfig;

    @PostMapping("/upload")
    @Operation(summary = "文件上传", description = "上传文件")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException(ResultCode.FILE_NOT_EXISTS);
        }

        try {
            String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path fullDirPath = Paths.get(fileUploadConfig.getBasePath(), dateDir);
            if (!Files.exists(fullDirPath)) {
                Files.createDirectories(fullDirPath);
            }

            String originalFilename = file.getOriginalFilename();
            String fileExt = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString().replace("-", "");
            Path filePath = Paths.get(fullDirPath.toString(), uniqueFileName);

            file.transferTo(filePath.toFile());

            String fileId = fileService.saveFile(originalFilename
                    , uniqueFileName
                    , fileExt
                    , dateDir
                    , filePath.toString()
                    , file.getSize()
                    , file.getContentType()
                    , JwtTokenUtil.getUserId());

            return Result.success(fileId);

        } catch (IOException e) {
            // 文件保存失败异常处理
            throw new BusinessException("文件上传失败：" + e.getMessage());
        }
    }

    @GetMapping("/download/{fileId}")
    @Operation(summary = "文件下载", description = "根据文件ID下载文件（附件形式）")
    public ResponseEntity<byte[]> download(
            @Parameter(description = "文件ID", required = true)
            @PathVariable String fileId) {

        FileDO fileDO = fileService.getById(fileId);
        if (fileDO == null) {
            throw new BusinessException(ResultCode.FILE_NOT_EXISTS);
        }
        if (fileDO.getIsDelete() == 1) {
            throw new BusinessException("文件已被删除，无法下载");
        }

        File file = new File(fileDO.getFullFilePath());
        if (!file.exists() || !file.isFile()) {
            throw new BusinessException("文件在服务器上不存在，无法下载");
        }

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());

            HttpHeaders headers = new HttpHeaders();
            String encodedFileName = URLEncoder.encode(fileDO.getFileName(), StandardCharsets.UTF_8.toString());
            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + encodedFileName + "\"");
            headers.setContentType(MediaType.parseMediaType(fileDO.getContentType()));
            headers.setContentLength(fileContent.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileContent);

        } catch (IOException e) {
            throw new BusinessException("文件下载失败：" + e.getMessage());
        }
    }


}