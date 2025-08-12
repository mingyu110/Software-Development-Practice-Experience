FROM openjdk:21-jdk-slim
RUN apt-get update && apt-get install -y netcat-traditional
VOLUME /tmp
ARG JAR_FILE=target/discovery-server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]