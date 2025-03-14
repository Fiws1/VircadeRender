FROM maven:3.8.7-openjdk-18 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM maven:3.8.7-openjdk-18-slim
ARG JAR_FILE=target/Vircade-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_Vircade.jar
EXPOSE 8080
ENTRYPOINT ["java" , "-jar" , "app_Vircade.jar"]
