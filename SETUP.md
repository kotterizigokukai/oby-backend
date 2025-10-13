# Oby Backend - 開発環境セットアップ

## 必要な環境

- Java 21
- Docker & Docker Compose
- Git

## セットアップ手順

### 1. リポジトリのクローン

```bash
git clone <repository-url>
cd oby-backend
```

### 2. 環境変数の設定

`.env.example`をコピーして`.env`を作成：

```bash
cp .env.example .env
```

必要に応じて`.env`の値を編集してください（デフォルトのままでも動作します）。

### 3. データベースの起動

Docker Composeを使ってPostgreSQLを起動：

```bash
docker-compose up -d
```

初回起動時は自動的にデータベースが作成されます。

### 4. データベースの状態確認

```bash
# コンテナが起動しているか確認
docker-compose ps

# ログを確認
docker-compose logs postgres

# データベースに接続してみる
docker-compose exec postgres psql -U obyuser -d obydb
```

### 5. アプリケーションの起動

```bash
./gradlew bootRun
```

アプリケーションは `http://localhost:8080` で起動します。

## よく使うコマンド

### データベース操作

```bash
# データベースの起動
docker-compose up -d

# データベースの停止
docker-compose stop

# データベースの完全削除（データも消えます）
docker-compose down -v

# データベースのログを見る
docker-compose logs -f postgres

# データベースに接続
docker-compose exec postgres psql -U obyuser -d obydb
```

### アプリケーション操作

```bash
# アプリケーションの起動
./gradlew bootRun

# ビルド
./gradlew build

# テスト実行
./gradlew test

# クリーンビルド
./gradlew clean build
```

## トラブルシューティング

### ポート5432が既に使用されている

既にローカルのPostgreSQLが起動している場合：

```bash
# ローカルのPostgreSQLを停止
sudo systemctl stop postgresql

# または .env でポートを変更
DB_PORT=5433
```

### データベースに接続できない

```bash
# コンテナが起動しているか確認
docker-compose ps

# ヘルスチェックの状態を確認
docker-compose ps postgres

# コンテナを再起動
docker-compose restart postgres
```

### Gradleがビルドに失敗する

Java 21が正しく設定されているか確認：

```bash
java -version
# openjdk version "21.x.x" と表示されるはず
```

## チーム開発のルール

1. `.env`ファイルは各自のローカル環境に合わせて設定し、**Gitにコミットしない**
2. `.env.example`を更新した場合は、チームメンバーに通知する
3. `docker-compose.yml`を変更した場合は、チームメンバーに通知する
4. データベースのマイグレーションは（今後追加予定）
