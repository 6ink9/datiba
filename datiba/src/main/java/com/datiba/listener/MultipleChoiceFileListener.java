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
import com.datiba.domain.excel.MultipleChoice;
import com.datiba.exception.BusinessException;
import com.datiba.service.PaperQuestionRecordService;
import com.datiba.service.QuestionService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Description:
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/16
 */
@Slf4j
public class MultipleChoiceFileListener implements ReadListener<MultipleChoice>  {

    private int paperId;
    private QuestionService questionService;
    private int bankId;
    private PaperQuestionRecordService recordService;
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<Question> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    int row = 1;


    public MultipleChoiceFileListener(int paperId, int bankId, QuestionService questionService, PaperQuestionRecordService recordService) {
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
            throw new BusinessException("多选题解析失败");
        }
    }

    public void invoke(MultipleChoice o, AnalysisContext analysisContext) {
        // 检查格式
        checkRow(o);

        List<ChoiceAnswer> answerList = new ArrayList<>();
        Set<String> answerSet = new HashSet<>(List.of(o.getRightChoice().split("/")));
        // 多选题至少有四个选项
        ChoiceAnswer A = new ChoiceAnswer(o.getChoiceA(),answerSet.contains("A")?1:0);
        ChoiceAnswer B = new ChoiceAnswer(o.getChoiceB(),answerSet.contains("B")?1:0);
        ChoiceAnswer C = new ChoiceAnswer(o.getChoiceC(),answerSet.contains("C")?1:0);
        ChoiceAnswer D = new ChoiceAnswer(o.getChoiceD(),answerSet.contains("D")?1:0);

        answerList.add(A);
        answerList.add(B);
        answerList.add(C);
        answerList.add(D);

        // 判断是否有EFG选项
        if (o.getChoiceE() != null  && StringUtils.isNotBlank(o.getChoiceE())){
            ChoiceAnswer E = new ChoiceAnswer(o.getChoiceE(),answerSet.contains("E")?1:0);
            answerList.add(E);
        }

        if (o.getChoiceF() != null  && StringUtils.isNotBlank(o.getChoiceF())){
            ChoiceAnswer F = new ChoiceAnswer(o.getChoiceF(),answerSet.contains("F")?1:0);
            answerList.add(F);
        }

        if (o.getChoiceG() != null  && StringUtils.isNotBlank(o.getChoiceG())){
            ChoiceAnswer G = new ChoiceAnswer(o.getChoiceG(),answerSet.contains("G")?1:0);
            answerList.add(G);
        }

        String answerMultiple = JSON.toJSONString(answerList);
        Question question = Question.builder()
                .question(o.getQuestion())
                .answerMultiple(answerMultiple)
                .analysis(StringUtils.isBlank(o.getAnalysis())?null:o.getAnalysis())
                .type(QuestionTypeEnum.MULTIPLE.getType())
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

    private void checkRow(MultipleChoice o) {
        if (o.getQuestion() == null  || StringUtils.isBlank(o.getQuestion())){
            log.error("多选题第" + row + "行中问题不能为空");
            throw new BusinessException("多选题第" + row + "行中问题不能为空");
        }

        if (o.getChoiceA() == null  || StringUtils.isBlank(o.getChoiceA())){
            log.error("多选题第" + row + "行中选项A不能为空");
            throw new BusinessException("多选题第" + row + "行中选项A不能为空");
        }

        if (o.getChoiceB() == null  || StringUtils.isBlank(o.getChoiceB())){
            log.error("多选题第" + row + "行中选项B不能为空");
            throw new BusinessException("多选题第" + row + "行中选项B不能为空");
        }

        if (o.getChoiceC() == null  || StringUtils.isBlank(o.getChoiceC())){
            log.error("多选题第" + row + "行中选项C不能为空");
            throw new BusinessException("多选题第" + row + "行中选项C不能为空");
        }

        if (o.getChoiceD() == null  || StringUtils.isBlank(o.getChoiceD())){
            log.error("多选题第" + row + "行中选项D不能为空");
            throw new BusinessException("多选题第" + row + "行中选项D不能为空");
        }

        if (o.getRightChoice() == null  || StringUtils.isBlank(o.getRightChoice())){
            log.error("多选题第" + row + "行中正确选项不能为空");
            throw new BusinessException("多选题第" + row + "行中正确选项不能为空");
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
