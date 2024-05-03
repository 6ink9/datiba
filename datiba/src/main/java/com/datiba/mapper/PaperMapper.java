package com.datiba.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datiba.domain.Paper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description:
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/8
 */
public interface PaperMapper extends BaseMapper<Paper> {

    public List<Paper> listPaper(@Param("order") String order, @Param("bankId") Integer bankId, @Param("keyword") String keyword, int userId);

    List<Paper> listHotPaper(int hotPaperSize);

    List<Paper> getUserCreated(Integer userId);

    List<Paper> getUserCollection(List<Integer> paperIds);
}
