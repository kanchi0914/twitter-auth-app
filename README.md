# 概要
フロントエンド部分：https://github.com/nomurashunta/twitter_app_frontend
Auth認証を利用したTwitterクライアントのデモアプリです。 
GCPのNatural Language APIを使い、ネガティブなツイートを非表示にします。
http://ec2-3-22-197-231.us-east-2.compute.amazonaws.com:8090/

### 環境
- Spring Boot 2.3.0
- Gradle 5.6.4
- twitter4j 4.0.7
- Google Natural Language Api 1.100.0
- node.js 14.6.0
- Nuxt.js 2.12.2
- yarn 1.22.5

## 画面
![トップ画面](images/top2.jpg "top")

## 仕様など
- ログイン後、サーバーからアクセストークンが発行されるので、それをフロント側で保存して各エンドポイントからTwitter APIを叩く
- アクセストークンはCookieではなくVuex Storeに保存するようにした
- デザインはVuetify + Materilal Desigin Icon
- ログイン時の認証はOAuth 1.0(Twitter APIがその仕様のため)

## 感想(v1)
- フロント部分をReactなどで作り直したい。thymeleafだけで要素を動的に表示するのが難しそう
  - → Nuxt.jsで作り直した
- 現状バックエンドにKotlin+Spring Bootを使う意味がほぼないので、GCPでなくjavaの機械学習ライブラリを使うなどの工夫がしたい。
  JavaにはDeepLearning4Jがある
  - →今後の課題として...
- 認証キー等を直接レポジトリに入れてるので現状ではデプロイできない、本番環境で使うためにはどうすればいいか調べる必要がある
  - →WinSCPで転送した
- 正直OAuth認証について未だによくわかってないので根本的に勉強が必要
  - →少しわかるようになった

## 感想(v2)
- Nuxt+Typescriptのボイラープレート使ってみたが正直何もわからず、、、結局普通のVue+jsの書き方になってしまった
- Twitter APIの制限が厳しく(ホームタイムラインが15回/15分しか取得できない)、
本格的にクライアントアプリを作るのが難しそう

## 参考サイト

https://careydevelopment.us/2017/05/24/implement-twitter-login-solution-spring-boot/  
https://cloud.google.com/natural-language/docs/analyzing-sentiment?hl=jahttps://cloud.google.com/natural-language/docs/analyzing-sentiment?hl=ja
