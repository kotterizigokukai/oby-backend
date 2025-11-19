package com.example.obybackend.domain.repository

import com.example.obybackend.domain.entity.ProfileEntity
import java.util.UUID

/**
 * プロフィールリポジトリ
 *
 * クリーンアーキテクチャに従い、シンプルなCRUD操作のみを提供
 * 更新ロジック（updatedAtの設定等）はユースケース層で行う
 */
interface ProfileRepository {
    /**
     * ユーザーIDでプロフィールを検索
     */
    fun findByUserId(userId: UUID): ProfileEntity?

    /**
     * プロフィールを保存または更新（UPSERT）
     *
     * エンティティに含まれるすべてのフィールド（updatedAt含む）をそのまま永続化する
     */
    fun save(profile: ProfileEntity): ProfileEntity
}
