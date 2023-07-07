javac -cp .\WEB-INF\lib\fw.jar -d . .\etu1924\model\*.java

jar -cf test-servlet.war *
    copy "test-servlet.war" "C:\Program Files\apache-tomcat-10.1.8\webapps"
cd ..