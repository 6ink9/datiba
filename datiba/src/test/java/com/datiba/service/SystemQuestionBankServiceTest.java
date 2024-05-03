package com.datiba.service;

import com.datiba.domain.dto.BankMenuDto;
import com.datiba.util.TimeUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

/**
 * Description:
 * Param:
 * return:
 *
 * @author:WuXiaotong Date: 2024/2/17
 */
@SpringBootTest
class SystemQuestionBankServiceTest {

    @Autowired
    SystemQuestionBankService systemQuestionBankService;

    @Test
    void listBank() {
        List<BankMenuDto> dtoList = systemQuestionBankService.listBank();
        System.out.println(dtoList.toString());
    }

    @Test
    void transfer(){
        String s = "2023-04-09 00:00:00,2023-05-09 23:59:00";
        Date date = TimeUtil.strToDate(s.split(",")[1]);
        System.out.println(date);
        Date now = new Date();
        System.out.println(date.compareTo(now)); // -1
    }


}