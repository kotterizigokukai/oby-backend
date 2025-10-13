# コードフォーマット設定

このプロジェクトではコードスタイルの統一のため、**ktlint**を使用しています。

## 概要

- **フォーマッター**: [ktlint](https://github.com/pinterest/ktlint) v1.0.1
- **スタイルガイド**: Kotlin公式スタイルガイド準拠
- **自動チェック**: GitHub Actions（PR時）

---

## セットアップ

### 必要なもの

- JDK 21
- Gradle（プロジェクトに含まれています）

### 初回セットアップ

```bash
# リポジトリをクローン
git clone <repository-url>
cd oby-backend

# 依存関係をダウンロード（ktlintも自動的にセットアップされます）
./gradlew build
```

---

## 使い方

### コードスタイルをチェック

```bash
./gradlew ktlintCheck
```

違反がある場合、エラーメッセージとファイル/行番号が表示されます。

### 自動修正

```bash
./gradlew ktlintFormat
```

自動修正可能な違反は全て修正されます。

### 特定のソースセットのみチェック

```bash
./gradlew ktlintMainSourceSetCheck    # src/main のみ
./gradlew ktlintTestSourceSetCheck    # src/test のみ
```

---

## 推奨ワークフロー

### 開発中

1. コードを書く
2. 保存時にエディタが自動フォーマット（推奨設定は下記参照）

### コミット前

```bash
./gradlew ktlintFormat
```

コミット前に実行して違反を修正しておくと、CIで失敗しません。

### PR作成前

```bash
./gradlew ktlintCheck
```

すべてのチェックがパスすることを確認してからPRを作成してください。

---

## エディタ設定

### IntelliJ IDEA / Android Studio

1. **設定** → **Editor** → **Code Style** → **Kotlin**
2. **Set from...** → **Predefined Style** → **Kotlin style guide**
3. **Apply** をクリック

`.editorconfig`が自動的に認識されるため、追加設定は不要です。

### VSCode

拡張機能をインストール：
```
Name: Kotlin
Publisher: mathiasfrohlich
```

`.editorconfig`が自動的に認識されます。

### Neovim / Vim

`.editorconfig`が自動的に認識されます（`editorconfig-vim`プラグインが必要な場合があります）。

---

## CI/CD（GitHub Actions）

### 自動チェック

PR作成時やmain/masterブランチへのpush時に、自動的に`ktlintCheck`が実行されます。

### チェックが失敗した場合

1. GitHub Actionsのログでエラーメッセージを確認
2. ローカルで`./gradlew ktlintFormat`を実行
3. 修正をコミット・push

### レポートの確認

チェックが失敗した場合、GitHub ActionsのArtifactsからktlintレポートをダウンロードできます：

1. GitHub Actionsのワークフロー実行ページへ移動
2. **Artifacts**セクションから`ktlint-reports`をダウンロード
3. テキストファイルで詳細な違反内容を確認

---

## よくある質問

### Q: ktlintのルールを無効化できますか？

A: 可能ですが、推奨しません。チーム全体のコードスタイル統一のため、公式スタイルガイドに従うことを推奨します。

どうしても必要な場合は、`build.gradle.kts`の`ktlint`セクションで設定できます。

### Q: なぜPrettierではなくktlintを使うのですか？

A: Prettierはweb開発言語（JavaScript/TypeScript等）専用です。KotlinプロジェクトにはKotlin公式のktlintが最適です。

### Q: フォーマットを自動で行いたくない場合は？

A: GitHub Actionsのチェックはスキップできませんが、ローカルでは`ktlintCheck`のみ実行して手動修正することも可能です。

### Q: コミット時に自動でフォーマットされるようにできますか？

A: Git Hooksを使えば可能です。今後導入を検討しています。

---

## 参考リンク

- [ktlint公式ドキュメント](https://pinterest.github.io/ktlint/)
- [Kotlin公式スタイルガイド](https://kotlinlang.org/docs/coding-conventions.html)
- [ktlint Gradleプラグイン](https://github.com/JLLeitschuh/ktlint-gradle)

---

## トラブルシューティング

### `./gradlew ktlintCheck`が遅い

初回実行時はktlintのダウンロードに時間がかかります。2回目以降は高速です。

### チェックが通らない

```bash
# レポートファイルを確認
cat build/reports/ktlint/ktlintMainSourceSetCheck/ktlintMainSourceSetCheck.txt
```

詳細なエラー内容が記載されています。

### Gradleのキャッシュをクリアしたい

```bash
./gradlew clean
rm -rf .gradle build
./gradlew build
```

---

## サポート

フォーマット設定に関する質問や問題があれば、Issueを作成するか、チームメンバーに相談してください。
