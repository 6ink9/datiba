package com.datiba.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datiba.common.constant.CollectEnum;
import com.datiba.domain.Paper;
import com.datiba.domain.UserPaperCollection;
import com.datiba.mapper.UserPaperCollectionMapper;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author universal
* @description 针对表【user_paper_collection】的数据库操作Service实现
* @createDate 2024-02-22 00:30:12
*/
@Service
@Slf4j
public class UserPaperCollectionService extends ServiceImpl<UserPaperCollectionMapper, UserPaperCollection> {

    public Boolean collectPaper(Integer paperId, Integer userId, int opt) {

        if (opt == CollectEnum.COLLECT.getCode()) {
            log.info("用户{}收藏了试卷{}",userId,paperId);
            UserPaperCollection collection = UserPaperCollection.builder()
                    .userId(userId)
                    .paperId(paperId)
                    .build();
            return save(collection);
        }else if(opt == CollectEnum.DIS_COLLECT.getCode()) {
            log.info("用户{}取消收藏了试卷{}",userId,paperId);
            return this.baseMapper.disCollectPaper(userId,paperId);
        }else{
            UserPaperCollection collection = getOne(new QueryWrapper<UserPaperCollection>().eq("user_id",userId).eq("paper_id",paperId));
            return collection==null ? false:true;
        }
    }


}




