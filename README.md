# 単語帳アプリ【MyWordbook】
![AppImage](https://user-images.githubusercontent.com/94959504/198753869-6abdc75f-ea94-4d11-93da-44bf6a628dc9.png)

## アプリ紹介
言語学習の際に必要である単語を覚えるために開発したシンプルな単語帳アプリです。

単語の意味だけでなく、その単語が使われている例文等様々なことが書けるメモ機能も搭載しています。

## 主に搭載した機能
1. 複数の単語をまとめられる「単語帳」を追加

   ![screen001](https://user-images.githubusercontent.com/94959504/198754233-a335029a-7c8a-43a9-8cff-f1015a13612c.PNG)
2. 「単語帳」内に単語を追加

   ![screen002](https://user-images.githubusercontent.com/94959504/198754289-2e1c06ab-ea82-4aa1-bf9d-dff5ff0048da.PNG)
3. 追加した各単語をタップすることで、単語とその意味が書かれたダイアログが表示される。目のアイコンをタップすることで内容の表示・非表示を切り替えられる。

   ![screen003](https://user-images.githubusercontent.com/94959504/198754334-76a6254f-a1b6-40a4-921a-292388c6e6ae.PNG)
4. 各単語を長押しすることで、単語の詳細情報を確認することができ、そこで例文といったメモ情報の編集ができる。

   ![screen004](https://user-images.githubusercontent.com/94959504/198754505-d8d2748f-254b-4961-9eaa-12903eee3945.PNG)

## 主な使用技術・ライブラリ等
- Kotlin
- Jetpack Compose
  - ConstraintLayout-compose
  - Navigation-compose
- Android Jetpack
  - Room
  - ViewModel
  - DataStore
- Hilt
- Gson
- accompanist-pager

## その他搭載した機能
- リストを作成日時が古い順・新しい順等に並べ替える機能
- 並び順変更の操作を受け付けるレイアウト部分の表示非表示を切り替える機能
- Pagerを用いて、単語を表示するダイアログを左右フリックによって表示内容を切り替えられる機能
