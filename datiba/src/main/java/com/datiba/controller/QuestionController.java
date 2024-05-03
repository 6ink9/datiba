package com.datiba.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.datiba.common.constant.CommonConstant;
import com.datiba.domain.Question;
import com.datiba.exception.BusinessException;
import com.datiba.service.QuestionService;
import com.datiba.util.Result;
import com.datiba.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/2/22
 */
@RestController
@Slf4j
@RequestMapping("/question")
@Api(tags = "问题管理")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @PostMapping("list")
    @ApiOperation("问题列表")
    public Result list(@RequestParam("pageNum") Integer pageNum,
                       @RequestParam("bankId") Integer bankId,
                       @RequestParam(required = false, value = "type") Integer type,
                       @RequestParam(required = false, value = "keyword") String keyword) {

        if (pageNum < 0 || pageNum > 100 || StringUtils.isBlank(pageNum.toString())) {
            return ResultGenerator.genFailResult("页码有误，请检查");
        }

        Page<Question> page = new Page<>(pageNum, CommonConstant.PAGE_SIZE);
        List<Question> questions = questionService.getBaseMapper().listQuestion(bankId, type, keyword);
        page.setRecords(questions);
        return ResultGenerator.genSuccessResult(page);
    }

    @PostMapping("random")
    @ApiOperation("随机刷题")
    public Result list(@RequestParam("number") String number,
                       @RequestParam("bankId") Integer bankId) {
        int num = Integer.parseInt(number);
        if (num < 0 || num > 50 || StringUtils.isBlank(number)){
            return ResultGenerator.genFailResult("数量有误，请检查");
        }
        try {
            List<Question> randomQuestions = questionService.getRandomQuestion(num, bankId);
            return ResultGenerator.genSuccessResult(randomQuestions);
        }catch (BusinessException e){
            return ResultGenerator.genFailResult(e.getMessage());
        }catch (Exception e){
            return ResultGenerator.genFailResult("生成题目失败！请稍后重试");
        }
    }

}
