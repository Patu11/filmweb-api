FROM maven:3.8-openjdk-17 AS maven

WORKDIR /build
COPY . /build

RUN mvn package -Dmaven.test.skip

FROM openjdk:17-alpine

WORKDIR /app

COPY --from=maven /build/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]