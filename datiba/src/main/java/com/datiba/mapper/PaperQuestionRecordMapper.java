package com.datiba.mapper;

import com.datiba.domain.PaperQuestionRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datiba.domain.dto.PaperQuestionPair;

import java.util.List;

/**
* @author universal
* @description 针对表【paper_question_record】的数据库操作Mapper
* @createDate 2024-02-19 22:11:25
* @Entity com.datiba.domain.PaperQuestionRecord
*/
public interface PaperQuestionRecordMapper extends BaseMapper<PaperQuestionRecord> {

    List<Integer> searchQIdByPId(Integer paperId);

    void saveRecord(List<PaperQuestionPair> pairs);
}




