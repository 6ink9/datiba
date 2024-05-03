package com.datiba.controller;

import com.datiba.domain.dto.BankMenuDto;
import com.datiba.service.SystemQuestionBankService;
import com.datiba.util.Result;
import com.datiba.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/2/17
 */
@RestController
@Slf4j
@RequestMapping("/bank")
@Api(tags = "题库管理")
public class QuestionBankController {

    @Autowired
    SystemQuestionBankService bankService;

    @GetMapping("list")
    @ApiOperation("题库列表")
    public Result list(){
        List<BankMenuDto> menu = bankService.listBank();
        return ResultGenerator.genSuccessResult(menu);
    }
}
