package com.datiba.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @TableName paper_question_record
 */
@TableName(value ="paper_question_record")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaperQuestionRecord implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 试卷Id
     */
    private Integer paperId;

    /**
     * 问题Id
     */
    private Integer questionId;

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

    public PaperQuestionRecord(int paperId, int questionId){
        this.paperId = paperId;
        this.questionId = questionId;
    }
}