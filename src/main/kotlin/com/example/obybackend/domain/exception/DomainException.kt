package com.example.obybackend.domain.exception

// 基底例外(sealed classで型安全性を確保)
sealed class DomainException(message: String) : RuntimeException(message)

// 予期しない致命的エラー(プログラムバグなど)
class UnexpectedDomainException(message: String) : DomainException(message)

// 値オブジェクトのバリデーションエラー
sealed class ValidationException(message: String) : DomainException(message)

class InvalidNicknameException(message: String) : ValidationException(message)

class InvalidBioException(message: String) : ValidationException(message)

class InvalidAvatarUrlException(message: String) : ValidationException(message)

class InvalidImageException(message: String) : ValidationException(message)

// ビジネスルール違反
sealed class BusinessRuleException(message: String) : DomainException(message)

// リソースが見つからない
class ProfileNotFoundException(userId: String) : DomainException("Profile not found for user: $userId")

// インフラストラクチャ層の例外
sealed class InfrastructureException(message: String, cause: Throwable? = null) : DomainException(message) {
    init {
        if (cause != null) initCause(cause)
    }
}

class StorageException(message: String, cause: Throwable? = null) : InfrastructureException(message, cause)
