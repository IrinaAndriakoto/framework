cd framework
    javac -cp D:\.jar\commons-io-2.7.jar;servlet-api.jar -d . *java
    jar -cf fw.jar .
    copy fw.jar "C:\Users\USER\Documents\GitHub\framework\framework\test-framework\WEB-INF\lib"

cd ..

cd test-framework/
