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
 * 
 * @TableName exam_release_record
 */
@TableName(value ="exam_release_record")
@Data
@Builder
public class ExamReleaseRecord implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 考试id
     */
    private Integer examId;

    /**
     * 班级id
     */
    private Integer classId;

    /**
     * 教师（用户）id
     */
    private Integer userId;

    /**
     * 试卷id
     */
    private Integer paperId;

    /**
     * 
     */
    private Date createdTime;

    /**
     * 
     */
    private Date updatedTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}