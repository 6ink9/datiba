package com.datiba.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datiba.common.constant.CommonConstant;
import com.datiba.domain.Exam;
import com.datiba.domain.Paper;
import com.datiba.domain.Question;
import com.datiba.domain.answer.UserAnswer;
import com.datiba.domain.dto.*;
import com.datiba.domain.req.ExamSubmitReq;
import com.datiba.domain.res.ExamAnalysisRes;
import com.datiba.domain.res.ExamContinRes;
import com.datiba.domain.res.ExamStartRes;
import com.datiba.exception.BusinessException;
import com.datiba.service.ExamService;
import com.datiba.util.AuthUtil;
import com.datiba.util.Result;
import com.datiba.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/2/19
 */
@RestController
@Slf4j
@RequestMapping("/exam")
@Api(tags = "考试管理")
public class ExamController {
    @Autowired
    ExamService examService;

    /**
     * 开始考试
     * @param recordId
     * @return
     */
    @PostMapping("start")
    @ApiOperation("开始考试")
    public Result start(@RequestParam Integer recordId){
        try {
            ExamStartRes res = examService.start(recordId);
            return ResultGenerator.genSuccessResult(res);
        }catch (BusinessException e){
            return ResultGenerator.genFailResult(e.getMsg());
        }catch (Exception e){
            return ResultGenerator.genFailResult("操作失败，请重试！");
        }


    }

    /**
     * 考试详情
     * @param recordId
     * @return
     */
    @PostMapping("detail")
    @ApiOperation("考试详情")
    public Result detail(@RequestParam("recordId") Integer recordId){
        ExamRecordDetailDto dto = examService.getExamDetail(recordId);
        return ResultGenerator.genSuccessResult(dto);
    }

    /**
     * 创建考试
     * @param examSetDto
     * @return
     */
    @PostMapping("create")
    @ApiOperation("创建考试")
    public Result create(@RequestBody ExamSetDto examSetDto,HttpServletRequest request){
        try {
            Integer userId = AuthUtil.getUserId(request);
            int recordId = examService.create(examSetDto, userId);
            return ResultGenerator.genSuccessResult(recordId);

        }catch (BusinessException e){
            return ResultGenerator.genFailResult(e.getMsg());
        }catch (Exception e){
            return ResultGenerator.genFailResult("操作失败，请重试！");
        }

    }

    /**
     * 发布考试（班级列表页点击发布考试 进入试卷页面 选择试卷 弹窗 设置考试 发布考试 ）
     */
    @PostMapping("release")
    @ApiOperation("发布考试")
    public Result release(@RequestBody ExamReleaseDto dto, HttpServletRequest request){
        Integer userId = AuthUtil.getUserId(request);
        String code = examService.release(dto, userId);
        return ResultGenerator.genSuccessResult("发布成功",code);
    }

    /**
     * 获取老师发布过的考试试卷
     * @param request
     * @return
     */
    @GetMapping("releaseList")
    @ApiOperation("发布考试列表")
    public Result releaseList(HttpServletRequest request){
        Integer userId = AuthUtil.getUserId(request);
        List<Paper> examList = examService.releaseList(userId);
        return ResultGenerator.genSuccessResult(examList);
    }


    /**
     * 创建的考试
     * @param pageNum
     * @return
     */
    @GetMapping("listCreated/{pageNum}")
    @ApiOperation("我创建的考试")
    public Result listCreated(@PathVariable Integer pageNum,HttpServletRequest request){
        if (pageNum < 0 || pageNum >100 || StringUtils.isBlank(pageNum.toString())){
            return ResultGenerator.genFailResult("页码有误，请检查");
        }

        Integer userId = AuthUtil.getUserId(request);

        Page<ExamListDto> page = new Page<>(pageNum, CommonConstant.PAGE_SIZE);
        List<ExamListDto> questions = examService.listCreatedExam(userId);
        page.setRecords(questions);
        return ResultGenerator.genSuccessResult(page);
    }

    /**
     * 参与的考试
     * @param pageNum
     * @return
     */

    @GetMapping("listJoin//{pageNum}")
    @ApiOperation("我参与的考试")
    public Result listJoin(@PathVariable Integer pageNum,HttpServletRequest request){

        if (pageNum < 0 || pageNum >100 || StringUtils.isBlank(pageNum.toString())){
            return ResultGenerator.genFailResult("页码有误，请检查");
        }

        Integer userId = AuthUtil.getUserId(request);

        Page<ExamListDto> page = new Page<>(pageNum, CommonConstant.PAGE_SIZE);
        List<ExamListDto> questions = examService.listJoinExam(userId);
        page.setRecords(questions);
        return ResultGenerator.genSuccessResult(page);
    }

    @PostMapping("submit")
    @ApiOperation("提交试卷")
    public Result submit(@RequestBody ExamSubmitReq req){
        try{
            log.info("用户交卷 {}",req.toString());
            examService.submit(req.getRecordId(),req.getAnswerList());
        }catch (BusinessException e){
            return ResultGenerator.genFailResult(e.getMsg());
        }catch (Exception e){
            return ResultGenerator.genFailResult("交卷失败");
        }
        return ResultGenerator.genSuccessResult();
    }

    @GetMapping("analysis/{recordId}")
    @ApiOperation("查看试卷分析")
    public Result analysis(@PathVariable Integer recordId){
        try{
            ExamAnalysisRes res = examService.analysis(recordId);
            return ResultGenerator.genSuccessResult(res);
        }catch (BusinessException e){
            return ResultGenerator.genFailResult(e.getMsg());
        }catch (Exception e){
            return ResultGenerator.genFailResult("获取考试分析失败");
        }
    }

    @PostMapping("add")
    @ApiOperation("添加考试")
    public Result add(@RequestParam("code") String code, HttpServletRequest request){
        try{
            Integer userId = AuthUtil.getUserId(request);
            examService.add(code, userId);
            return ResultGenerator.genSuccessResult(true);
        }catch (BusinessException e){
            return ResultGenerator.genFailResult(e.getMsg());
        }catch (Exception e){
            return ResultGenerator.genFailResult("添加考试失败");
        }
    }

    @PostMapping("continue")
    @ApiOperation("继续考试")
    public Result contin(@RequestParam Integer recordId){
        try {
            ExamContinRes res = examService.contin(recordId);
            return ResultGenerator.genSuccessResult(res);
        }catch (BusinessException e){
            return ResultGenerator.genFailResult(e.getMsg());
        }catch (Exception e){
            return ResultGenerator.genFailResult("操作失败，请重试！");
        }


    }

}
