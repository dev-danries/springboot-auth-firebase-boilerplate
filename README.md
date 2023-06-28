# Spring Boot, Firebase Auth, Redis
<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" width="200px"/>
<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/firebase/firebase-plain.svg" width="200"/>
<img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/redis/redis-original.svg" width="200px"/>

### Why
Spring Boot security offers significant benefits, yet there is a prevailing desire to avoid redundant efforts. 
To address this concern, a recommended approach leverages Firebase authentication in tandem with Spring Security, 
facilitating the expedient implementation of application security best practices.
### How
The initialization process begins by setting up Firebase and applying a custom Firebase filter within the Spring Security framework, effectively bypassing the standard email/password forms. Upon receiving an ID Token from the web application, this system proceeds to verify the token's authenticity with Firebase. If the token is not recognized, an authentication error is promptly raised. Conversely, if the token is successfully validated, the application's Security Context for that specific request is populated with pertinent details extracted from the Firebase User.

Additionally, an added layer of Redis cache is implemented, featuring a time-to-live (TTL) value of 60 minutes. In cases where the user's information is absent from the Redis cache, the key-value pair of the ID Token and corresponding user details is inserted into the Redis database. Subsequent requests bearing the same token first check the cache, retrieving the Principal object directly from the cache instead of re-serializing the token. This optimization minimizes processing overhead and improves overall performance.

### Setup
To seamlessly integrate Firebase authentication with your Spring Boot application, follow these technical steps. Begin by adding a file obtained from the Firebase console, containing the necessary configuration details, to the designated resources folder. Next, ensure the runtime environment is equipped with an appropriate environment variable that specifies the file's path. An example of this variable assignment is as follows: `classpath:firebase_config.json`.