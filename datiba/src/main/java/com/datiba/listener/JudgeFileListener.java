package com.datiba.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson2.JSON;
import com.datiba.common.constant.JudgeAnswerEnum;
import com.datiba.common.constant.QuestionTypeEnum;
import com.datiba.domain.PaperQuestionRecord;
import com.datiba.domain.Question;
import com.datiba.domain.dto.PaperQuestionPair;
import com.datiba.domain.excel.Judge;
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
 * @author:WuXiaotong
 * Date: 2024/2/17
 */
@Slf4j
public class JudgeFileListener implements ReadListener<Judge> {

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
    public JudgeFileListener(int paperId, int bankId, QuestionService questionService, PaperQuestionRecordService recordService) {
        this.paperId = paperId;
        this.bankId = bankId;
        this.questionService = questionService;
        this.recordService = recordService;
    }

    @Override
    public void onException(Exception e, AnalysisContext context){
        if (e instanceof BusinessException){
            throw new BusinessException(((BusinessException) e).getMsg());
        }else{
            throw new BusinessException("判断题解析失败");
        }
    }

    @Override
    public void invoke(Judge o, AnalysisContext analysisContext) {
        if (o.getQuestion() == null  || StringUtils.isBlank(o.getQuestion())){
            log.error("判断题" + row + "行中问题不能为空");
            throw new BusinessException("判断题" + row + "行中问题不能为空");
        }

        if (o.getAnswer() == null  || StringUtils.isBlank(o.getAnswer())){
            log.error("判断题" + row + "行中正确答案不能为空");
            throw new BusinessException("判断题" + row + "行中正确答案不能为空");
        }

        if (!o.getAnswer().equals(JudgeAnswerEnum.TRUE.getMsg())  && !o.getAnswer().equals(JudgeAnswerEnum.FALSE.getMsg())){
            log.error("判断题" + row + "行中正确答案必须为正确或者错误");
            throw new BusinessException("判断题" + row + "行中正确答案必须为正确或者错误");
        }

        Question question = Question.builder().build();
        question.setQuestion(o.getQuestion());
        question.setAnswerJudge(o.getAnswer().equals(JudgeAnswerEnum.TRUE.getMsg())?JudgeAnswerEnum.TRUE.getCode():JudgeAnswerEnum.FALSE.getCode());
        question.setType(QuestionTypeEnum.JUDGE.getType());
        question.setBankId(bankId);

        question.setAnalysis(StringUtils.isBlank(o.getAnalysis())?null:o.getAnalysis());
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
