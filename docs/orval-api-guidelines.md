# Orval 連携のための API 設計ガイドライン

- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Controller での必須アノテーション

### @Tag（Controller クラスレベル）

```kotlin
@RestController
@RequestMapping("/messages")
@Tag(name = "Messages", description = "メッセージ管理API")
class MessageController { ... }
```

- orval の `mode: 'tags'` で適切にファイル分割される
- `name` は orval が生成するファイル名の基準になる

### @Operation（メソッドレベル）

```kotlin
@GetMapping("/{id}")
@Operation(operationId = "getMessageById", summary = "メッセージ取得")
fun getMessage(@PathVariable id: UUID): ResponseEntity<Message>
```

- `operationId` を**必ず明示的に指定**（orval が生成する関数名になる）
- キャメルケースで記述（例: `getMessageById`, `createMessage`, `listMessages`）
- プロジェクト全体でユニークな名前を使用

### @Schema（DTO クラス/プロパティレベル）

```kotlin
@Schema(description = "メッセージエンティティ")
data class Message(
    @Schema(description = "メッセージ本文", required = true)
    val text: String,

    @Schema(description = "メッセージID")
    val id: UUID? = null
)
```

- `required` でプロパティが必須かを明示（orval の型で `string` か `string | undefined` かが決まる）
