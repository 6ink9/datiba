package com.datiba.controller;

import com.datiba.domain.User;
import com.datiba.domain.dto.MyClazzDto;
import com.datiba.service.ClazzService;
import com.datiba.service.UserClassRecordService;
import com.datiba.util.AuthUtil;
import com.datiba.util.Result;
import com.datiba.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Description:
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/10
 */
@RestController
@Slf4j
@RequestMapping("/class")
@Api(tags = "班级管理")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    @Autowired
    private UserClassRecordService userClassRecordService;

    @ApiOperation(value = "班级列表")
    @GetMapping(path = "list")
    public Result list(HttpServletRequest request) {
        Integer userId = AuthUtil.getUserId(request);
        log.info("用户{}获取班级列表...", userId);
        List<MyClazzDto> res = clazzService.getBaseMapper().listClass(userId);
        return ResultGenerator.genSuccessResult(res);
    }

    /**
     * 创建班级
     *
     * @param name 班级名称
     * @return
     */
    @ApiOperation(value = "创建班级")
    @PostMapping(path = "create")
    public Result create(@RequestParam("name") String name,
                         HttpServletRequest request) {
        Integer userId = AuthUtil.getUserId(request);
        clazzService.createClazz(name, userId);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 退出班级/解散班级
     * @param recordId
     * @return
     */
    @ApiOperation(value = "退出班级")
    @GetMapping(path = "quit/{recordId}")
    public Result quit(@PathVariable Integer recordId, HttpServletRequest request) {
        Integer userId = AuthUtil.getUserId(request);
        String msg = clazzService.quitClazz(recordId, userId);
        return ResultGenerator.genSuccessResult(msg);

    }

    /**
     * 使用课程码加入课程
     *
     * @param code
     * @return
     */
    @ApiOperation(value = "加入班级")
    @PostMapping("join")
    public Result join(@RequestParam("code") String code, HttpServletRequest request) {
        Integer userId = AuthUtil.getUserId(request);
        clazzService.joinClazz(code, userId);
        return ResultGenerator.genSuccessResult(true);
    }

    @GetMapping("listStud/{classId}")
    @ApiOperation("学生列表")
    public Result listStud(HttpServletRequest request,@PathVariable Integer classId){
        Integer userId = AuthUtil.getUserId(request);
        List<User> studentList = clazzService.getStudList(userId, classId);
        return ResultGenerator.genSuccessResult(studentList);
    }

    @PostMapping("move")
    @ApiOperation("学生调班")
    public Result move(@RequestParam Integer userId, @RequestParam Integer fromClassId, @RequestParam Integer toClassId){
        clazzService.moveStudent(userId,fromClassId,toClassId);
        return ResultGenerator.genSuccessResult(true);

    }


}
