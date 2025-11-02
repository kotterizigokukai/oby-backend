## 概要

- **フォーマッター**: [ktlint](https://github.com/pinterest/ktlint) v1.0.1
- **自動チェック**: GitHub Actions（PR 時）

## 使い方

### コードスタイルをチェック

```bash
./gradlew ktlintCheck
```

### 自動修正

```bash
./gradlew ktlintFormat
```

### Q: なぜ Prettier ではなく ktlint を使うのですか？

A: Prettier は web 開発言語（JavaScript/TypeScript 等）専用です。Kotlin プロジェクトには Kotlin 公式の ktlint が最適です。
