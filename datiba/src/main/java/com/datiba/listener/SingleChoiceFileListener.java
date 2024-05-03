package com.datiba.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.datiba.common.constant.QuestionTypeEnum;
import com.datiba.domain.PaperQuestionRecord;
import com.datiba.domain.answer.ChoiceAnswer;
import com.datiba.domain.Question;
import com.datiba.domain.dto.PaperQuestionPair;
import com.datiba.domain.excel.SingleChoice;
import com.datiba.exception.BusinessException;
import com.datiba.service.PaperQuestionRecordService;
import com.datiba.service.QuestionService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/16
 */
@Slf4j
public class SingleChoiceFileListener implements ReadListener<SingleChoice>  {

    private int paperId;

    private QuestionService questionService;
    private PaperQuestionRecordService recordService;

    private int bankId;

    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<Question> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    int row = 1;


    public SingleChoiceFileListener(int paperId, int bankId, QuestionService questionService, PaperQuestionRecordService recordService) {
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
            throw new BusinessException("单选题解析失败");
        }
    }

    public void invoke(SingleChoice o, AnalysisContext analysisContext) {
        if (o.getQuestion() == null  || StringUtils.isBlank(o.getQuestion())){
            log.error("单选题第" + row + "行中问题不能为空");
            throw new BusinessException("单选题第" + row + "行中问题不能为空");
        }

        if (o.getChoiceA() == null  || StringUtils.isBlank(o.getChoiceA())){
            log.error("单选题第" + row + "行中选项A不能为空");
            throw new BusinessException("单选题第" + row + "行中选项A不能为空");
        }

        if (o.getChoiceB() == null  || StringUtils.isBlank(o.getChoiceB())){
            log.error("单选题第" + row + "行中选项B不能为空");
            throw new BusinessException("单选题第" + row + "行中选项B不能为空");
        }

        if (o.getRightChoice() == null  || StringUtils.isBlank(o.getRightChoice())){
            log.error("单选题第" + row + "行中正确选项不能为空");
            throw new BusinessException("单选题第" + row + "行中正确选项不能为空");
        }


        String rightChoice = o.getRightChoice();
        // 单选题默认四个选项 至少有两个选项
        List<ChoiceAnswer> answerList = new ArrayList<>();
        ChoiceAnswer A = new ChoiceAnswer(o.getChoiceA(),rightChoice.equals("A")?1:0);
        ChoiceAnswer B = new ChoiceAnswer(o.getChoiceB(),rightChoice.equals("B")?1:0);
        answerList.add(A);
        answerList.add(B);
        // 判断是否有C、D两个选项
        if (o.getChoiceC() != null  && StringUtils.isNotBlank(o.getChoiceC())){
            ChoiceAnswer C = new ChoiceAnswer(o.getChoiceC(),rightChoice.equals("C")?1:0);
            answerList.add(C);
        }

        if (o.getChoiceD() != null  && StringUtils.isNotBlank(o.getChoiceD())){
            ChoiceAnswer D = new ChoiceAnswer(o.getChoiceD(),rightChoice.equals("D")?1:0);
            answerList.add(D);
        }

        String answerSingle = JSON.toJSONString(answerList);
        Question question = Question.builder()
                .question(o.getQuestion())
                .answerSingle(answerSingle)
                .analysis(StringUtils.isBlank(o.getAnalysis())?null:o.getAnalysis())
                .type(QuestionTypeEnum.SINGLE.getType())
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
