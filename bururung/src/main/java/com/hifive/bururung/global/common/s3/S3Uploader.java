package com.hifive.bururung.global.common.s3;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.hifive.bururung.global.exception.CustomException;
import com.hifive.bururung.global.exception.errorcode.FileErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Uploader {
	
	private final AmazonS3 amazonS3Client;
	
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    
    public UploadFileDTO uploadFile(MultipartFile multipartFile, String subPath) throws IOException {
        if (isEmptyFile(multipartFile)) {
        	throw new CustomException(FileErrorCode.FILE_EMPTY);
        }
        
        String originalFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFileName);
        String storeFileUrl = sendAwsS3(bucket, subPath + storeFileName, multipartFile);
        
        log.info("S3에 파일 전송 완료 originalFileName = {},fileSubPath = {}, storeUrl={}", originalFileName, subPath, storeFileUrl);
        return new UploadFileDTO(storeFileName, storeFileUrl);
    }
    
    private String sendAwsS3(String bucketName, String filePath, MultipartFile uploadFile) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(uploadFile.getContentType());
        objectMetadata.setContentLength(uploadFile.getSize());
        
        amazonS3Client.putObject(new PutObjectRequest(bucketName, filePath, uploadFile.getInputStream(), objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        log.info("✅ S3 업로드 성공 - URL: {}", amazonS3Client.getUrl(bucketName, filePath).toString());
        return amazonS3Client.getUrl(bucketName, filePath).toString();
    }
    
    public String deleteFile(String filePath) {
    	if(!StringUtils.hasText(filePath)) {
    		throw new CustomException(FileErrorCode.NO_FILE_NAME);
    	}
    	if(!isExistObjectInS3(bucket, filePath)) {
    		throw new CustomException(FileErrorCode.S3_FILE_NOT_FOUND);
    	}
    	
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, filePath));
        
        log.info("S3 파일 삭제 완료 deletedFilePath = {}", filePath);
        return filePath;
    }

    private boolean isExistObjectInS3(String bucket, String filePath) {
    	return amazonS3Client.doesObjectExist(bucket, filePath);
	}

	private boolean isEmptyFile(MultipartFile multipartFile) {
        return multipartFile == null || multipartFile.isEmpty();
    }
    
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        return UUID.randomUUID().toString() + "." + ext;
    }
    
    // 확장자 추출
    private static String extractExt(String originalFilename) {
        int position = originalFilename.lastIndexOf(".");
        return originalFilename.substring(position + 1);
    }
}
