# Mutaneratorとは
Javaのプログラムに対して，ミュータント（少しだけ変更したプログラム）を生成する祖父東亜です．
他の多くのミュータント生成ツールは，Javaのバイトコードを書き換えてミュータントを生成しますが，Muteneratorはソースコードに対して書き換えを行います．

# 使い方

まずはGitHubからダウンロードして下さい．
その後Jarファイルを生成して下さい．
`build/libs/Mutanerator-all.jar`というファイルができていれば成功です．
```sh
git clone git@github.com:kusumotolab/Mutanerator.git
cd Mutanerator
./gradlew shadowJar
```

Jarファイルが実行できたら，同梱のサンプルプログラムに対して実行してみて下さい．
下記がコマンドラインの例です．
`log.csv`というファイルと`mutations`というフォルダができていれば成功です．
```sh
java -jar build/libs/Mutanerator-all.jar -f src/test/java/example/CloseToZero.java -l log.csv
```

`-l`オプションでログファイルを指定すると全てのミュータントに対するログを指定したファイルに吐きます．`-l`を指定しない場合はログを出力しません．
ログファイルはCSVフォーマットです．
左から順に，ミュータントID（ディレクトリ名と同じ），適用したミューテイターの種類，変更した行，変更前テキスト，変更後テキスト，となっています．
```
1, ConditionalsBoundary, 6, "0 < value", "0 <= value"
2, ConditionalsBoundary, 9, "value < 0", "value <= 0"
3, Increments, 7, "value--", "value++"
4, Increments, 10, "value++", "value--"
5, NegateConditionals, 6, "0 < value", "0 >= value"
6, NegateConditionals, 9, "value < 0", "value >= 0"
```
