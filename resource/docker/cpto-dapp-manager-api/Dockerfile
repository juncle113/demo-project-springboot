FROM swr.cn-north-1.myhuaweicloud.com/chaincat-base/jdk:8u192-zulu-ubuntu
MAINTAINER Sunli@ChainCat
COPY cpto-dapp-manager-api-1.0.0.jar /app.jar
#CMD java -jar /app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar
EXPOSE 8082
