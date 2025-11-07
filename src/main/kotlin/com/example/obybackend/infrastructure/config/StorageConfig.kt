package com.example.obybackend.infrastructure.config

import org.springframework.context.annotation.Configuration

/**
 * ストレージ設定
 *
 * MinIOStorageServiceとS3StorageServiceは
 * @ConditionalOnPropertyによって自動的に切り替わる
 */
@Configuration
class StorageConfig
