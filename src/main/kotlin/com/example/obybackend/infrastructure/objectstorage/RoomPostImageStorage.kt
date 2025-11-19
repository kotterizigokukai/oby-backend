package com.example.obybackend.infrastructure.objectstorage

import com.example.obybackend.domain.exception.StorageException
import com.example.obybackend.domain.repository.StorageService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * 部屋投稿画像のストレージ操作
 *
 * パス形式: room-posts/{userId}/{uuid}.{ext}
 */
@Component
class RoomPostImageStorage(
    private val storageService: StorageService,
    private val imageProcessor: ImageProcessor,
) {
    private val logger = LoggerFactory.getLogger(RoomPostImageStorage::class.java)

    /**
     * 部屋投稿画像をアップロード
     *
     * @param userId 投稿者のユーザーID
     * @param imageData 画像データ
     * @return 画像のURL
     */
    fun uploadRoomPostImage(
        userId: UUID,
        imageData: ByteArray,
    ): String {
        // 画像処理 (JPEG変換、最適化)
        val processedImage = imageProcessor.processImage(imageData)

        // ストレージキー生成
        val key = "room-posts/$userId/${UUID.randomUUID()}.jpg"

        return try {
            val url = storageService.upload(key, processedImage, "image/jpeg")
            logger.info("Successfully uploaded room post image: key=$key, userId=$userId")
            url
        } catch (e: Exception) {
            logger.error("Failed to upload room post image: key=$key, userId=$userId", e)
            throw StorageException("Failed to upload room post image", e)
        }
    }

    /**
     * 部屋投稿画像を削除
     *
     * @param imageUrl 画像のURL
     */
    fun deleteRoomPostImage(imageUrl: String) {
        try {
            val key = extractStorageKey(imageUrl)
            storageService.delete(key)
            logger.info("Successfully deleted room post image: key=$key")
        } catch (e: Exception) {
            logger.error("Failed to delete room post image: url=$imageUrl", e)
            throw StorageException("Failed to delete room post image", e)
        }
    }

    /**
     * URLからストレージキーを抽出
     *
     * 例: "http://localhost:9000/bucket/room-posts/uuid/uuid.jpg" → "room-posts/uuid/uuid.jpg"
     */
    private fun extractStorageKey(url: String): String {
        // URLから最後の"/"以降を取得する簡易実装
        // 実際の環境に応じて調整が必要
        val parts = url.split("/")
        val bucketIndex = parts.indexOfFirst { it == "bucket" || it.contains("minio") || it.contains("s3") }
        return if (bucketIndex != -1 && bucketIndex + 1 < parts.size) {
            parts.subList(bucketIndex + 1, parts.size).joinToString("/")
        } else {
            // フォールバック: room-posts で始まる部分を探す
            val roomPostsIndex = parts.indexOfFirst { it == "room-posts" }
            if (roomPostsIndex != -1) {
                parts.subList(roomPostsIndex, parts.size).joinToString("/")
            } else {
                throw IllegalArgumentException("Invalid storage URL format: $url")
            }
        }
    }
}
