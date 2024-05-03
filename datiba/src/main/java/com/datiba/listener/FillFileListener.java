package com.datiba.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.datiba.common.constant.QuestionTypeEnum;
import com.datiba.domain.PaperQuestionRecord;
import com.datiba.domain.Question;
import com.datiba.domain.dto.PaperQuestionPair;
import com.datiba.domain.excel.Fill;
import com.datiba.exception.BusinessException;
import com.datiba.service.PaperQuestionRecordService;
import com.datiba.service.QuestionService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Param:
 * return:
 * @author:WuXiaotong
 * Date: 2024/2/16
 */
@Slf4j
public class FillFileListener implements ReadListener<Fill>  {

    private int paperId;

    private QuestionService questionService;

    private int bankId;

    private static final int BATCH_COUNT = 100;

    private PaperQuestionRecordService recordService;
    /**
     * 缓存的数据
     */
    private List<Question> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    int row = 1;


    public FillFileListener(int paperId, int bankId, QuestionService questionService, PaperQuestionRecordService recordService) {
        this.paperId = paperId;
        this.bankId = bankId;
        this.questionService = questionService;
        this.recordService = recordService;
    }

    @Override
    public void onException(Exception e, AnalysisContext analysisContext){
        if (e instanceof BusinessException){
            throw new BusinessException(((BusinessException) e).getMsg());
        }else{
            throw new BusinessException("填空题解析失败");
        }
    }

    public void invoke(Fill o, AnalysisContext analysisContext) {
        // TODO paper_question_record 还没有添加
        if (o.getQuestion() == null  || StringUtils.isBlank(o.getQuestion())){
            log.error("填空题第" + row + "行中问题不能为空");
            throw new BusinessException("填空题第" + row + "行中问题不能为空");
        }

        if (o.getAnswer1() == null  || StringUtils.isBlank(o.getAnswer1())){
            log.error("填空题第" + row + "行中答案1不能为空");
            throw new BusinessException("填空题第" + row + "行中答案1不能为空");
        }

        // 填空题最多五个答案 至少有一个答案
        List<String> answerList = new ArrayList<>();
        answerList.add(o.getAnswer1());
        // 判断有其他答案
        if (o.getAnswer2() != null  && StringUtils.isNotBlank(o.getAnswer2())){
            answerList.add(o.getAnswer2());
        }

        if (o.getAnswer3() != null  && StringUtils.isNotBlank(o.getAnswer3())){
            answerList.add(o.getAnswer3());
        }

        if (o.getAnswer4() != null  && StringUtils.isNotBlank(o.getAnswer4())){
            answerList.add(o.getAnswer4());
        }

        if (o.getAnswer5() != null  && StringUtils.isNotBlank(o.getAnswer5())){
            answerList.add(o.getAnswer5());
        }

        Question question = Question.builder()
                .question(o.getQuestion())
                .answerFill(JSON.toJSONString(answerList))
                .analysis(StringUtils.isBlank(o.getAnalysis())?null:o.getAnalysis())
                .type(QuestionTypeEnum.FILL.getType())
                .bankId(bankId)
                .build();
        // 加入到缓存列表中
        cachedDataList.add(question);

        // 达到存储上限
        if (cachedDataList.size() >= BATCH_COUNT) {
            save();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }

    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        save();
    }

    public void save(){
        questionService.saveBatch(cachedDataList);
        // 添加到paperQuestionRecord
        List<PaperQuestionRecord> records = new ArrayList<>();
        cachedDataList.stream().forEach(i -> records.add(new PaperQuestionRecord(paperId,i.getId())));
        recordService.saveBatch(records);
    }

}
