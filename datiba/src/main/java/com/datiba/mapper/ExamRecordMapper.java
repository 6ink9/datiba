package com.datiba.mapper;

import com.datiba.domain.ExamRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Date;
import java.util.List;

/**
* @author universal
* @description 针对表【paper_record(考试记录表)】的数据库操作Mapper
* @createDate 2024-02-17 22:25:00
* @Entity com.datiba.domain.ExamRecord
*/
public interface ExamRecordMapper extends BaseMapper<ExamRecord> {

    List<ExamRecord> selectByUserId(Integer userId);

    List<Integer> listRecentPaper(Integer userId, Integer size);

    /***
     * 使用unfinish为了查找是否有未开始或者进行中的考试
     * @param paperId
     * @param userId
     * @param unfinish
     * @param status
     * @return
     */
    List<ExamRecord> selectByPaperId(Integer paperId,Integer userId,Integer unfinish, Integer status);

    List<ExamRecord> selectByExamId(List<Integer> examIds);

    List<ExamRecord> selectClazzRecord(List<Integer> studentIds, int examId);

    ExamRecord getUserExamRecord(Integer examId, Integer userId);

    void updateStatus(Date now);
}




