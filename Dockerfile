FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} ddetect-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/ddetect-0.0.1-SNAPSHOT.jar"]

ENV PORT=5000