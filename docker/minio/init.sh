#!/bin/sh

echo "Starting MinIO initialization..."

# MinIO CLIのエイリアス設定（接続テストも兼ねる）
echo "Connecting to MinIO..."
until mc alias set myminio http://minio:9000 ${MINIO_ROOT_USER} ${MINIO_ROOT_PASSWORD} > /dev/null 2>&1; do
    echo "Waiting for MinIO to be ready..."
    sleep 2
done

echo "MinIO is ready. Configuring bucket..."

# バケット作成（既に存在する場合はスキップ）
BUCKET_NAME=${MINIO_BUCKET:-oby-bucket}
if mc ls myminio/${BUCKET_NAME} > /dev/null 2>&1; then
    echo "Bucket '${BUCKET_NAME}' already exists"
else
    echo "Creating bucket '${BUCKET_NAME}'..."
    mc mb myminio/${BUCKET_NAME}
fi

# バケットポリシーを完全公開 (public) に設定
echo "Setting bucket policy to public..."
mc anonymous set public myminio/${BUCKET_NAME}

# テスト画像のアップロード
if [ -d "/test-images" ] && [ "$(ls -A /test-images 2>/dev/null)" ]; then
    echo "Uploading test images..."
    for image in /test-images/*.jpg /test-images/*.jpeg /test-images/*.png; do
        if [ -f "$image" ]; then
            filename=$(basename "$image")
            # テストユーザーID（data.sqlと一致させる必要がある）
            # 018d0000-0000-7000-a000-000000000001
            mc cp "$image" "myminio/${BUCKET_NAME}/room-posts/018d0000-0000-7000-a000-000000000001/$filename"
            echo "Uploaded: $filename"
        fi
    done
    echo "Test images uploaded successfully!"
else
    echo "No test images found in /test-images, skipping upload..."
fi

echo "MinIO initialization completed successfully!"
