# SteamLeaderboards-API
This Java Program works through a Framework called 'Spring-Boot' which allows our Java application to intercept and respond to HTTP requests. This project was created as an introduction into the Framework, you will find a full fleet of Services with this project including; a user system, a game logging system, and the bulk of the API which is a leaderboard system.

## Links
### Documentation
* [SwaggerHub API Documentation](https://app.swaggerhub.com/apis/ARTechnology/steam-leaderboards/1.0.0)
### Guides
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

## Libraries
### Spring-Boot/Java
* [Spring-Boot Maven](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/maven-plugin/reference/html/)
* [Lambok - Automated Class Method Genetation](https://projectlombok.org/features/all) - [Maven](https://mvnrepository.com/artifact/org.projectlombok/lombok)
* [JSON Parser JAVADOC](https://javadoc.io/doc/net.minidev/json-smart/latest/index.html) - [Maven](https://mvnrepository.com/artifact/net.minidev/json-smart)
### JDBC
* [MySQL Connector Maven](https://mvnrepository.com/artifact/mysql/mysql-connector-java)
* [Java Persistence Integration Maven](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa)
### Security
* [Java JSON Web Token (JWT) Github](https://jwt.io/) - [Maven](https://mvnrepository.com/artifact/com.auth0/java-jwt)

## Databse Setup
Our Database setup is handeled almost entirly by Spring-Boot, by this I mean that once you have created a new and blank Database called whatever you want and make sure it is either on the local machine(localhost) or available on a public address somewhere. To link the new Database to the program simply edit the following lines inside [src/main/resources/application.properties](src/main/resources/application.properties)

```properties
#Database connection URL
spring.datasource.url=jdbc:mysql://{URL-ADDRESS}:{PORT}/{DATABASE-NAME}
#Database login user name
spring.datasource.username=ADMIN
#Database login password
spring.datasource.password=PASSWORD
```

No preliminary files or setup is requred for the Database because Spring-Boot will handle table creation. Do not try to generate Database table manually because there is a special relation between the user table and friend_list that is created by linking ID's of the two inside of user_friend_list. See the image below for ERD.

![Image of Database relation in typical ERD fashion](resources/images/SteamLeaderboards-ERD.png?raw=true)

## Running the Project

## CI/CD Integration

