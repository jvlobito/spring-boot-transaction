FROM tomcat:9.0-jdk11
COPY target/restApi-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]


#testmitec.xooksoft.com.mx