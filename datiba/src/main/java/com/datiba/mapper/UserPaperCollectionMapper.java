package com.datiba.mapper;

import com.datiba.domain.UserPaperCollection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author universal
* @description 针对表【user_paper_collection】的数据库操作Mapper
* @createDate 2024-02-22 00:30:12
* @Entity com.datiba.domain.UserPaperCollection
*/
public interface UserPaperCollectionMapper extends BaseMapper<UserPaperCollection> {

    List<Integer> getUserCollectionIds(Integer userId);

    Boolean disCollectPaper(Integer userId, Integer paperId);
}




