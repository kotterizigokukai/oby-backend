package com.example.obybackend.presentation.exception

import com.example.obybackend.domain.exception.DomainException
import com.example.obybackend.domain.exception.InfrastructureException
import com.example.obybackend.domain.exception.ProfileNotFoundException
import com.example.obybackend.domain.exception.ValidationException
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.multipart.MaxUploadSizeExceededException
import java.time.Instant

/**
 * グローバル例外ハンドラー
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    /**
     * バリデーション例外のハンドリング
     */
    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(ex: ValidationException): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                error = "Validation Error",
                message = ex.message ?: "Invalid input",
                timestamp = Instant.now(),
            )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    /**
     * プロフィール未発見例外のハンドリング
     */
    @ExceptionHandler(ProfileNotFoundException::class)
    fun handleProfileNotFoundException(ex: ProfileNotFoundException): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                status = HttpStatus.NOT_FOUND.value(),
                error = "Profile Not Found",
                message = ex.message ?: "Profile not found",
                timestamp = Instant.now(),
            )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    /**
     * インフラストラクチャ例外のハンドリング (ストレージエラーなど)
     */
    @ExceptionHandler(InfrastructureException::class)
    fun handleInfrastructureException(ex: InfrastructureException): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                error = "Infrastructure Error",
                message = ex.message ?: "Storage or infrastructure service error occurred",
                timestamp = Instant.now(),
            )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }

    /**
     * その他のドメイン例外のハンドリング
     */
    @ExceptionHandler(DomainException::class)
    fun handleDomainException(ex: DomainException): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                error = "Domain Error",
                message = ex.message ?: "An unexpected error occurred",
                timestamp = Instant.now(),
            )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }

    /**
     * ファイルサイズ超過例外のハンドリング
     */
    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxUploadSizeExceededException(ex: MaxUploadSizeExceededException): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                error = "File Size Exceeded",
                message = "File size exceeds maximum allowed size (5MB)",
                timestamp = Instant.now(),
            )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    /**
     * 一般的な例外のハンドリング
     */
    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse =
            ErrorResponse(
                status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                error = "Internal Server Error",
                message = ex.message ?: "An unexpected error occurred",
                timestamp = Instant.now(),
            )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}

/**
 * エラーレスポンス
 */
@Schema(description = "エラーレスポンス")
data class ErrorResponse(
    @Schema(description = "HTTPステータスコード", example = "400")
    val status: Int,
    @Schema(description = "エラー種別", example = "Validation Error")
    val error: String,
    @Schema(description = "エラーメッセージ", example = "Nickname must be between 1 and 50 characters")
    val message: String,
    @Schema(description = "エラー発生時刻 (UTC)", example = "2025-11-07T12:34:56Z")
    val timestamp: Instant,
)
