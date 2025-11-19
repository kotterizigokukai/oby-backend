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

echo "MinIO initialization completed successfully!"
