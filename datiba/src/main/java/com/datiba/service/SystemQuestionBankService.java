package com.datiba.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datiba.domain.SystemQuestionBank;
import com.datiba.domain.dto.BankMenuDto;
import com.datiba.domain.dto.SysQuestionBankDto;
import com.datiba.mapper.SystemQuestionBankMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author universal
* @description 针对表【system_question_bank】的数据库操作Service实现
* @createDate 2024-02-17 20:49:48
*/
@Service
public class SystemQuestionBankService extends ServiceImpl<SystemQuestionBankMapper, SystemQuestionBank> {

    @Resource
    SystemQuestionBankMapper bankMapper;
    public List<BankMenuDto> listBank() {
        // 查找一级大类
        List<BankMenuDto> menuList = bankMapper.selectMenu();
        List<SysQuestionBankDto> sysQuestionBankDtos = bankMapper.listDto();
        Map<Integer, List<SysQuestionBankDto>> dtoMap = sysQuestionBankDtos.stream().collect(Collectors.groupingBy(SysQuestionBankDto::getParentId));
        for (BankMenuDto menuDto : menuList){
            int parentId = menuDto.getId();
            if (dtoMap.containsKey(parentId)){
                menuDto.setChilds(dtoMap.get(parentId));
            }
        }
        return menuList;
    }
}




