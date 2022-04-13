FROM maven:3.8.4-openjdk-17-slim AS builder

WORKDIR /app

COPY pom.xml .


COPY src ./src

RUN mvn install -DskipTests

FROM openjdk:17

WORKDIR /app

COPY --from=builder /app/target/welcomeBot.jar ./welcomeBot.jar

ENTRYPOINT ["java", "-jar", "./welcomeBot.jar"]
