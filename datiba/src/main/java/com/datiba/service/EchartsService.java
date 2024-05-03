package com.datiba.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.datiba.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/15
 */
@Service
@Slf4j
public class EchartsService {
    @Autowired
    UserService userService;

    @Autowired
    ExamReleaseRecordService releaseRecordService;

    @Autowired
    UserClassRecordService userClassRecordService;

    @Autowired
    ExamRecordService examRecordService;

    @Autowired
    ExamService examService;

    @Autowired
    ClazzService clazzService;

    @Autowired
    PaperService paperService;

    public static final String[] intervalLabels = {"0%-20%", "20%-40%", "40%-60%", "60%-80%", "80%-100%"};

    /**
     * 获取可视化数据
     *
     * @param userId
     */
    public ExamVisualAnalysis getVisualData(Integer userId, Integer paperId) {
        log.info("获取试卷{}考试结果可视化所需的数据...", paperId);
        // 该老师在该试卷下创建的所有班级考试记录
        List<ExamReleaseRecord> releaseRecordList = releaseRecordService.list(new QueryWrapper<ExamReleaseRecord>().eq("user_id", userId).eq("paper_id", paperId));
        ExamVisualAnalysis examVisualAnalysis = new ExamVisualAnalysis();
        examVisualAnalysis.setPaperId(paperId);
        Paper paper = paperService.getById(paperId);
        examVisualAnalysis.setPaperName(paper.getName());
        List<ClassScore> classScores = new ArrayList<>();
        for (ExamReleaseRecord releaseRecord : releaseRecordList) {
            ClassScore classScore = getClassScoreList(releaseRecord.getClassId(), releaseRecord.getExamId(), userId);
            if (classScore != null) {
                classScores.add(classScore);
            }
        }

        examVisualAnalysis.setClassScoresList(classScores);
        return examVisualAnalysis;

    }

    private ClassScore getClassScoreList(int classId, int examId, int teacherId) {
        // 筛选出班级的学生id列表
        List<Integer> studentIds = userClassRecordService.getBaseMapper().selectStudIdsByClazz(classId, teacherId);
        if (studentIds.size() <= 0) {
            return null;
        }
        List<ExamRecord> records = examRecordService.getBaseMapper().selectClazzRecord(studentIds, examId);
        Clazz clazz = clazzService.getById(classId);
        double averageScore = records.stream().mapToDouble(ExamRecord::getObtainedScore).average().orElse(0);
        Map<Object, Long> accuracyCounts = records.stream().collect(Collectors.groupingBy(
                record -> {
                    double accuracy = record.getAccuracy();
                    if (accuracy < 20) {
                        return "0%-20%";
                    } else if (accuracy < 40) {
                        return "20%-40%";
                    } else if (accuracy < 60) {
                        return "40%-60%";
                    } else if (accuracy < 80) {
                        return "60%-80%";
                    } else {
                        return "80%-100%";
                    }
                },
                Collectors.counting()
        ));
        List<Long> countsArray = new ArrayList<>();

        for (String key : intervalLabels) {
            if (accuracyCounts.containsKey(key)) {
                countsArray.add(accuracyCounts.get(key));
            } else {
                countsArray.add(0L);
            }
        }

        ClassScore classScore = ClassScore.builder()
                .scoreRange(countsArray)
                .averageScore(Math.round(averageScore * 100.0) / 100.0)
                .clazzId(classId)
                .clazzName(clazz.getName())
                .build();
        return classScore;
    }

    /**
     * 学生查看某课程下所有考试的分数
     *
     * @param userId
     * @param classId
     * @return
     */
    public ExamVisualAnalysisStud getStudVisualData(Integer userId, Integer classId) {
        // 查找该课程下的所有考试记录
        List<ExamReleaseRecord> examReleaseRecords = releaseRecordService.list(new QueryWrapper<ExamReleaseRecord>().eq("class_id", classId));
        List<String> examNameList = new ArrayList<>();
        List<Double> accuracyList = new ArrayList<>();
        for (ExamReleaseRecord releaseRecord : examReleaseRecords) {
            ExamRecord record = examRecordService.getBaseMapper().getUserExamRecord(releaseRecord.getExamId(), userId);
            String examName = examService.getById(record.getExamId()).getName();
            double accuracy = record.getAccuracy();
            examNameList.add(examName);
            accuracyList.add(accuracy);
        }
        ExamVisualAnalysisStud examVisualAnalysis = ExamVisualAnalysisStud.builder()
                .examName(examNameList)
                .accuracy(accuracyList)
                .build();

        return examVisualAnalysis;
    }
}
