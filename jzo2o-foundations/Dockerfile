FROM openjdk:11-jdk
LABEL maintainer="研究院研发组 <research-maint@itcast.cn>"
RUN echo "Asia/Shanghai" > /etc/timezone
ARG PACKAGE_PATH=./target/jzo2o-foundations.jar

ADD ${PACKAGE_PATH:-./} app.jar
EXPOSE 8080
EXPOSE 9999
ENTRYPOINT ["sh","-c","java  -jar $JAVA_OPTS app.jar"]