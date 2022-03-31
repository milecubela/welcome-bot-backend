**WelcomeBot v1**

<h5>HOW TO RUN </h5>
Download the project and make sure you have docker and docker-compose installed.<br>
Open terminal in the project directory and paste the following line with sudo permissions:

```text

docker-compose -f docker-compose-prod.yml up

```

Technologies used

```text
Java 17 - JDK 17.0.2
Spring Boot 2.6.4
MariaDB 
Dependencies: 
    - Spring Data JPA
    - OAuth2 Client
    - Spring Web
    - Flyway
    - MariaDB Java Client
```

A slack bot with the ability to send automated messages and talk to slack users.

Admin features :

```text
Messages - Create new message, delete a message
         - Bind schedule to message
         - Bind trigger to message
         
Schedule - Create a new schedule
         - Activate/Deactivate
         - Repeat/One time
         - Update schedule
         
Trigger  - Create a new trigger 
         - Bind a slack channel
         - Activate/Deactivate 
```

Adding new events and commands:
You can find the supported event types here: https://api.slack.com/events

```text
Adding events:
    1. Add new enum member in TriggerEvent under Utilities package that describes the event.
    2. Create a new class in SlackEvents package that will contain its interface methods.
Adding commands:
Before going through these steps the bot owner needs to subscribe to certain SlashCommands in its bot configuration page.
    1. Add new enum member in SlackCommand under Utilities package.
    2. Create a new class in SlackCommands package that will contain its interface methods.
```

Bot can also communicate with users and listen to their messages to give them a correct response. Responses are defined
in database. 
