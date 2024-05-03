package com.datiba.mapper;

import com.datiba.domain.UserClassRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author universal
* @description 针对表【user_class_record(用户-班级记录)】的数据库操作Mapper
* @createDate 2024-02-10 13:29:58
* @Entity com.datiba.domain.UserClassRecord
*/
public interface UserClassRecordMapper extends BaseMapper<UserClassRecord> {

    /**
     * 筛选出该班级下所有学生Id（除了创建人老师外）
     * @param clazzId
     * @param teacherId
     * @return
     */
    List<Integer> selectStudIdsByClazz(Integer clazzId, Integer teacherId);

    UserClassRecord check(Integer userId, Integer clazzId);

    boolean quitClazz(Integer recordId);
}




