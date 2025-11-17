# Google サインイン統合

このドキュメントは、バックエンドで認証を担う Google OAuth 2.0 / OpenID Connect の設定手順と利用方法をまとめたものです。

## 概要

- 外部IDプロバイダーは Google のみで、OAuth 2.0 / OpenID Connect を通じて認証します。
- Spring Security が認証フロー、セッション管理、CSRF 保護を担当します。
- ユーザープロファイル（メール、表示名、アバター、ログイン時刻）は PostgreSQL に永続化されます。
- Google のアクセストークン／リフレッシュトークンは保持せず、最小限のプロフィール情報のみ保存します。

## 前提条件

1. **Google Cloud プロジェクト**
   - 新規作成または既存プロジェクトを利用します。
   - *Google Identity Services (OAuth 2.0)* API を有効化します。
2. **OAuth 同意画面**
   - 同意画面を公開します（本番では External タイプが必要）。
   - `openid`, `profile`, `email` のスコープを追加します。
3. **OAuth クライアント認証情報**
   - *Web アプリケーション* 用の OAuth クライアントIDを作成します。
   - リダイレクトURIに `http://localhost:8080/login/oauth2/code/google` を追加します（環境に応じて変更）。
   - 発行された **Client ID** と **Client Secret** を控えます。

## 環境変数

`.env.example` をもとに `.env` を作成し、上記情報を設定します。

```dotenv
GOOGLE_CLIENT_ID=xxxxxxx.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=super-secret
SERVER_SESSION_COOKIE_SECURE=false  # HTTPS 運用時は true にする
```

> 本番環境では `SERVER_SESSION_COOKIE_SECURE=true` を必ず使用し、Cookie を HTTPS のみで送信してください。

`application.properties` の `spring.security.oauth2.client.*` からこれらの変数を参照します。

## ローカル実行手順

1. 環境変数をエクスポートするか `.env` を作成します。
2. PostgreSQL を起動します（Compose を使う場合は `docker-compose up db` を実行）。
3. アプリケーションを起動します。

```bash
./gradlew bootRun
```

4. ブラウザで `http://localhost:8080/oauth2/authorization/google` にアクセスし、ログイン後 `/auth/me` へリダイレクトされることを確認します。

## セキュリティ上の考慮事項

- CSRF トークンは Cookie ベースで配布し、ブラウザクライアントを保護します。
- セッション Cookie は SameSite・HTTP Only を使用し、環境に応じて `SERVER_SESSION_COOKIE_SECURE` を切り替えます。
- セッション固定攻撃は `migrateSession` で防止し、同時接続制御のため `HttpSessionEventPublisher` を登録します。
- ユーザーごとに同時セッションは1件のみ許可。新しいログインで旧セッションが失効し、クライアントには HTTP 401 を返して再認証を促します。
- 保存するプロフィール情報はID管理に必要な最小限のみで、Googleのトークン類は永続化しません。
- `/api/messages` の `GET` は誰でも利用可能ですが、作成系は認証と `ROLE_USER` 権限が必須です。
- ロールは階層化されており `ROLE_ADMIN` は `ROLE_USER` を継承します。これによりコントローラやメソッドレベルの権限制御が簡潔になります。

## 主要エンドポイント

- `GET /oauth2/authorization/google` – Google ログインフローの開始。
- `GET /auth/me` – 認証済みユーザーのプロフィール取得（要ログイン）。
- `POST /auth/logout` – Spring Security のログアウトエンドポイント（CSRF トークン必須、HTTP 204 を返却）。
- `GET /api/messages` – 公開エンドポイント。メッセージ閲覧。
- `POST /api/messages` – 認証必須。`ROLE_USER` が必要。

## データベーススキーマ

`schema.sql` では次の2テーブルを作成します。

- `app_user`: Google アカウントとの紐付けとプロフィール情報を保存。
- `message`: `MessageController` が使用するサンプルデータ。

Spring の SQL 初期化機能により、アプリ起動時に自動適用されます。
