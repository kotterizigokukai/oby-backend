package com.example.obybackend.presentation.validation

import com.example.obybackend.domain.exception.InvalidImageException
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

/**
 * 画像ファイルバリデーター
 *
 * Controller層での早期バリデーションを実施し、
 * 不正なファイルを早期に検出してリソースの無駄を防ぐ
 */
@Component
class ImageFileValidator {
    companion object {
        // 最大ファイルサイズ: 5MB
        private const val MAX_FILE_SIZE = 5 * 1024 * 1024L

        // 許可するMIMEタイプ
        private val ALLOWED_MIME_TYPES =
            setOf(
                "image/jpeg",
                "image/png",
                "image/webp",
            )

        // 許可する拡張子
        private val ALLOWED_EXTENSIONS =
            setOf(
                "jpg",
                "jpeg",
                "png",
                "webp",
            )
    }

    /**
     * 画像ファイルをバリデーション
     *
     * @param file アップロードされたファイル
     * @throws InvalidImageException バリデーションエラー
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

        // 4. 拡張子チェック
        val extension = extractExtension(file.originalFilename)
        if (extension == null || extension !in ALLOWED_EXTENSIONS) {
            throw InvalidImageException(
                "Invalid file extension. Allowed extensions: ${ALLOWED_EXTENSIONS.joinToString()}, actual: $extension",
            )
        }
    }

    /**
     * ファイル名から拡張子を抽出（小文字に変換）
     */
    private fun extractExtension(filename: String?): String? {
        if (filename.isNullOrBlank()) return null
        val lastDotIndex = filename.lastIndexOf('.')
        if (lastDotIndex == -1 || lastDotIndex == filename.length - 1) return null
        return filename.substring(lastDotIndex + 1).lowercase()
    }
}
