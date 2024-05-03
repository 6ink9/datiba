package com.datiba.mapper;

import com.datiba.domain.ExamReleaseRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author universal
* @description 针对表【exam_release_record】的数据库操作Mapper
* @createDate 2024-03-14 23:15:53
* @Entity com.datiba.domain.ExamReleaseRecord
*/
public interface ExamReleaseRecordMapper extends BaseMapper<ExamReleaseRecord> {

    List<Integer> getReleasePaperIds(Integer userId);
}




