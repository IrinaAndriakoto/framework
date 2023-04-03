cd framework
    javac -d . FrontServlet.java
    javac -d . Mapping.java
    javac -d . url.java
    javac -d . *.java
    jar -cf fw.jar .
    copy fw.jar "D:\IT\L2\S4\Web Dynamique Mr Naina\framework\test-framework\WEB-INF\lib"

cd ..

cd test-framework/
    jar -cf test-servlet.war *
    copy "test-servlet.war" "C:\Program Files\Apache Software Foundation\Tomcat 10.0\webapps"
cd ..