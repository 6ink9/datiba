package com.datiba.controller;

import com.datiba.domain.req.EditInfoReq;
import com.datiba.domain.res.InfoRes;
import com.datiba.service.UserService;
import com.datiba.util.AuthUtil;
import com.datiba.config.annotation.IsPhoneNumber;
import com.datiba.util.Result;
import com.datiba.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.util.UUID;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/13
 */
@RestController
@Slf4j
@RequestMapping("/user")
@Api(tags = "用户管理")
@Validated
public class UserController {

    /**
     * 图片保存目录（相对于应用部署目录）
     */
    @Value("${image.upload.dir}")
    private String uploadDir;
    /**
     * 文件访问URL前缀
     */
    @Value("${image.file.url.prefix}")
    private String fileUrlPrefix;
    @Autowired
    UserService userService;

    @PostMapping("login")
    @ApiOperation("登录")
    public Result login(@RequestParam("phone")String phone,
                        @RequestParam("password") String password) {

        InfoRes res = userService.login(phone, password);
        return ResultGenerator.genSuccessResult(res);
    }

    @PostMapping("sign")
    @ApiOperation("注册")
    public Result sign(@RequestParam("phone")@IsPhoneNumber String phone,
                       @RequestParam("password") String password,
                       @RequestParam("role") String role) {
        InfoRes res = userService.sign(phone, password, role);
        return ResultGenerator.genSuccessResult(res);
    }

    @PostMapping("edit")
    @ApiOperation("修改资料")
    public Result edit(@RequestBody @Validated EditInfoReq req,
                       HttpServletRequest request) {
        Integer userId = AuthUtil.getUserId(request);
        InfoRes res = userService.edit(userId, req.getNickName(), req.getAvatar(), req.getMotto());
        return ResultGenerator.genSuccessResult(res);
    }

    @PostMapping("upload")
    @ApiOperation("上传头像")
    public Result upload(@RequestParam("file") MultipartFile multipartFile) {
        try {
            // 生成唯一文件名，避免重名覆盖
            String originalFilename = multipartFile.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileName = UUID.randomUUID().toString() + "." + fileExtension;
            String filePath = uploadDir + File.separator + fileName;
            log.info("文件名为:{}", fileName);
            //创建空文件
            File file = new File(filePath);
            multipartFile.transferTo(file);
            String fileUrl = fileUrlPrefix + fileName;
            return ResultGenerator.genSuccessResult(null,fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResultGenerator.genFailResult("头像上传失败");

    }
}
