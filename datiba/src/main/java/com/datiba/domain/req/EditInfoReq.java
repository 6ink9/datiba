package com.datiba.domain.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotEmpty;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/21
 */
@Data
public class EditInfoReq {

    @Length(max = 10)
    String nickName;
    @Length(max = 20)
    String motto;
    String avatar;
}
