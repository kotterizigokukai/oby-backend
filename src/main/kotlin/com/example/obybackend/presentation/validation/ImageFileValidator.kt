package com.example.obybackend.presentation.validation

import com.example.obybackend.domain.exception.InvalidImageException
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

/**
 * 画像ファイルバリデーター
 *
 * Controller層で早期バリデーションを行い、不正なファイルを即座に拒否する。
 * ImageProcessor（Infrastructure層）で実際の画像形式も検証するため、多層防御となる。
 */
@Component
class ImageFileValidator {
    companion object {
        // 最大ファイルサイズ: 5MB
        private const val MAX_FILE_SIZE = 5 * 1024 * 1024L

        // 許可するMIMEタイプ（JPEG, PNG, WebP）
        private val ALLOWED_MIME_TYPES =
            setOf(
                "image/jpeg",
                "image/png",
                "image/webp",
            )
    }

    /**
     * 画像ファイルをバリデーション
     *
     * @param file アップロードされたファイル
     * @throws InvalidImageException ファイルが空、サイズ超過、または不正なMIMEタイプの場合
     */
    fun validate(file: MultipartFile) {
        // 1. ファイルが空でないかチェック
        if (file.isEmpty) {
            throw InvalidImageException("Uploaded file is empty")
        }

        // 2. ファイルサイズチェック
        if (file.size > MAX_FILE_SIZE) {
            throw InvalidImageException(
                "File size exceeds maximum allowed size (max 5MB, actual: ${file.size / 1024 / 1024}MB)",
            )
        }

        // 3. MIMEタイプチェック
        val mimeType = file.contentType
        if (mimeType == null || mimeType !in ALLOWED_MIME_TYPES) {
            throw InvalidImageException(
                "Invalid file type. Allowed types: ${ALLOWED_MIME_TYPES.joinToString()}, actual: $mimeType",
            )
        }
    }
}
