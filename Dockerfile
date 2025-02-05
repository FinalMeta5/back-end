FROM tomcat:10-jdk17-corretto

ENV TZ=Asia/Seoul

RUN rm -rf /usr/local/tomcat/webapps/*
COPY bururung/build/libs/hifive.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]

