package com.datiba.util;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/4/2
 */
public class RegexUtils {
    /**
     * 是否是无效手机格式
     * @param phone 要校验的手机号
     * @return true:符合，false：不符合
     */
    public static boolean isPhoneNumberValid(String phone){
        return mismatch(phone, RegexPatterns.PHONE_REGEX);
    }

    // 校验是否符合正则格式
    public static boolean mismatch(String str, String regex){
        if (StringUtils.isBlank(str)) {
            return false;
        }
        return str.matches(regex);
    }
}
