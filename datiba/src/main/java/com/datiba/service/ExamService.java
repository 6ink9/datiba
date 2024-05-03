package com.datiba.service;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.datiba.common.constant.ExamRecordStatusEnum;
import com.datiba.common.constant.JudgeAnswerEnum;
import com.datiba.common.constant.QuestionTypeEnum;
import com.datiba.common.constant.UserRoleEnum;
import com.datiba.domain.*;
import com.datiba.domain.answer.UserAnswer;
import com.datiba.domain.answer.UserChoiceAnswer;
import com.datiba.domain.dto.*;
import com.datiba.domain.res.ExamAnalysisRes;
import com.datiba.domain.res.ExamContinRes;
import com.datiba.domain.res.ExamStartRes;
import com.datiba.exception.BusinessException;
import com.datiba.mapper.ExamMapper;
import com.datiba.util.CodeUtil;
import com.datiba.util.ScoreUtil;
import com.datiba.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExamService extends ServiceImpl<ExamMapper, Exam> {

    @Autowired
    QuestionService questionService;

    @Autowired
    ExamRecordService recordService;

    @Autowired
    PaperService paperService;

    @Autowired
    PaperQuestionRecordService paperQuestionRecordService;

    @Autowired
    UserClassRecordService userClassRecordService;

    @Autowired
    ExamReleaseRecordService examReleaseRecordService;

    @Autowired
    UserService userService;

    /**
     * 检查该试卷是否有未完成的考试
     * @param paperId
     * @param userId
     */
    public void check(int paperId, int userId) {
        List<ExamRecord> oldRecord = recordService.getBaseMapper().selectByPaperId(paperId, userId, ExamRecordStatusEnum.FINISH.getCode(), null);
        if (oldRecord.size() != 0) {
            log.error("用户 {} 在试卷 {} 下仍有进行中的考试，请先完成", userId, paperId);
            throw new BusinessException("该试卷仍有进行中的考试，请先完成");
        }

        Paper paper = paperService.getBaseMapper().selectById(paperId);
        if (paper == null) {
            throw new BusinessException("该试卷不存在");
        }

    }


    /**
     * 创建考试弹窗->立即考试
     */
    public ExamStartRes start(Integer recordId) {
        log.info("考试{}开始...", recordId);
        ExamRecord record = recordService.getById(recordId);
        Exam exam = this.getById(record.getExamId());
        Paper paper = paperService.getById(record.getPaperId());

        Date startTime = null;
        Date endTime = null;
        if (exam.getDateRange() != null) {
            startTime = TimeUtil.strToDate(exam.getDateRange().split(",")[0]);
            endTime = TimeUtil.strToDate(exam.getDateRange().split(",")[1]);
            Date now = new Date();
            if (now.compareTo(startTime) < 0) {
                // 考试还未到开始时间
                throw new BusinessException("考试未开始，请稍后重试");
            }

            if (now.compareTo(endTime) > 0) {
                // 考试已经结束
                throw new BusinessException("考试已结束");
            }
        }
        // 根据paperId找到问题列表
        List<Integer> questionIds = paperQuestionRecordService.getBaseMapper().searchQIdByPId(exam.getPaperId());
        List<Question> questionList = questionService.getBaseMapper().selectBatchIds(questionIds);


        // 更新考试记录中的考试状态为进行中,并更新考试开始时间
        Date examStartTime = new Date();
        record.setStatus(ExamRecordStatusEnum.ONGOING.getCode());
        record.setStartTime(examStartTime);

        recordService.getBaseMapper().updateById(record);

        // 构建返回值
        ExamStartRes res = ExamStartRes.builder()
                .questionList(questionList)
                .startTime(examStartTime)
                .ddl(endTime)
                .countDownMinutes(exam.getCountdownMinutes())
                .eachScore(paper.getEachScore())
                .build();
        log.info("ExamStartRes: {}", res.toString());
        return res;
    }


    /**
     * 继续考试
     *
     * @param recordId
     */
    public ExamContinRes contin(Integer recordId) {
        ExamRecord record = recordService.getBaseMapper().selectById(recordId);
        Exam exam = this.getById(record.getExamId());
        Paper paper = paperService.getById(record.getPaperId());

        Date endTime = null;
        Date now = new Date();
        if (exam.getDateRange() != null) {
            endTime = TimeUtil.strToDate(exam.getDateRange().split(",")[1]);
            if (now.compareTo(endTime) > 0) {
                // 考试已经结束
                throw new BusinessException("考试已结束");
            }
        }

        // 判断是否已经超过倒计时时间
        if (exam.getCountdownMinutes() != null && record.getStartTime() != null) {
            Duration duration = Duration.between(record.getStartTime().toInstant(), now.toInstant());
            log.info(String.valueOf(duration.toMinutes()));
            if (duration.toMinutes() >= exam.getCountdownMinutes()) {
                record.setStatus(ExamRecordStatusEnum.FINISH.getCode());
                record.setEndTime(now);
                recordService.updateById(record);
                throw new BusinessException("考试倒计时已经结束！");
            }
        }


        // 根据paperId找到问题列表
        List<Integer> questionIds = paperQuestionRecordService.getBaseMapper().searchQIdByPId(exam.getPaperId());
        List<Question> questionList = questionService.getBaseMapper().selectBatchIds(questionIds);

        // 构建返回值
        ExamContinRes res = ExamContinRes.builder()
                .questionList(questionList)
                .startTime(record.getStartTime())
                .ddl(endTime)
                .countDownMinutes(exam.getCountdownMinutes())
                .eachScore(paper.getEachScore())
                .build();

        log.info("ExamContinRes: {}", res.toString());
        return res;

    }


    /**
     * 用户参与的考试
     */
    public List<ExamListDto> listJoinExam(Integer userId) {
        List<ExamRecord> records = recordService.getBaseMapper().selectByUserId(userId);
        if (records.size() == 0) {
            throw new BusinessException("用户未参加过考试");
        }
        List<Integer> paperIds = records.stream().map(ExamRecord::getPaperId).toList();
        List<Paper> paperList = paperService.listByIds(paperIds);
        // paperId -> paperName
        Map<Integer, String> paperIdToName = paperList.stream().collect(Collectors.toMap(Paper::getId, Paper::getName));

        // 组装返回值
        List<ExamListDto> dtoList = new ArrayList<>();
        for (ExamRecord record : records) {
            ExamListDto dto = ExamListDto.builder()
                    .recordId(record.getId())
                    .correctCount(record.getCorrectCount())
                    .score(record.getScore())
                    .status(record.getStatus())
                    .paperName(paperIdToName.get(record.getPaperId()))
                    .startTime(record.getStartTime())
                    .build();
            dtoList.add(dto);
        }
        return dtoList;
    }

    /**
     * 用户创建的考试
     *
     * @param userId
     * @return
     */
    public List<ExamListDto> listCreatedExam(Integer userId) {
        // 筛选出由用户创建的考试
        QueryWrapper<Exam> qw = new QueryWrapper<>();
        qw.eq("created_by", userId);
        List<Exam> examList = this.list(qw);
        List<Integer> examIds = examList.stream().map(Exam::getId).toList();

        // 根据考试id筛选出考试记录
        List<ExamRecord> records = recordService.getBaseMapper().selectByExamId(examIds);

        // paperId -> Records
        Map<Integer, ExamRecord> recordHashMap = records.stream().collect(Collectors.toMap(ExamRecord::getExamId, record -> record));

        // 组装返回值
        List<ExamListDto> dtoList = new ArrayList<>();
        for (Exam exam : examList) {
            ExamRecord record = recordHashMap.get(exam.getId());
            ExamListDto dto = ExamListDto.builder()
                    .paperName(exam.getName())
                    .recordId(record.getId())
                    .correctCount(record.getCorrectCount())
                    .score(record.getScore())
                    .status(record.getStatus())
                    .build();

            dtoList.add(dto);
        }
        return dtoList;
    }


    /**
     * 点击考试记录 进入考试记录详情
     *
     * @param recordId
     * @return
     */
    public ExamRecordDetailDto getExamDetail(Integer recordId) {
        ExamRecord record = recordService.getById(recordId);
        int examId = record.getExamId();
        Exam exam = getById(examId);
        ExamRecordDetailDto dto = ExamRecordDetailDto.builder()
                .examRecord(record)
                .exam(exam)
                .build();
        return dto;
    }

    /**
     * 试卷详情页——>创建考试
     *
     * @param examSetDto
     * @return
     */

    @Transactional(rollbackFor = Exception.class)
    public Integer create(ExamSetDto examSetDto, Integer userId) {
        // 检查该试卷下是否有未完成的考试
        check(examSetDto.getPaperId(), userId);


        String code = CodeUtil.generateShortUuid();
        // 创建一条exam数据
        Exam exam = Exam.builder()
                .paperId(examSetDto.getPaperId())
                .countdownMinutes(examSetDto.getCountDownMinutes())
                .dateRange(examSetDto.getDateRange())
                .createdBy(userId)
                .code(code)
                .name(examSetDto.getPaperName())
                .build();

        save(exam);
        Date endTime = null;
        if (examSetDto.getDateRange() != null) {
            String end = examSetDto.getDateRange().split(",")[1];
            if (end != null) {
                endTime = TimeUtil.strToDate(end);
            }
        }
        // 添加考试记录（以便展示待进行考试）
        ExamRecord record = ExamRecord.builder()
                .userId(userId)
                .examId(exam.getId())
                .paperId(examSetDto.getPaperId())
                .status(ExamRecordStatusEnum.UNSTARTED.getCode())
                .ddl(endTime)
                .build();

        recordService.save(record);
        return record.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public String release(ExamReleaseDto dto, Integer userId) {
        log.info("用户{}发布考试...", userId);
        User user = userService.getById(userId);
        if (UserRoleEnum.TEACHER.getCode() != user.getRole()) {
            throw new BusinessException("用户" + userId + "无操作权限");
        }
        String code = CodeUtil.generateShortUuid();
        // 创建一条exam数据
        Exam exam = Exam.builder()
                .paperId(dto.getPaperId())
                .countdownMinutes(dto.getCountDownMinutes())
                .dateRange(dto.getDateRange())
                .createdBy(userId)
                .code(code)
                .name(dto.getPaperName())
                .build();

        save(exam);

        // 创造一条考试发布记录
        ExamReleaseRecord releaseRecord = ExamReleaseRecord.builder()
                .examId(exam.getId())
                .classId(dto.getClazzId())
                .userId(userId)
                .paperId(dto.getPaperId())
                .build();

        examReleaseRecordService.save(releaseRecord);


        // 获取截至时间
        Date endTime = null;
        if (dto.getDateRange() != null) {
            String end = dto.getDateRange().split(",")[1];
            if (end != null) {
                endTime = TimeUtil.strToDate(end);
            }
        }
        // 插入一个班同学的考试记录 状态为未开始
        List<Integer> userIds = userClassRecordService.getBaseMapper().selectStudIdsByClazz(dto.getClazzId(), userId);
        List<ExamRecord> records = new ArrayList<>();
        for (int i : userIds) {
            ExamRecord record = ExamRecord.builder()
                    .userId(i)
                    .examId(exam.getId())
                    .paperId(dto.getPaperId())
                    .status(ExamRecordStatusEnum.UNSTARTED.getCode())
                    .ddl(endTime)
                    .build();
            records.add(record);
        }

        recordService.saveBatch(records);
        return exam.getCode();
    }

    /**
     * 交卷改卷
     *
     * @param recordId
     * @param answerList
     */
    public void submit(Integer recordId, List<UserAnswer> answerList) {
        log.info("批改试卷中，recordId:{}", recordId);
        // 获取record
        ExamRecord record = recordService.getById(recordId);
        Paper paper = paperService.getById(record.getPaperId());
        // 获取各个题目的分值
        ScoreConfig scoreConfig = JSON.parseObject(paper.getEachScore(), ScoreConfig.class);
        Map<Integer, Integer> questionNum = answerList.stream().collect(Collectors.toMap(UserAnswer::getType, el -> 1, Integer::sum));
        int totalScore = ScoreUtil.getFullScore(scoreConfig, questionNum);
        int grade = 0;
        int correctCount = 0;
        List<QuestionAnalysis> analysisList = new ArrayList<>();
        for (UserAnswer answer : answerList) {
            QuestionAnalysis analysis = new QuestionAnalysis();
            if (answer.getType() == QuestionTypeEnum.SINGLE.getType() && checkChoice(answer.getAnswerSingle(), analysis)) {
                grade += scoreConfig.getSingle();
                correctCount++;

            }
            if (answer.getType() == QuestionTypeEnum.MULTIPLE.getType() && checkChoice(answer.getAnswerMultiple(), analysis)) {
                grade += scoreConfig.getMultiple();
                correctCount++;

            }
            // 判断题答案不为空并且判断正确
            if (answer.getType() == QuestionTypeEnum.JUDGE.getType()) {
                analysis.setCorrect(false);
                analysis.setAnswer(answer.getmJudgeAnswer() == null ? "空" : answer.getmJudgeAnswer() == 1 ? JudgeAnswerEnum.TRUE.getMsg() : JudgeAnswerEnum.FALSE.getMsg());
                if (answer.getmJudgeAnswer() != null && answer.getAnswerJudge().equals(answer.getmJudgeAnswer())) {
                    grade += scoreConfig.getJudge();
                    correctCount++;
                    analysis.setCorrect(true);
                }

            }
            // 答案在候选答案列表中
            if (answer.getType() == QuestionTypeEnum.FILL.getType() && checkFill(answer.getAnswerFill(), answer.getmFillAnswer(), analysis)) {
                grade += scoreConfig.getFill();
                correctCount++;

            }
            analysisList.add(analysis);
        }

        int questionCount = answerList.size();
        String correct = correctCount + "/" + questionCount;
        String score = grade + "/" + totalScore;
        // 更新记录状态
        record.setEndTime(new Date());
        record.setStatus(ExamRecordStatusEnum.FINISH.getCode());
        record.setScore(score);
        record.setCorrectCount(correct);
        record.setAnswerAnalysis(JSON.toJSONString(analysisList));
        record.setUpdatedTime(new Date());
        recordService.updateById(record);
    }

    /**
     * 检查单选题/多选题是否正确
     *
     * @return
     */
    public boolean checkChoice(String str, QuestionAnalysis analysis) {
        boolean res = true;
        List<UserChoiceAnswer> choiceAnswer = JSON.parseObject(str, new TypeReference<List<UserChoiceAnswer>>() {
        });
        StringBuffer correctAns = new StringBuffer();
        StringBuffer mAns = new StringBuffer();
        for (UserChoiceAnswer answer : choiceAnswer) {
            // 该答案是正确答案
            boolean choice = answer.getIsAnswer() == 1;
            if (choice) {
                // 将正确答案添加到解析中的正确答案
                correctAns.append(answer.getContent() + "\n");
            }
            if (answer.isChecked()) {
                // 将用户答案加入到解析中的用户答案
                mAns.append(answer.getContent() + "\n");
            }
            // 正确则 choice == answer.isChecked() 为true
            res = (choice == answer.isChecked()) & res;
        }
        // 更新该题的解析
        analysis.setCorrect(res);
        analysis.setAnswer(mAns.toString());
        return res;
    }

    /**
     * 检查填空题是否正确
     *
     * @return
     */
    public boolean checkFill(String str, String userAns, QuestionAnalysis analysis) {

        List<List<String>> rightFill = JSON.parseObject(str, new TypeReference<List<List<String>>>() {
        });
        // 转为小写
        rightFill = rightFill.stream()
                .map(innerList -> innerList.stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        List<String> userFill = JSON.parseArray(userAns, String.class);
        boolean res = true;
        for (int i = 0; i < userFill.size(); i++) {
            // 去除多余空格,并且将字母都转成小写
            String uAns = userFill.get(i).trim().toLowerCase();
            if (!rightFill.get(i).contains(uAns)) {
                res = false;
                break;
            }
        }
        analysis.setAnswer(userAns);
        analysis.setCorrect(res);
        return res;
    }

    /**
     * 获取考试分析
     *
     * @param recordId
     * @return
     */

    public ExamAnalysisRes analysis(Integer recordId) {
        ExamRecord record = recordService.getById(recordId);
        Exam exam = this.getById(record.getExamId());
        Paper paper = paperService.getById(record.getPaperId());
        // 根据paperId找到问题列表
        List<Integer> questionIds = paperQuestionRecordService.getBaseMapper().searchQIdByPId(exam.getPaperId());
        List<Question> questionList = questionService.getBaseMapper().selectBatchIds(questionIds);

        ExamAnalysisRes res = ExamAnalysisRes.builder()
                .questionList(questionList)
                .analysis(record.getAnswerAnalysis())
                .eachScore(paper.getEachScore())
                .build();
        return res;
    }

    /**
     * 返回recordId
     *
     * @param code
     * @param userId
     * @return
     */
    public void add(String code, int userId) {
        log.info("用户{}通过考试码{}添加考试...", userId, code);
        Exam exam = this.baseMapper.selectOne(new QueryWrapper<Exam>().eq("code", code));
        if (exam == null) {
            log.error("考试码{}无效", code);
            throw new BusinessException("找不到对应考试,请检查考试码");
        }

        ExamRecord record = ExamRecord.builder()
                .userId(userId)
                .examId(exam.getId())
                .paperId(exam.getPaperId())
                .status(ExamRecordStatusEnum.UNSTARTED.getCode())
                .build();

        try {
            recordService.save(record);
        } catch (Exception e) {
            log.error("保存记录失败");
        }
    }

    /**
     * 只考虑一场在一个班级只发布一次的情况
     *
     * @param userId
     * @return
     */
    public List<Paper> releaseList(Integer userId) {
        log.info("用户{}获取考试发布列表...", userId);
        List<Integer> paperIds = examReleaseRecordService.getBaseMapper().getReleasePaperIds(userId);
        log.info("用户发布的考卷有:{}", paperIds.toString());
        return paperService.getBaseMapper().selectBatchIds(paperIds);
    }
}