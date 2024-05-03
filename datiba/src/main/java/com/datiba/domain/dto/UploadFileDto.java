package com.datiba.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Description:
 * Author:WuXiaotong
 * Date: 2024/3/1
 */
@Data
public class UploadFileDto {

    MultipartFile file;

    Integer bankId;

    String name;

    String description;

    Integer isPrivate;

    String eachScore;
}
