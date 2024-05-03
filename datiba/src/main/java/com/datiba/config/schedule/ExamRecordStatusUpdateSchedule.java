package com.datiba.config.schedule;

import com.datiba.service.ExamRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/4/8
 */
@Component
@Slf4j
public class ExamRecordStatusUpdateSchedule {

    @Autowired
    private ExamRecordService recordService;

    @Scheduled(cron = "0 * * * * ?")
    public void execute() {
        try {
            recordService.getBaseMapper().updateStatus(new Date());
        }catch (Exception e){
            log.error("自动更新考试记录状态失败");
        }

    }
}
