package com.datiba.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datiba.common.constant.CommonConstant;
import com.datiba.domain.Paper;
import com.datiba.domain.dto.PaperDetailRes;
import com.datiba.domain.req.PaperListReq;
import com.datiba.domain.dto.PaperSetDto;
import com.datiba.exception.BusinessException;
import com.datiba.service.PaperService;
import com.datiba.service.UserPaperCollectionService;
import com.datiba.util.AuthUtil;
import com.datiba.util.Result;
import com.datiba.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Description:
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/8
 */
@RestController
@Slf4j
@RequestMapping("/paper")
@Api(tags = "试卷管理")
public class PaperController {
    @Autowired
    private PaperService paperService;

    @Autowired
    private UserPaperCollectionService paperCollectionService;

    @PostMapping("list")
    @ApiOperation("试卷列表")
    public Result list(@RequestBody PaperListReq req, HttpServletRequest request){
        int userId = AuthUtil.getUserId(request);
        Page<Paper> page = new Page<>(req.getPageNum(), CommonConstant.PAGE_SIZE);
        List<Paper> records = paperService.getBaseMapper().listPaper(req.getOrder(), req.getBankId(), req.getKeyword()!=null?req.getKeyword().trim():null, userId);
        page.setRecords(records);
        return ResultGenerator.genSuccessResult(page);
    }

    @PostMapping("upload")
    @ApiOperation("上传试卷")
    public Result upload(@RequestParam("excelFile")MultipartFile file,
                         @RequestParam("name") String name,
                         @RequestParam("description") String desc,
                         @RequestParam("bankId") int bankId,
                         @RequestParam("isPrivate") int isPrivate,
                         @RequestParam("eachScore") String eachScore,
                         HttpServletRequest request){
        int userId = AuthUtil.getUserId(request);

        try {
            paperService.upload(file, name, desc, bankId,isPrivate,eachScore,userId);
        }catch (BusinessException e){
            log.error(e.getMsg());
            return ResultGenerator.genFailResult(e.getMsg());
        }catch (Exception e){
            log.error(e.getMessage());
            return ResultGenerator.genFailResult("文件解析失败，请重试");
        }

        return  ResultGenerator.genSuccessResult();
    }

    @PostMapping("set")
    @ApiOperation("编题考试")
    public Result set(@RequestBody PaperSetDto paperSetDto,HttpServletRequest request){
        int userId = AuthUtil.getUserId(request);
        try {
            paperService.set(paperSetDto, userId);
        }catch (BusinessException e){
            return ResultGenerator.genFailResult(e.getMsg());
        }catch (Exception e){
            return ResultGenerator.genFailResult("编题考试失败");
        }

        return ResultGenerator.genSuccessResult();
    }



    @GetMapping("hot")
    @ApiOperation("热门试卷")
    public Result hot(){
        List<Paper> hotPaperList = paperService.getBaseMapper().listHotPaper(CommonConstant.HOT_PAPER_SIZE);
        return ResultGenerator.genSuccessResult(hotPaperList);
    }

    @GetMapping("recent")
    @ApiOperation("最近参加")
    public Result recent(HttpServletRequest request){
        Integer userId = AuthUtil.getUserId(request);
        List<Paper> recentPaper = paperService.getRecentPaper(userId);
        return ResultGenerator.genSuccessResult(recentPaper);
    }

    @PostMapping("detail")
    @ApiOperation("试卷详情")
    public Result detail(@RequestParam("paperId") Integer paperId, HttpServletRequest request){
        int userId = AuthUtil.getUserId(request);
        PaperDetailRes dto = paperService.getPaperDetail(paperId,userId);
        return ResultGenerator.genSuccessResult(dto);
    }

    @PostMapping("collect")
    @ApiOperation("收藏试卷")
    public Result collect(@RequestParam("paperId") Integer paperId,
                          @RequestParam("operation") Integer opt,
                          HttpServletRequest request){
        try{
            Integer userId = AuthUtil.getUserId(request);
            boolean res = paperCollectionService.collectPaper(paperId, userId, opt);
            return ResultGenerator.genSuccessResult(res);
        }catch (BusinessException e){
            return ResultGenerator.genFailResult("收藏试卷失败，请稍后重试");
        }
    }

    @GetMapping("collection")
    @ApiOperation("收藏列表")
    public Result listCollection(HttpServletRequest request){
        Integer userId = AuthUtil.getUserId(request);
        List<Paper> collection = paperService.getUserCollection(userId);
        return ResultGenerator.genSuccessResult(collection);
    }

    @GetMapping("created")
    @ApiOperation("创建列表")
    public Result listCreated(HttpServletRequest request){
        Integer userId = AuthUtil.getUserId(request);
        List<Paper> paperList = paperService.getBaseMapper().getUserCreated(userId);
        return ResultGenerator.genSuccessResult(paperList);
    }


}


