package com.example.obybackend.presentation.controller.roompost

import com.example.obybackend.domain.exception.RoomPostNotFoundException
import com.example.obybackend.domain.repository.UserRepository
import com.example.obybackend.presentation.dto.roompost.CreateRoomPostRequest
import com.example.obybackend.presentation.dto.roompost.RoomPostDetailResponse
import com.example.obybackend.presentation.dto.roompost.RoomPostListResponse
import com.example.obybackend.presentation.dto.roompost.RoomPostResponse
import com.example.obybackend.presentation.validation.ImageFileValidator
import com.example.obybackend.usecase.roompost.CreateRoomPostInput
import com.example.obybackend.usecase.roompost.CreateRoomPostUseCase
import com.example.obybackend.usecase.roompost.DeleteRoomPostInput
import com.example.obybackend.usecase.roompost.DeleteRoomPostUseCase
import com.example.obybackend.usecase.roompost.GetRoomPostDetailInput
import com.example.obybackend.usecase.roompost.GetRoomPostDetailUseCase
import com.example.obybackend.usecase.roompost.GetRoomPostsInput
import com.example.obybackend.usecase.roompost.GetRoomPostsUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

/**
 * 部屋投稿コントローラー
 */
@RestController
@RequestMapping("/api/v1/room-posts")
@Tag(name = "RoomPost", description = "部屋投稿API")
class RoomPostController(
    private val createRoomPostUseCase: CreateRoomPostUseCase,
    private val getRoomPostsUseCase: GetRoomPostsUseCase,
    private val getRoomPostDetailUseCase: GetRoomPostDetailUseCase,
    private val deleteRoomPostUseCase: DeleteRoomPostUseCase,
    private val imageFileValidator: ImageFileValidator,
    private val userRepository: UserRepository,
) {
    /**
     * P1: 部屋投稿作成
     */
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(
        operationId = "createRoomPost",
        summary = "部屋投稿作成",
        requestBody =
            RequestBody(
                content = [
                    Content(
                        mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                        schema = Schema(implementation = CreateRoomPostRequest::class),
                    ),
                ],
            ),
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "作成成功",
                content = [Content(schema = Schema(implementation = RoomPostResponse::class))],
            ),
            ApiResponse(responseCode = "400", description = "バリデーションエラー", content = [Content()]),
            ApiResponse(responseCode = "401", description = "未認証", content = [Content()]),
        ],
    )
    fun createRoomPost(
        @AuthenticationPrincipal oauth2User: OAuth2User,
        @RequestPart("image")
        @Parameter(description = "部屋の画像ファイル（最大5MB、JPEG/PNG/WebP）", required = true)
        image: MultipartFile,
        @RequestPart("title")
        @Parameter(description = "投稿タイトル（1-100文字）", required = true)
        title: String,
        @RequestPart("description", required = false)
        @Parameter(description = "投稿の説明文（最大1000文字）", required = false)
        description: String?,
    ): ResponseEntity<RoomPostResponse> {
        val userId = extractUserIdFromOAuth2User(oauth2User)

        // 画像バリデーション
        imageFileValidator.validate(image)

        val input =
            CreateRoomPostInput(
                userId = userId,
                imageData = image.bytes,
                title = title,
                description = description,
            )

        val output = createRoomPostUseCase.execute(input)

        return ResponseEntity.status(HttpStatus.CREATED).body(RoomPostResponse.from(output))
    }

    /**
     * P2: 部屋投稿一覧取得（カーソルベースページネーション）
     */
    @GetMapping
    @Operation(operationId = "getRoomPosts", summary = "部屋投稿一覧取得（カーソルベースページネーション）")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "成功",
                content = [Content(schema = Schema(implementation = RoomPostListResponse::class))],
            ),
        ],
    )
    fun getRoomPosts(
        @RequestParam("cursor", required = false)
        @Parameter(description = "ページネーション用カーソル（部屋投稿ID）", required = false)
        cursor: String?,
        @RequestParam("limit", required = false)
        @Parameter(description = "取得件数（デフォルト10件、最大50件）", required = false)
        limit: Int?,
    ): ResponseEntity<RoomPostListResponse> {
        val input =
            GetRoomPostsInput(
                cursor = cursor?.let { UUID.fromString(it) },
                limit = limit,
            )

        val output = getRoomPostsUseCase.execute(input)

        return ResponseEntity.ok(RoomPostListResponse.from(output))
    }

    /**
     * P3: 部屋投稿詳細取得
     */
    @GetMapping("/{roomPostId}")
    @Operation(operationId = "getRoomPostDetail", summary = "部屋投稿詳細取得")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "成功",
                content = [Content(schema = Schema(implementation = RoomPostDetailResponse::class))],
            ),
            ApiResponse(responseCode = "404", description = "部屋投稿が見つからない", content = [Content()]),
        ],
    )
    fun getRoomPostDetail(
        @PathVariable
        @Parameter(description = "部屋投稿ID", required = true)
        roomPostId: UUID,
    ): ResponseEntity<RoomPostDetailResponse> {
        val input = GetRoomPostDetailInput(roomPostId)
        val output = getRoomPostDetailUseCase.execute(input)

        return ResponseEntity.ok(RoomPostDetailResponse.from(output))
    }

    /**
     * P4: 部屋投稿削除
     */
    @DeleteMapping("/{roomPostId}")
    @Operation(operationId = "deleteRoomPost", summary = "部屋投稿削除（自分の投稿のみ）")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "削除成功", content = [Content()]),
            ApiResponse(responseCode = "401", description = "未認証", content = [Content()]),
            ApiResponse(responseCode = "403", description = "他人の部屋投稿を削除しようとした", content = [Content()]),
            ApiResponse(responseCode = "404", description = "部屋投稿が見つからない", content = [Content()]),
        ],
    )
    fun deleteRoomPost(
        @AuthenticationPrincipal oauth2User: OAuth2User,
        @PathVariable
        @Parameter(description = "部屋投稿ID", required = true)
        roomPostId: UUID,
    ): ResponseEntity<Void> {
        val userId = extractUserIdFromOAuth2User(oauth2User)

        val input =
            DeleteRoomPostInput(
                roomPostId = roomPostId,
                requestUserId = userId,
            )

        try {
            deleteRoomPostUseCase.execute(input)
            return ResponseEntity.noContent().build()
        } catch (e: RoomPostNotFoundException) {
            // 所有者チェックに失敗した場合は403を返す
            // （実際にはNotFoundだが、所有者でない場合は詳細を隠す）
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }
    }

    /**
     * OAuth2Userから内部ユーザーIDを取得
     */
    private fun extractUserIdFromOAuth2User(oauth2User: OAuth2User): UUID {
        val googleSub = oauth2User.getAttribute<String>("sub") ?: throw IllegalStateException("Google sub not found")
        return userRepository.findByGoogleSub(googleSub)?.id ?: throw IllegalStateException("User not found")
    }
}
