package com.hifive.bururung.global.common.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileDTO {
    private String storeFileName; // S3 저장 파일이름
    private String storeFullUrl; // S3 URL
}
