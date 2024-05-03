package com.datiba.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datiba.domain.ExamRecord;
import com.datiba.domain.Paper;
import com.datiba.domain.PaperQuestionRecord;
import com.datiba.domain.Question;
import com.datiba.domain.dto.PaperDetailRes;
import com.datiba.domain.dto.PaperSetDto;
import com.datiba.domain.dto.ScoreConfig;
import com.datiba.domain.excel.Fill;
import com.datiba.domain.excel.Judge;
import com.datiba.domain.excel.MultipleChoice;
import com.datiba.domain.excel.SingleChoice;
import com.datiba.exception.BusinessException;
import com.datiba.listener.FillFileListener;
import com.datiba.listener.JudgeFileListener;
import com.datiba.listener.MultipleChoiceFileListener;
import com.datiba.listener.SingleChoiceFileListener;
import com.datiba.mapper.PaperMapper;
import com.datiba.util.ScoreUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.datiba.common.constant.CommonConstant.RECENT_PAPER_SIZE;

/**
 * Description:
 * Param:
 * return:
 * Author:WuXiaotong
 * Date: 2024/2/8
 */
@Service
@Slf4j
public class PaperService extends ServiceImpl<PaperMapper, Paper> {

    @Autowired
    QuestionService questionService;

    @Autowired
    ExamRecordService examRecordService;

    @Autowired
    PaperQuestionRecordService paperQuestionRecordService;

    @Autowired
    UserPaperCollectionService collectionService;

    @Autowired
    SystemQuestionBankService systemQuestionBankService;

    @Transactional(rollbackFor = Exception.class)
    public void upload(MultipartFile file, String name, String desc, int bankId, int isPrivate, String eachScore,int userId) {
        // 查找题库名称
        String bankName = systemQuestionBankService.getById(bankId).getName();
        // 创建试卷
        Paper paper = Paper.builder()
                .createdBy(userId)
                .name(name)
                .description(desc)
                .bankId(bankId)
                .bankName(bankName)
                .isPrivate(isPrivate)
                .eachScore(eachScore)
                .build();
        save(paper);
        int paperId = paper.getId();

        try {
            InputStream in = file.getInputStream();
            ExcelReader excelReader = EasyExcel.read(in).build();
            // 读取上传的excel 按照题型读取
            ReadSheet s1 = EasyExcel.readSheet(SingleChoice.SHEET_NAME).registerReadListener(new SingleChoiceFileListener(paperId, bankId, questionService, paperQuestionRecordService)).head(SingleChoice.class).autoTrim(true).build();
            ReadSheet s2 = EasyExcel.readSheet(Judge.SHEET_NAME).registerReadListener(new JudgeFileListener(paperId, bankId, questionService, paperQuestionRecordService)).head(Judge.class).autoTrim(true).build();
            ReadSheet s3 = EasyExcel.readSheet(Fill.SHEET_NAME).registerReadListener(new FillFileListener(paperId, bankId, questionService, paperQuestionRecordService)).head(Fill.class).autoTrim(true).build();
            ReadSheet s4 = EasyExcel.readSheet(MultipleChoice.SHEET_NAME).registerReadListener(new MultipleChoiceFileListener(paperId, bankId, questionService, paperQuestionRecordService)).head(MultipleChoice.class).autoTrim(true).build();

            excelReader.read(s1, s2, s3, s4);
            excelReader.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 编题考试
     *
     * @param paperSetDto
     */
    @Transactional(rollbackFor = Exception.class)
    public void set(PaperSetDto paperSetDto, int userId) {
        log.info("paperSetDto: {}", paperSetDto);
        // 将手动添加的问题上传到题库
        if (paperSetDto.getQuestionList().size() > 0) {
            try {
                questionService.saveBatch(paperSetDto.getQuestionList());
            } catch (Exception e) {
                log.error("保存问题失败");
                throw new BusinessException("保存问题失败");
            }
        }
        // 添加一条试卷数据
        Paper paper = Paper.builder()
                .name(paperSetDto.getName())
                .description(paperSetDto.getDescription())
                .eachScore(paperSetDto.getEachScore())
                .bankId(paperSetDto.getBankId())
                .isPrivate(paperSetDto.getIsPrivate())
                .createdBy(userId)
                .build();

        try {
            save(paper);
        } catch (Exception e) {
            throw new BusinessException("保存试卷信息失败");
        }

        // 创建问题和试卷对应的paperQuestionRecord
        List<PaperQuestionRecord> paperQuestionRecords = new ArrayList<>();
        for (Question q : paperSetDto.getQuestionList()) {
            PaperQuestionRecord questionRecord = PaperQuestionRecord.builder()
                    .questionId(q.getId())
                    .paperId(paper.getId())
                    .build();
            paperQuestionRecords.add(questionRecord);
        }

        for (int id : paperSetDto.getBankQuestionId()) {
            PaperQuestionRecord questionRecord = PaperQuestionRecord.builder()
                    .questionId(id)
                    .paperId(paper.getId())
                    .build();
            paperQuestionRecords.add(questionRecord);
        }

        try {
            paperQuestionRecordService.saveBatch(paperQuestionRecords);
        } catch (Exception e) {
            log.error("保存记录失败");
            throw new BusinessException("保存记录失败");

        }
    }

    /**
     * 根据userId筛选出近期参与的试卷
     *
     * @param userId
     * @return
     */
    public List<Paper> getRecentPaper(Integer userId) {
        List<Integer> paperIds = examRecordService.getBaseMapper().listRecentPaper(userId,RECENT_PAPER_SIZE);
        if (paperIds.size() == 0){
            throw new BusinessException("该用户未参加过考试");
        }
        List<Paper> papers = listByIds(paperIds);
        return papers;
    }

    /**
     * 根据paperid获取试卷详情 包括参与的考试记录
     *
     * @param paperId
     * @return
     */
    public PaperDetailRes getPaperDetail(Integer paperId, Integer userId) {
        Paper paper = getById(paperId);
        List<ExamRecord> records = examRecordService.getBaseMapper().selectByPaperId(paperId, userId, null,null);
        // 题目数量统计
        List<Integer> questionIds = paperQuestionRecordService.getBaseMapper().searchQIdByPId(paperId);
        List<Question > questionList = questionService.listByIds(questionIds);
        // 统计各个题目的数量 el->表示默认键值为1,后续相加
        Map<Integer, Integer> questionNum = questionList.stream().collect(Collectors.toMap(Question::getType, el -> 1, Integer::sum));
        // 获得各个题目的分值
        ScoreConfig scoreConfig = JSON.parseObject(paper.getEachScore(), ScoreConfig.class);
        int totalScore = ScoreUtil.getFullScore(scoreConfig,questionNum);
        PaperDetailRes dto = PaperDetailRes.builder()
                .paper(paper)
                .examRecordList(records)
                .totalScore(totalScore)
                .questionNum(questionList.size())
                .build();
        return dto;
    }

    /**
     * 用户收藏试卷列表
     *
     * @param userId
     * @return
     */

    public List<Paper> getUserCollection(Integer userId) {
        List<Integer> paperIds = collectionService.getBaseMapper().getUserCollectionIds(userId);
        List<Paper> papers = new ArrayList<>();
        if (paperIds.size()>0){
            papers = getBaseMapper().getUserCollection(paperIds);
        }

        return papers;
    }
}
