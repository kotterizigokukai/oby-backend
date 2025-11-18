package com.example.obybackend.presentation.validation

import com.example.obybackend.domain.exception.InvalidImageException
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

/**
 * 画像ファイルバリデーター
 *
 * Controller層での早期バリデーションを実施し、
 * 不正なファイルを早期に検出してリソースの無駄を防ぐ
 *
 * フロントエンドでは画像のトリミング時にすべてJPEG形式に変換しているため、
 * 実際にアップロードされるファイルはほぼimage/jpegとなる。
 * MIMEタイプでの検証は拡張子よりも信頼性が高く（拡張子は簡単に偽装可能）、
 * さらにImageProcessorで実際の画像形式も検証しているため、セキュリティは十分に保たれる。
 */
@Component
class ImageFileValidator {
    companion object {
        // 最大ファイルサイズ: 5MB
        private const val MAX_FILE_SIZE = 5 * 1024 * 1024L

        // 許可するMIMEタイプ
        // フロントエンドがトリミング後にJPEGに変換するが、
        // トリミング前の直接アップロードにも対応するため複数形式を許可
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
     * MIMEタイプのみで検証を行う。拡張子チェックは以下の理由により不要:
     * 1. MIMEタイプの方が信頼性が高い（拡張子は簡単に偽装可能）
     * 2. ImageProcessorで実際の画像形式も検証している
     * 3. フロントエンドが既にJPEGに統一している
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
    }
}
