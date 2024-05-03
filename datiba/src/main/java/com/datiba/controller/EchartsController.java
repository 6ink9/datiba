package com.datiba.controller;

import com.datiba.domain.ExamVisualAnalysis;
import com.datiba.domain.ExamVisualAnalysisStud;
import com.datiba.service.EchartsService;
import com.datiba.util.AuthUtil;
import com.datiba.util.Result;
import com.datiba.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/2/27
 */
@RestController
@Slf4j
@RequestMapping("/echarts")
@Api(tags = "可视化数据")
public class EchartsController {

    @Autowired
    EchartsService echartsService;

    @GetMapping("analysis/teacher/{paperId}")
    @ApiOperation("教师版考试分析")
    public Result analysis(HttpServletRequest request, @PathVariable Integer paperId){
        Integer userId = AuthUtil.getUserId(request);
        ExamVisualAnalysis examVisualAnalysis = echartsService.getVisualData(userId,paperId);
        return ResultGenerator.genSuccessResult(examVisualAnalysis);
    }

    @GetMapping("analysis/student/{classId}")
    @ApiOperation("学生版考试分析")
    public Result  analysisStud(HttpServletRequest request, @PathVariable Integer classId){
        Integer userId = AuthUtil.getUserId(request);
        ExamVisualAnalysisStud examVisualAnalysis = echartsService.getStudVisualData(userId,classId);
        return  ResultGenerator.genSuccessResult(examVisualAnalysis);
    }
}
