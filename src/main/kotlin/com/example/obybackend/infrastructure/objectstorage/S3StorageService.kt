package com.example.obybackend.infrastructure.objectstorage

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.example.obybackend.domain.repository.StorageService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream

/**
 * AWS S3 ストレージサービス実装
 */
@Service
@ConditionalOnProperty(name = ["storage.type"], havingValue = "s3")
class S3StorageService(
    @Value("\${storage.s3.region}") private val region: String,
    @Value("\${storage.s3.bucket}") private val bucket: String,
    @Value("\${storage.s3.access-key:}") private val accessKey: String?,
    @Value("\${storage.s3.secret-key:}") private val secretKey: String?,
) : StorageService {
    private val s3Client: AmazonS3 =
        if (!accessKey.isNullOrBlank() && !secretKey.isNullOrBlank()) {
            // 明示的な認証情報を使用
            val credentials = BasicAWSCredentials(accessKey, secretKey)
            AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(AWSStaticCredentialsProvider(credentials))
                .build()
        } else {
            // デフォルト認証(EC2 IAMロール)
            AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .build()
        }

    override fun upload(
        key: String,
        data: ByteArray,
        contentType: String,
    ): String {
        val metadata =
            ObjectMetadata().apply {
                this.contentType = contentType
                this.contentLength = data.size.toLong()
            }

        val inputStream = ByteArrayInputStream(data)

        val putRequest =
            PutObjectRequest(bucket, key, inputStream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead)

        s3Client.putObject(putRequest)

        return getPublicUrl(key)
    }

    override fun delete(key: String) {
        s3Client.deleteObject(DeleteObjectRequest(bucket, key))
    }

    override fun getPublicUrl(key: String): String {
        return s3Client.getUrl(bucket, key).toString()
    }
}
