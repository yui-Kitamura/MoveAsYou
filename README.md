# ![logo](https://github.com/user-attachments/assets/9459806d-73ae-40b8-b71f-88d23611deab) MoveAsYou

## 概要
MoveAsYouは配信向けにクロマキー背景でスキン全身とゲーム内に合わせた動きの表現を提供するMinecraftプラグインです。

## 機能
- クロマキー用背景色（緑、青、赤）
- Minecraftサーバーとのリアルタイム連動
  - 上下の視線方向
  - 手を振り回すモーション

## サーバー管理者向けガイド
### 動作要件
- Java 17 以上
- Spigot/Paperサーバー 1.20.4 以上

### インストール方法
1. GitHub上の最新リリースから`MoveAsYou-v.v.v.jar`をダウンロード
2. サーバーの`plugins`フォルダに配置
3. ポート`38080(HTTP)`と`38081(WebSocket)`の開放

### 設定方法
初回起動時に`plugins/MoveAsYou/`に設定ファイルが生成されます。  

## ユーザーガイド
### 基本コマンド
`/moveasyou token onetime` : 1回限り有効の表示連携トークンを発行します  
`/moveasyou token stream` : 配信向け再利用可能トークンを発行します  
`/moveasyou token admin` : 管理者トークンを発行します  
`/moveasyou token list` : 発行したトークンの一覧を表示します。管理者トークンを含みます  
`/moveasyou token list playerName` : 指定ユーザについて有効なトークンの一覧を表示します  
`/moveasyou token revoke token-code` : 指定トークンを無効にします  
`/moveasyou token revoke all playerName` : 指定プレイヤーのトークンを無効にします  
`/moveasyou token revoke admin` : 管理者トークンをすべて無効にします  
`/moveasyou stats` : トークンの発行状況を表示します  
`/moveasyou help config` Configの詳細を表示します  
= configコマンド =  
`/moveasyou config yuiktmr backgroundcolor colorName` : ウェブ画面で表示する背景色を設定します  
`/moveasyou config playerName bagkgroundcolor colorName` : 指定プレイヤーのウェブ画面で表示する背景色を設定します

### クロマキー背景色
利用可能な背景色：
- 緑（colorName=`GREEN`）
- 青（colorName=`BLUE`）
- 赤（colorName=`RED`）

## サポート
不具合報告や機能リクエストは[GitHubのIssues](https://github.com/yui-Kitamura/MoveAsYou/issues)までお願いします。

## 開発者情報
開発者: yui-KITAMURA  
組織: Yui-KitamuraLLC.
