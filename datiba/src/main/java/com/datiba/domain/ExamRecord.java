package com.datiba.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * 考试记录表
 * @TableName paper_record
 */
@TableName(value ="exam_record")
@Data
@Builder
public class ExamRecord implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private Integer paperId;

    /**
     * 
     */
    private Integer userId;


    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 
     */
    private String score;

    /**
     * 答对题数 -/-
     */
    private String correctCount;

    /**
     * 试卷状态
     */
    private Integer status;

    /**
     * 
     */
    private Date createdTime;

    /**
     * 
     */
    private Date updatedTime;

    /**
     * 
     */
    private String answerAnalysis;

    /**
     *
     */
    private Integer examId;

    /**
     *  考试的截至时间，如果没有则为空
     */
    private Date ddl;
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    // 解析得分/总分字符串，返回正确率（0-1之间的小数）

    public double getAccuracy() {
        if (score == null || score.equals("0")){
            return 0d;
        }

        String[] parts = score.split("/");
        int obtainedScore = Integer.parseInt(parts[0]);
        int totalScore = Integer.parseInt(parts[1]);
        double acc = (double) obtainedScore / totalScore * 100.00;
        return Math.round(acc * 100.0) / 100.0;
    }

    public int getObtainedScore(){
        if (score == null || score.equals("0")){
            return 0;
        }

        String[] parts = score.split("/");
        return Integer.parseInt(parts[0]);
    }
}