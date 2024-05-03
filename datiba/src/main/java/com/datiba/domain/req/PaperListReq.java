package com.datiba.domain.req;


import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * paper/list请求参数
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/7
 */
@Data
public class PaperListReq {

    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小为1" )
    @Max(value = 100, message = "页码最大为100")
    private Integer pageNum;

    private Integer bankId;

    private String order;

    private String keyword;
}
