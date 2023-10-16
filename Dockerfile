#
# Build stage
#
FROM maven:3.8.2-jdk-11 AS build
#Copy ..
COPY src /home/app/src
COPY pom.xml /home/app
WORKDIR /home/app
RUN mvn clean package -DskipTests

#
# Package stage
#
FROM openjdk:11-jdk-slim
#COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar demo.jar
COPY --from=build /home/app/target/*.jar app.jar
# ENV
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]

