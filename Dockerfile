FROM openjdk:17
ADD target/welcomeBot.jar welcomeBot.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "welcomeBot.jar"]
