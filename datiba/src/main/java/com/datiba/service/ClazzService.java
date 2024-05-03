package com.datiba.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datiba.domain.Clazz;
import com.datiba.domain.User;
import com.datiba.domain.UserClassRecord;
import com.datiba.exception.BusinessException;
import com.datiba.mapper.ClazzMapper;
import com.datiba.util.CodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/10
 */
@Service
@Slf4j
public class ClazzService extends ServiceImpl<ClazzMapper, Clazz> {

    @Autowired
    private UserClassRecordService userClassRecordService;

    @Autowired
    private UserService userService;
    public void joinClazz(String code, Integer userId){
        // 判断课程是否存在
        QueryWrapper<Clazz> qw = new QueryWrapper<>();
        qw.eq("code",code);
        Clazz clazz = getOne(qw);
        if (clazz == null){
            log.error("班级编号{}不存在",code);
            throw new BusinessException("班级编号不存在");
        }

        // 查看用户是否已经加入了该课程
        UserClassRecord exitRecord = userClassRecordService.getBaseMapper().check(userId,clazz.getId());
        if (exitRecord != null){
            log.error("用户{}重复加入班级，操作失败",userId);
            throw new BusinessException("你已经加入了该班级");
        }

        // 新增一条课程记录
        UserClassRecord record = UserClassRecord.builder()
                .classId(clazz.getId())
                .userId(userId)
                .build();
        userClassRecordService.save(record);
    }

    public void createClazz(String name, Integer createdBy) {
        log.info("教师用户{}正在创建班级,名称为{}...",createdBy,name);
        // 创建班级
        Clazz clazz = Clazz.builder()
                .name(name)
                .createdBy(createdBy)
                .code(CodeUtil.generateShortUuid())
                .build();
        save(clazz);
        int cId = clazz.getId();
        // 新增一条记录
        UserClassRecord record = UserClassRecord.builder()
                .classId(cId)
                .userId(createdBy)
                .build();
        userClassRecordService.save(record);
        log.info("班级{}创建成功！",name);
    }


    public void dissolved(Integer classId) {
        // 先删除user_class_record
        userClassRecordService.remove(new QueryWrapper<UserClassRecord>().eq("class_id",classId));
        // 再删除班级信息
        this.removeById(classId);
    }

    public String quitClazz(Integer recordId, Integer userId) {
        UserClassRecord record = userClassRecordService.getById(recordId);
        Clazz clazz = getById(record.getClassId());
        // 创建人退出班级 —— 解散班级
        if(clazz.getCreatedBy() == userId){
            log.info("教师用户{}解散班级{}...",userId,clazz.getId());
            // 解散班级
            dissolved(clazz.getId());
            return "解散班级成功";
        }
        log.info("学生用户{}退出班级{}...",userId,clazz.getId());
        userClassRecordService.getBaseMapper().quitClazz(recordId);
        return "退出班级成功";
    }

    /**
     * 获取某班级的学生列表
     * @param userId
     * @param classId
     * @return
     */
    public List<User> getStudList(Integer userId, Integer classId) {
        log.info("用户{}获取班级{}下学生列表...",userId,classId);
        List<Integer> studentIds = userClassRecordService.getBaseMapper().selectStudIdsByClazz(classId,userId);
        if (studentIds.size() == 0){
            throw  new BusinessException("该班级下没有学生");
        }
        List<User> students = userService.getBaseMapper().selectBatchIds(studentIds);
        return students;
    }

    /**
     * 学生调班
     * @param userId
     * @param fromClassId
     * @param toClassId
     */
    public void moveStudent(Integer userId, Integer fromClassId, Integer toClassId) {
        log.info("正在将学生{}从班级{}调整到班级{}...",userId,fromClassId,toClassId);
        userClassRecordService.remove(new QueryWrapper<UserClassRecord>().eq("user_id",userId).eq("class_id",fromClassId));
        UserClassRecord record = UserClassRecord.builder()
                .classId(toClassId)
                .userId(userId)
                .build();
        userClassRecordService.save(record);
        log.info("学生调班成功！");
    }
}
