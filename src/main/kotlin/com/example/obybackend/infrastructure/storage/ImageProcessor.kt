package com.example.obybackend.infrastructure.storage

import com.example.obybackend.domain.exception.InvalidImageException
import org.springframework.stereotype.Component
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

/**
 * 画像処理サービス
 *
 * フロントエンドで正方形にクロップ済みの画像を受け取り、
 * 512x512pxにリサイズしてJPEGに変換する
 */
@Component
class ImageProcessor {
    companion object {
        private const val TARGET_SIZE = 512
        private const val MAX_DIMENSION = 4096
    }

    /**
     * 画像を512x512にリサイズしてJPEGに変換
     *
     * 前提: フロントエンドで正方形にクロップ済み
     *
     * @param imageData 元画像のバイナリデータ
     * @return リサイズ後のJPEGバイナリデータ
     * @throws ValidationException 画像が不正な場合
     */
    fun processImage(imageData: ByteArray): ByteArray {
        // 画像を読み込み
        val originalImage =
            ByteArrayInputStream(imageData).use { input ->
                ImageIO.read(input) ?: throw InvalidImageException("Invalid image format")
            }

        // バリデーション
        validateImageSize(originalImage)
        validateAspectRatio(originalImage)

        // リサイズのみ実行
        val resizedImage = resize(originalImage, TARGET_SIZE)

        // JPEGに変換
        return convertToJpeg(resizedImage)
    }

    /**
     * 画像サイズをバリデーション
     */
    private fun validateImageSize(image: BufferedImage) {
        if (image.width > MAX_DIMENSION || image.height > MAX_DIMENSION) {
            throw InvalidImageException(
                "Image dimensions too large (max ${MAX_DIMENSION}x${MAX_DIMENSION}px)",
            )
        }

        if (image.width < 1 || image.height < 1) {
            throw InvalidImageException("Image dimensions too small")
        }
    }

    /**
     * 画像が正方形かどうかをバリデーション
     *
     * 厳格に幅と高さが一致していることをチェック
     */
    private fun validateAspectRatio(image: BufferedImage) {
        if (image.width != image.height) {
            throw InvalidImageException(
                "Image must be square (current: ${image.width}x${image.height}). " +
                    "Please crop the image to a square on the frontend.",
            )
        }
    }

    /**
     * 画像を指定サイズにリサイズ
     */
    private fun resize(
        image: BufferedImage,
        targetSize: Int,
    ): BufferedImage {
        // 既に目標サイズの場合はそのまま返す
        if (image.width == targetSize && image.height == targetSize) {
            return image
        }

        // リサイズ
        val scaledImage =
            image.getScaledInstance(
                targetSize,
                targetSize,
                Image.SCALE_SMOOTH,
            )

        // BufferedImageに変換
        val resizedImage = BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_RGB)
        val graphics = resizedImage.createGraphics()
        graphics.drawImage(scaledImage, 0, 0, null)
        graphics.dispose()

        return resizedImage
    }

    /**
     * BufferedImageをJPEGバイナリに変換
     */
    private fun convertToJpeg(image: BufferedImage): ByteArray {
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(image, "jpg", outputStream)
        return outputStream.toByteArray()
    }
}
