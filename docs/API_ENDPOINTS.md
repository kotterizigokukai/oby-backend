# API エンドポイント一覧

`/api/v1` 配下の REST エンドポイント（Kotlin Spring Boot × React/TS）

| #   | Method | Path                     | 説明                         | 認証   |
| --- | ------ | ------------------------ | ---------------------------- | ------ |
| A1  | POST   | /auth/signup             | 新規登録                     | なし   |
| A2  | POST   | /auth/login              | ログイン                     | なし   |
| A3  | POST   | /auth/refresh            | リフレッシュトークンで再発行 | なし   |
| U1  | GET    | /users/me                | 自分のプロフィール取得       | 要 JWT |
| U2  | PATCH  | /users/me                | 自分のプロフィール更新       | 要 JWT |
| P1  | POST   | /posts                   | 汚部屋投稿作成（写真＋説明） | 要 JWT |
| P2  | GET    | /posts                   | 投稿一覧                     | 任意   |
| P3  | GET    | /posts/{postId}          | 投稿詳細                     | 任意   |
| P4  | DELETE | /posts/{postId}          | 自分の投稿削除               | 要 JWT |
| R2  | GET    | /posts/{postId}/ratings  | 平均評価取得                 | 要 JWT |
| C1  | POST   | /posts/{postId}/comments | コメント作成                 | 要 JWT |
| C2  | GET    | /posts/{postId}/comments | コメント一覧                 | 任意   |
| H1  | GET    | /health                  | ヘルスチェック               | なし   |
