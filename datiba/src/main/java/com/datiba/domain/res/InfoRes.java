package com.datiba.domain.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/13
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoRes {

    private String nickName;

    private String avatar;

    private Integer role;

    private String description;

    private String token;
}
