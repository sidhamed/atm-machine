FROM openjdk:8-jdk-alpine
MAINTAINER siddiqhamed2017@gmail.com
COPY build/libs/machine-0.0.1-SNAPSHOT.jar machine-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/machine-0.0.1-SNAPSHOT.jar"]