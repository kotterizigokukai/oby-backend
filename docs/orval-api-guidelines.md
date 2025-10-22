# Orval連携のためのAPI設計ガイドライン

このドキュメントは、フロントエンドのorvalと連携するために、バックエンドのAPI実装で必要な設定とベストプラクティスをまとめたものです。

## 必須設定

### 1. SpringDoc依存関係

`build.gradle.kts`に以下の依存関係を追加してください：

```kotlin
implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")
```

### 2. application.properties設定

```properties
# OpenAPI / Swagger Configuration
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
```

## Controllerでの必須アノテーション

orvalが正しく型を生成するために、以下のアノテーションを**必ず**付与してください。

### @Tag（Controller クラスレベル）

```kotlin
@RestController
@RequestMapping("/messages")
@Tag(name = "Messages", description = "メッセージ管理API")
class MessageController { ... }
```

- **目的**: APIをグループ化し、orvalの`mode: 'tags'`で適切にファイル分割される
- **必須**: はい
- **name**: orvalが生成するファイル名の基準になるため、明確で一貫性のある名前を使用

### @Operation（メソッドレベル）

```kotlin
@GetMapping("/{id}")
@Operation(operationId = "getMessageById", summary = "メッセージ取得")
fun getMessage(@PathVariable id: UUID): ResponseEntity<Message>
```

- **目的**: orvalが生成する関数名を制御
- **必須**: はい
- **operationId**: orvalが生成する関数名になるため、**必ず明示的に指定すること**
  - 未指定の場合、自動生成された名前になり予測不可能
  - キャメルケースで記述（例: `getMessageById`, `createMessage`, `listMessages`）
  - プロジェクト全体でユニークな名前を使用
- **summary**: 任意だが、API一覧での可読性向上のため推奨

## DTOでの必須アノテーション

### @Schema（クラス/プロパティレベル）

```kotlin
@Schema(description = "メッセージエンティティ")
data class Message(
    @Schema(description = "メッセージ本文", example = "Hello, World!", required = true)
    val text: String,

    @Schema(description = "メッセージID（自動生成）", example = "123e4567-e89b-12d3-a456-426614174000")
    val id: UUID? = null
)
```

- **目的**: プロパティの型情報、説明、例を明示
- **必須**: はい
- **required**: プロパティが必須かどうかを明示（orvalの型で`string`か`string | undefined`かが決まる）
- **example**: Swagger UIでの表示とテストに使用される

## orval設定例（フロントエンド側）

```typescript
import { defineConfig } from 'orval';

export default defineConfig({
  oby: {
    input: 'http://localhost:8080/v3/api-docs',
    output: {
      target: 'src/api/generated',
      mode: 'tags',  // @Tagでグループ化
      client: 'react-query',
      httpClient: 'fetch',
      baseUrl: '/api',
    },
  },
});
```

## チェックリスト

新しいAPIエンドポイントを追加する際は、以下を確認してください：

- [ ] Controllerに`@Tag`が付与されている
- [ ] すべてのエンドポイントメソッドに`@Operation`が付与されている
- [ ] すべての`@Operation`に**明示的な`operationId`**が設定されている
- [ ] リクエスト/レスポンスで使用するDTOに`@Schema`が付与されている（任意：プロパティの説明が必要な場合）

## 動作確認

以下のURLでOpenAPI仕様とSwagger UIを確認できます：

- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

アプリケーション起動後、上記URLにアクセスして正しくAPI仕様が生成されていることを確認してください。

## トラブルシューティング

### orvalで関数名が予期しないものになる

**原因**: `operationId`が未指定または重複している

**解決策**: すべての`@Operation`に明示的で一意な`operationId`を設定する

### orvalで生成される型が間違っている

**原因**: DTOのプロパティがnullableかどうかが正しく反映されていない

**解決策**: Kotlinの型（`String`か`String?`か）を正しく設定する。SpringDocが自動的に型情報を検出します

### orvalでファイルが適切に分割されない

**原因**: `@Tag`が未指定または一貫性がない

**解決策**: すべてのControllerに適切な`@Tag(name = "...")`を設定

## 参考リンク

- [SpringDoc OpenAPI Documentation](https://springdoc.org/)
- [Orval Documentation](https://orval.dev/)
- [Swagger Annotations](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations)
