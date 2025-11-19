-- テストデータ投入用スクリプト
-- 開発環境でのみ使用されます

-- テストユーザーの作成
-- Google OAuthを経由せずに直接作成するテストユーザー
INSERT INTO users (id, google_sub, email, created_at, updated_at)
VALUES
    ('018d0000-0000-7000-a000-000000000001', 'test-user-001', 'test1@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('018d0000-0000-7000-a000-000000000002', 'test-user-002', 'test2@example.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- テストユーザーのプロフィール作成
INSERT INTO profiles (id, user_id, nickname, bio, avatar_url, created_at, updated_at)
VALUES
    ('018d0010-0000-7000-a000-000000000001', '018d0000-0000-7000-a000-000000000001', 'テストユーザー1', 'これはテストユーザー1のプロフィールです。', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('018d0010-0000-7000-a000-000000000002', '018d0000-0000-7000-a000-000000000002', 'テストユーザー2', 'これはテストユーザー2のプロフィールです。', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (user_id) DO NOTHING;

-- ルーム投稿のテストデータ作成
-- MinIOにアップロードされた画像を参照
INSERT INTO room_posts (id, user_id, title, image_url, description, created_at, updated_at)
VALUES
    ('018d0001-0000-7000-a000-000000000001', '018d0000-0000-7000-a000-000000000001', 'モダンなリビングルーム', 'http://localhost:9000/oby-bucket/room-posts/018d0000-0000-7000-a000-000000000001/test-room-1.jpg', '広々としたリビングルームです。自然光がたっぷり入る設計になっています。', CURRENT_TIMESTAMP - INTERVAL '11 days', CURRENT_TIMESTAMP - INTERVAL '11 days'),
    ('018d0002-0000-7000-a000-000000000001', '018d0000-0000-7000-a000-000000000001', 'シンプルな寝室', 'http://localhost:9000/oby-bucket/room-posts/018d0000-0000-7000-a000-000000000001/test-room-2.jpg', 'ミニマリストスタイルの寝室です。落ち着いた雰囲気で快適な睡眠空間を提供します。', CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP - INTERVAL '10 days'),
    ('018d0003-0000-7000-a000-000000000001', '018d0000-0000-7000-a000-000000000002', 'おしゃれなキッチン', 'http://localhost:9000/oby-bucket/room-posts/018d0000-0000-7000-a000-000000000001/test-room-3.jpg', '最新の設備を備えたキッチンです。料理が楽しくなる空間を目指しました。', CURRENT_TIMESTAMP - INTERVAL '9 days', CURRENT_TIMESTAMP - INTERVAL '9 days'),
    ('018d0004-0000-7000-a000-000000000001', '018d0000-0000-7000-a000-000000000002', 'リラックスできる書斎', 'http://localhost:9000/oby-bucket/room-posts/018d0000-0000-7000-a000-000000000001/test-room-4.jpg', '集中して作業できる静かな書斎です。本棚と作業デスクを配置しています。', CURRENT_TIMESTAMP - INTERVAL '8 days', CURRENT_TIMESTAMP - INTERVAL '8 days'),
    ('018d0005-0000-7000-a000-000000000001', '018d0000-0000-7000-a000-000000000001', '癒しのバスルーム', 'http://localhost:9000/oby-bucket/room-posts/018d0000-0000-7000-a000-000000000001/test-room-5.jpg', 'スパのような雰囲気のバスルームです。一日の疲れを癒す空間として設計しました。', CURRENT_TIMESTAMP - INTERVAL '7 days', CURRENT_TIMESTAMP - INTERVAL '7 days'),
    ('018d0006-0000-7000-a000-000000000001', '018d0000-0000-7000-a000-000000000001', 'コーヒータイムのダイニング', 'http://localhost:9000/oby-bucket/room-posts/018d0000-0000-7000-a000-000000000001/test-room-6.jpg', '朝のコーヒータイムに最適なダイニングスペースです。明るく開放的な雰囲気です。', CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP - INTERVAL '6 days'),
    ('018d0007-0000-7000-a000-000000000001', '018d0000-0000-7000-a000-000000000002', 'ワークスペース', 'http://localhost:9000/oby-bucket/room-posts/018d0000-0000-7000-a000-000000000001/test-room-7.jpg', '在宅ワークに最適なワークスペースです。効率的に作業できる環境を整えました。', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '5 days'),
    ('018d0008-0000-7000-a000-000000000001', '018d0000-0000-7000-a000-000000000001', 'リラックスラウンジ', 'http://localhost:9000/oby-bucket/room-posts/018d0000-0000-7000-a000-000000000001/test-room-8.jpg', 'くつろぎの時間を過ごせるラウンジスペースです。ソファとインテリアで統一感を出しています。', CURRENT_TIMESTAMP - INTERVAL '4 days', CURRENT_TIMESTAMP - INTERVAL '4 days'),
    ('018d0009-0000-7000-a000-000000000001', '018d0000-0000-7000-a000-000000000002', 'エントランスホール', 'http://localhost:9000/oby-bucket/room-posts/018d0000-0000-7000-a000-000000000001/test-room-9.jpg', '来客を迎えるエントランスホールです。洗練されたデザインで第一印象を大切にしています。', CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP - INTERVAL '3 days'),
    ('018d0010-0000-7000-a000-000000000001', '018d0000-0000-7000-a000-000000000001', 'ガーデンテラス', 'http://localhost:9000/oby-bucket/room-posts/018d0000-0000-7000-a000-000000000001/test-room-10.jpg', '屋外のガーデンテラスです。自然を感じながら過ごせる空間です。', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days'),
    ('018d0011-0000-7000-a000-000000000001', '018d0000-0000-7000-a000-000000000002', 'ホームシアター', 'http://localhost:9000/oby-bucket/room-posts/018d0000-0000-7000-a000-000000000001/test-room-11.jpg', '映画鑑賞に最適なホームシアターです。大画面とサウンドシステムで臨場感を楽しめます。', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day')
ON CONFLICT (id) DO NOTHING;
