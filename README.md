# 開発環境構築 README

Oby Backend をローカルで動かすための手順をまとめています。新しくプロジェクトに参加したメンバーはこのドキュメントを順番に進めてください。

## 前提ソフトウェア

| ツール | 推奨バージョン | 備考 |
| --- | --- | --- |
| Git | 任意 | リポジトリの取得に使用します |
| JDK | 21 | Temurin, Corretto など任意のディストリビューションで可 |
| Docker / Docker Compose | 24.x / v2 以上 | PostgreSQL コンテナ起動に使用します |
| Gradle Wrapper | 付属 | `./gradlew` がバンドル済み |

> **メモ**: Windows ユーザーは `git` 実行時に改行コードを自動変換しない設定（`git config --global core.autocrlf input`）にしておくと、シェルスクリプトが壊れにくくなります。

## 1. リポジトリの取得

```bash
git clone https://github.com/kotterizigokukai/oby-backend.git
cd oby-backend
```

初回のみ、UNIX 系 OS では Gradle Wrapper に実行権限を付与してください。

```bash
chmod +x gradlew
```

## 2. JDK 21 のインストール確認

```bash
java -version
```

`openjdk version "21"` のように 21 系が表示されれば準備完了です。異なるバージョンが表示される場合は、以下いずれかの方法で切り替えてください。

- [SDKMAN!](https://sdkman.io/) を利用して `sdk install java 21.x.x-tem` を実行
- Homebrew などのパッケージマネージャで JDK 21 をインストールし、`JAVA_HOME` を設定

## 3. 環境変数の準備

アプリケーションは `.env` に定義された値を読み込みます。雛形をコピーして必要なら値を調整してください。

```bash
cp .env.example .env
```

| 変数名 | デフォルト値 | 説明 |
| --- | --- | --- |
| `DB_NAME` | `obydb` | 使用するデータベース名 |
| `DB_USER` | `obyuser` | データベースユーザー名 |
| `DB_PASSWORD` | `obypass123` | データベースパスワード |
| `DB_PORT` | `5432` | ホスト側の公開ポート |
| `DB_HOST` | `localhost` | Spring Boot から接続するホスト名 |

## 4. PostgreSQL コンテナの起動

```bash
docker-compose up -d
```

起動後は `docker-compose ps` でコンテナ状態を確認できます。ログを追跡する場合は `docker-compose logs -f postgres` を利用してください。

コンテナは永続ボリューム `postgres_data` にデータを保存します。全データを削除したい場合のみ `docker-compose down -v` を実行します。

## 5. アプリケーションの起動

Gradle Wrapper から Spring Boot を起動します。

```bash
./gradlew bootRun
```

Windows の PowerShell / コマンドプロンプトでは `gradlew.bat bootRun` を使用してください。

アプリケーションが正常に起動すると、API は `http://localhost:8080` で利用可能になります。基本的な疎通確認は以下で行えます。

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI (JSON): http://localhost:8080/v3/api-docs

## 6. 動作検証

開発者向けに最低限以下のコマンドを実行し、環境が正常に機能することを確認してください。

```bash
# 単体テスト
./gradlew test

# 静的解析 (ktlint)
./gradlew ktlintCheck
```

問題があればスタックトレースとログを確認し、必要に応じてチームに共有してください。

## 7. よく使う運用コマンド

```bash
# PostgreSQL の停止
docker-compose stop

# PostgreSQL の再起動
docker-compose restart postgres

# DB へ接続 (psql)
docker-compose exec postgres psql -U ${DB_USER:-obyuser} -d ${DB_NAME:-obydb}

# アプリのクリーンビルド
./gradlew clean build
```

## 8. トラブルシューティング

- **ポート 5432 が使用中**
  既にローカルで PostgreSQL が動作している場合は停止するか、`.env` の `DB_PORT` を別ポートに変更してください。

- **アプリから DB に接続できない**
  `docker-compose ps` でコンテナが起動しているか確認し、必要に応じて `docker-compose restart postgres` を実行します。`docker-compose logs postgres` にエラーログが出ていないかも確認します。

- **Gradle ビルドが失敗する**
  `java -version` で JDK 21 が利用されているかを再確認してください。加えて `./gradlew --stop && ./gradlew clean` を実行すると改善するケースがあります。

## 9. 次のステップ

- コーディング前に `./gradlew ktlintFormat` を実行し、コードスタイル差分を解消しておくとレビューがスムーズになります。
