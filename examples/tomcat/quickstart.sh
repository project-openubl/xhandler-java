# Generate .war
mvn clean package

# Download Tomcat
rm -rf workspace/
#wget https://repo1.maven.org/maven2/org/apache/tomcat/tomcat/9.0.46/tomcat-9.0.46.zip -P workspace/
wget https://repo1.maven.org/maven2/org/apache/tomcat/tomcat/10.1.8/tomcat-10.1.8.zip -P workspace/
#unzip workspace/tomcat-9.0.46.zip -d workspace/
unzip workspace/tomcat-10.1.8.zip -d workspace/
#chmod +x -R ./workspace/apache-tomcat-9.0.46/bin
chmod +x -R ./workspace/apache-tomcat-10.1.8/bin

# Copy .war to Tomcat
#cp target/tomcat-xbuilder-xsender-0.0.1-SNAPSHOT.war workspace/apache-tomcat-9.0.46/webapps/demo.war
cp target/tomcat-xbuilder-xsender-0.0.1-SNAPSHOT.war workspace/apache-tomcat-10.1.8/webapps/demo.war

# Start Tomcat
#./workspace/apache-tomcat-9.0.46/bin/startup.sh
./workspace/apache-tomcat-10.1.8/bin/startup.sh

sleep 10s
