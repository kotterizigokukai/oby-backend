# oby-backend

Kotlin + Spring Bootで構築されたバックエンドAPIサーバー

## 技術スタック

- **言語**: Kotlin 1.9.25
- **フレームワーク**: Spring Boot 3.5.6
- **JDK**: 21
- **データベース**: PostgreSQL
- **ビルドツール**: Gradle

## セットアップ

### 前提条件

- JDK 21
- Docker & Docker Compose（データベース用）

### 起動方法

1. **リポジトリをクローン**
   ```bash
   git clone <repository-url>
   cd oby-backend
   ```

2. **データベースを起動**
   ```bash
   docker-compose up -d
   ```

3. **アプリケーションを起動**
   ```bash
   ./gradlew bootRun
   ```

4. **動作確認**
   ```
   http://localhost:8080
   ```

## ビルド

```bash
./gradlew build
```

## テスト

```bash
./gradlew test
```

## コードフォーマット

このプロジェクトではコードスタイルの統一のため**ktlint**を使用しています。

### クイックスタート

```bash
# コードスタイルをチェック
./gradlew ktlintCheck

# 自動修正
./gradlew ktlintFormat
```

### 推奨ワークフロー

**コミット前に必ず実行してください：**
```bash
./gradlew ktlintFormat
```

これにより、CI/CDでのチェック失敗を防げます。

### 詳細情報

エディタ設定、CI/CD、トラブルシューティングなどの詳細は [docs/FORMATTING.md](docs/FORMATTING.md) を参照してください。

## プロジェクト構成

```
oby-backend/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── com/example/obybackend/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── schema.sql
│   └── test/
│       └── kotlin/
├── build.gradle.kts
├── docker-compose.yml
└── docs/
    └── FORMATTING.md
```

## 開発

### 環境変数

`.env`ファイルを作成してください（`.env.example`を参考に）：

```bash
cp .env.example .env
```

### API仕様

アプリケーション起動後、以下のURLでAPIを確認・テストできます：

- **Swagger UI**: http://localhost:8080/swagger-ui.html
  - ブラウザでAPIをインタラクティブにテスト可能
- **OpenAPI仕様**: http://localhost:8080/v3/api-docs
  - フロントエンドのorvalがこのエンドポイントから型を自動生成

詳細は [docs/orval-api-guidelines.md](docs/orval-api-guidelines.md) を参照してください。

## CI/CD

GitHub Actionsを使用して、PR時に自動的にktlintチェックが実行されます。

## ライセンス

（TODO: ライセンス情報を追加）
