cd framework
    javac -cp servlet-api.jar -d . *java
    jar -cf fw.jar .
    copy fw.jar "D:\IT\L2\S4\Web Dynamique Mr Naina\framework\test-framework\WEB-INF\lib"

cd ..

cd test-framework/
    jar -cf test-servlet.war *
    copy "test-servlet.war" "C:\Program Files\apache-tomcat-10.1.8\webapps"
cd ..