package com.datiba.mapper;

import com.datiba.domain.SystemQuestionBank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.datiba.domain.dto.BankMenuDto;
import com.datiba.domain.dto.SysQuestionBankDto;

import java.util.List;

/**
* @author universal
* @description 针对表【system_question_bank】的数据库操作Mapper
* @createDate 2024-02-17 20:49:48
* @Entity com.datiba.domain.SystemQuestionBank
*/
public interface SystemQuestionBankMapper extends BaseMapper<SystemQuestionBank> {

    List<BankMenuDto> selectMenu();

    List<SysQuestionBankDto> listDto();
}




