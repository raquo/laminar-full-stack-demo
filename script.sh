if [ -f client/src/main/scala/com/raquo/app/Foo2.scala ]; then
    # If Foo2.scala exists, change Foo2 -> Foo
    sed -i '' 's/Foo2\.x/Foo.x/g' client/src/main/scala/com/raquo/app/HomePageView.scala
    sed -i '' 's/Foo2/Foo/g' client/src/main/scala/com/raquo/app/Foo2.scala
    mv client/src/main/scala/com/raquo/app/Foo2.scala client/src/main/scala/com/raquo/app/Foo.scala
else
    # If Foo2.scala does not exist, change Foo -> Foo2
    sed -i '' 's/Foo\.x/Foo2.x/g' client/src/main/scala/com/raquo/app/HomePageView.scala
    sed -i '' 's/Foo/Foo2/g' client/src/main/scala/com/raquo/app/Foo.scala
    mv client/src/main/scala/com/raquo/app/Foo.scala client/src/main/scala/com/raquo/app/Foo2.scala
fi
