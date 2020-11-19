# SteamLeaderboards-API
This Java Program works through a framework called Spring-Boot which allows our application to intercept and respond to HTTP requests. This project was created as an introduction into the framework, you will find a full fleet of services with this project including; a user system, a game logging system, and the bulk of the API which is a leaderboard system. The purpose of our project is to provide a third-party data delivery service using data from Steam services, yes I'm referring to the Steam that PC gamers buy games through. Steam allows for users to create public profiles to connect with others and to flex how much of our lives they have waisted. My API will connect those public profiles of yourself and friends to show who really is the boss!
### Users
As mentioned above this API service requires its own user management system, it contains ways for users to create and manage their own identity on our service. A user profile will be needed to login through the RESTful interface to obtain JWT tokens to access protected data on the server, for example once a user registers themselves by POSTing the required account data they can modify their own friends at any time by accessing that resource through the API. Check documentation for more information.
### Leaderboards 
The leaderboards that this API produces is hand-picked data from the Steam Public API for each profile which is formatted from one or more endpoint calls to the Steam services. The most important take-away from this endpoint is that it is PUBLIC you can use it without having an account all you must do is pass any SteamID into the URL request to get back the public information. If the SteamID that is passed into the service links to an account then a JSON array will be returned containing that users friends data.

If any user does not contain a public Steam profile then the application will not be able to format but a very minimal amount of data.

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
* [Lambok - Automated Class Method Generation](https://projectlombok.org/features/all) - [Maven](https://mvnrepository.com/artifact/org.projectlombok/lombok)
* [JSON Parser JAVADOC](https://javadoc.io/doc/net.minidev/json-smart/latest/index.html) - [Maven](https://mvnrepository.com/artifact/net.minidev/json-smart)
### JDBC
* [MySQL Connector Maven](https://mvnrepository.com/artifact/mysql/mysql-connector-java)
* [Java Persistence Integration Maven](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa)
### Security
* [Java JSON Web Token (JWT) Github](https://jwt.io/) - [Maven](https://mvnrepository.com/artifact/com.auth0/java-jwt)

## Database Setup
The Database setup is handled almost entirely by Spring-Boot, by this I mean that once you have created a new and blank Database called whatever you want and make sure it is either on the local machine(localhost) or available on a public address somewhere. To link the new Database to the program simply edit the following lines inside [src/main/resources/application.properties](src/main/resources/application.properties)
```properties
#Database connection URL
spring.datasource.url=jdbc:mysql://{URL-ADDRESS}:{PORT}/{DATABASE-NAME}
#Database login user name
spring.datasource.username=ADMIN
#Database login password
spring.datasource.password=PASSWORD
```
No preliminary files or setup is required for the Database because Spring-Boot will handle table creation. Do not try to generate Database table manually because there is a special relation between the user table and friend_list that is created by linking ID's of the two inside of user_friend_list. See the image below for ERD.

![Image of Database relation in typical ERD fashion](resources/images/SteamLeaderboards-ERD.png?raw=true)

## Running the Project
Executing the project can be done in two ways, the first is by initializing using Maven which the second produces a traditional Jar file.
### Maven
If you have Maven installed on your machine you can navigate to the root project directory with this README file and execute the following. Remember to follow the above Database setup procedures first.
```sh
mvn clean spring-boot:run
```
You can also use the built in Maven wrapper and execute the project by following this command.
```sh
./mvnw clean spring-boot:run
```
### Building Jar File
To generate a proper Jar file you can use the above Maven run commands which will generate a Jar under a folder "target/" from the root directory. This can be avoided and you can use the Maven wrapper like above or the Maven resources in the OS to build using following command.
```sh
mvn clean package
```

## CI/CD Integration
The delivery of our application requires that we can pass it through a Jenkins pipeline to automate the building, testing and version release. Here at the company I work for we use Jenkins deployed on a Kuberneties service which can handle the containerization of the independent steps in our Jenkins. Under the [ci/agents](ci/agents) you will find the pods file which informs our Jenkins server which Docker images it will need to download, as well as the Jenkinsfile that contains our pipeline steps.
