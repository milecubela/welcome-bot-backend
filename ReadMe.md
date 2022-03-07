**WelcomeBot v1**


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

Bot can also communicate with users and listen to their messages to give them a correct response.
Responses are defined in database. 