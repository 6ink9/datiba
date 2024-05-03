package com.datiba.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datiba.domain.Clazz;
import com.datiba.domain.dto.MyClazzDto;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * Description:
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/10
 */
public interface ClazzMapper extends BaseMapper<Clazz> {

    public List<MyClazzDto> listClass(Integer userId);


}
