FROM tomcat:10-jdk17-corretto

ENV TZ=Asia/Seoul

COPY hiapp/build/libs/hifive.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]

