package com.example.obybackend.presentation.controller.profile

import com.example.obybackend.presentation.dto.profile.ProfileResponse
import com.example.obybackend.presentation.dto.profile.UpdateProfileRequest
import com.example.obybackend.presentation.util.OAuth2UserUtils
import com.example.obybackend.presentation.validation.ImageFileValidator
import com.example.obybackend.usecase.profile.DeleteAvatarUseCase
import com.example.obybackend.usecase.profile.GetMyProfileUseCase
import com.example.obybackend.usecase.profile.GetUserProfileUseCase
import com.example.obybackend.usecase.profile.UpdateMyProfileInput
import com.example.obybackend.usecase.profile.UpdateMyProfileUseCase
import com.example.obybackend.usecase.profile.UploadAvatarInput
import com.example.obybackend.usecase.profile.UploadAvatarUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

/**
 * プロフィール管理コントローラー
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Profile", description = "プロフィール管理API")
class ProfileController(
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateMyProfileUseCase: UpdateMyProfileUseCase,
    private val uploadAvatarUseCase: UploadAvatarUseCase,
    private val deleteAvatarUseCase: DeleteAvatarUseCase,
    private val imageFileValidator: ImageFileValidator,
    private val oauth2UserUtils: OAuth2UserUtils,
) {
    /**
     * U1: 自分のプロフィール取得
     */
    @GetMapping("/me")
    @Operation(operationId = "getMyProfile", summary = "自分のプロフィール取得")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "成功"),
            ApiResponse(responseCode = "404", description = "プロフィールが見つかりません", content = [Content()]),
        ],
    )
    fun getMyProfile(
        @AuthenticationPrincipal oauth2User: OAuth2User,
    ): ResponseEntity<ProfileResponse> {
        val userId = oauth2UserUtils.extractUserId(oauth2User)
        val output = getMyProfileUseCase.execute(userId)

        return ResponseEntity.ok(ProfileResponse.from(output))
    }

    /**
     * U2: 自分のプロフィール更新
     */
    @PutMapping("/me")
    @Operation(operationId = "updateMyProfile", summary = "自分のプロフィール更新")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "成功"),
            ApiResponse(responseCode = "400", description = "バリデーションエラー", content = [Content()]),
            ApiResponse(responseCode = "404", description = "プロフィールが見つかりません", content = [Content()]),
        ],
    )
    fun updateMyProfile(
        @AuthenticationPrincipal oauth2User: OAuth2User,
        @RequestBody request: UpdateProfileRequest,
    ): ResponseEntity<ProfileResponse> {
        val userId = oauth2UserUtils.extractUserId(oauth2User)
        val input =
            UpdateMyProfileInput(
                userId = userId,
                nickname = request.nickname,
                bio = request.bio,
            )

        val output = updateMyProfileUseCase.execute(input)

        return ResponseEntity.ok(ProfileResponse.from(output))
    }

    /**
     * U3: アイコン画像アップロード
     */
    @PostMapping("/me/avatar", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(operationId = "uploadAvatar", summary = "アイコン画像アップロード")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "成功"),
            ApiResponse(responseCode = "400", description = "画像が不正です", content = [Content()]),
            ApiResponse(responseCode = "404", description = "プロフィールが見つかりません", content = [Content()]),
        ],
    )
    fun uploadAvatar(
        @AuthenticationPrincipal oauth2User: OAuth2User,
        @RequestPart("avatar") file: MultipartFile,
    ): ResponseEntity<ProfileResponse> {
        val userId = oauth2UserUtils.extractUserId(oauth2User)

        // 早期バリデーション: MIME type, ファイルサイズをチェック
        imageFileValidator.validate(file)

        val input =
            UploadAvatarInput(
                userId = userId,
                imageData = file.bytes,
            )

        val output = uploadAvatarUseCase.execute(input)

        return ResponseEntity.ok(ProfileResponse.from(output))
    }

    /**
     * U4: アイコン削除
     */
    @DeleteMapping("/me/avatar")
    @Operation(operationId = "deleteAvatar", summary = "アイコン削除")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "成功"),
            ApiResponse(responseCode = "404", description = "プロフィールが見つかりません", content = [Content()]),
        ],
    )
    fun deleteAvatar(
        @AuthenticationPrincipal oauth2User: OAuth2User,
    ): ResponseEntity<ProfileResponse> {
        val userId = oauth2UserUtils.extractUserId(oauth2User)
        val output = deleteAvatarUseCase.execute(userId)

        return ResponseEntity.ok(ProfileResponse.from(output))
    }

    /**
     * U5: 他ユーザーのプロフィール取得
     */
    @GetMapping("/{userId}")
    @Operation(operationId = "getUserProfile", summary = "他ユーザーのプロフィール取得")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "成功"),
            ApiResponse(responseCode = "404", description = "プロフィールが見つかりません", content = [Content()]),
        ],
    )
    fun getUserProfile(
        @PathVariable userId: UUID,
    ): ResponseEntity<ProfileResponse> {
        val output = getUserProfileUseCase.execute(userId)

        return ResponseEntity.ok(ProfileResponse.from(output))
    }
}
