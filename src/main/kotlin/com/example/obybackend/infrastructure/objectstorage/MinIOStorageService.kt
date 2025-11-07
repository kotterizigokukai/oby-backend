package com.example.obybackend.infrastructure.objectstorage

import com.example.obybackend.domain.repository.StorageService
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import io.minio.http.Method
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream

/**
 * MinIO ストレージサービス実装
 */
@Service
@ConditionalOnProperty(name = ["storage.type"], havingValue = "minio")
class MinIOStorageService(
    @Value("\${storage.minio.endpoint}") private val endpoint: String,
    @Value("\${storage.minio.access-key}") private val accessKey: String,
    @Value("\${storage.minio.secret-key}") private val secretKey: String,
    @Value("\${storage.minio.bucket}") private val bucket: String,
) : StorageService {
    private val minioClient: MinioClient =
        MinioClient.builder()
            .endpoint(endpoint)
            .credentials(accessKey, secretKey)
            .build()

    override fun upload(
        key: String,
        data: ByteArray,
        contentType: String,
    ): String {
        val inputStream = ByteArrayInputStream(data)

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucket)
                .`object`(key)
                .stream(inputStream, data.size.toLong(), -1)
                .contentType(contentType)
                .build(),
        )

        return getPublicUrl(key)
    }

    override fun delete(key: String) {
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(bucket)
                .`object`(key)
                .build(),
        )
    }

    override fun getPublicUrl(key: String): String {
        // MinIOの場合は presigned URL を返す (開発環境用)
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucket)
                .`object`(key)
                .expiry(60 * 60 * 24 * 7) // 7日間有効
                .build(),
        )
    }
}
