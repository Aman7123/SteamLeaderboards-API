[![BUILDER](https://github.com/Aman7123/SteamLeaderboards-API/actions/workflows/BUILDER.yml/badge.svg?branch=master)](https://github.com/Aman7123/SteamLeaderboards-API/actions/workflows/BUILDER.yml)

# SteamLeaderboards-API
### Table of Contents
* [Introduction](#Introduction)
* Setup *"How to"*
  * [Database Setup](#Database-Setup)
  * [Run Spring-Boot](#Running-the-Project)
* Workflow
  * [Jenkins Pipeline](#CICD-Integration)
* Help
  * [Documentation Examples](#Links)
  * [Setup Libraries and Examples](#Libraries)

## Introduction

This Java Program works through a framework called Spring-Boot which allows our application to intercept and respond to HTTP requests. This project was created as an introduction into the framework, you will find a full fleet of services with this project including; a user system, a game logging system, and the bulk of the API which is a leaderboard system. The purpose of our project is to provide a third-party data delivery service using data from Steam services, yes I'm referring to the Steam that PC gamers buy games through. Steam allows for users to create public profiles to connect with others and to flex how much of our lives they have waisted. My API will connect those public profiles of yourself and friends to show who really is the boss!

### Users

As mentioned above this API service requires its own user management system, it contains ways for users to create and manage their own identity on our service. A user profile will be needed to login through the RESTful interface to obtain JWT tokens to access protected data on the server, for example once a user registers themselves by POSTing the required account data they can modify their own friends at any time by accessing that resource through the API. Check documentation for more information.

### Leaderboards 

The leaderboards that this API produces is hand-picked data from the Steam Public API for each profile which is formatted from one or more endpoint calls to the Steam services. The most important take-away from this endpoint is that it is PUBLIC you can use it without having an account all you must do is pass any SteamID into the URL request to get back the public information. If the SteamID that is passed into the service links to an account then a JSON array will be returned containing that users friends data.

If any user does not contain a public Steam profile then the application will not be able to format but a very minimal amount of data.

## Setup

### Database Setup

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

### Running the Project

Executing the project can be done in two ways, the first is by initializing using Maven which the second produces a traditional Jar file. Before attempting to run the program some setup must be done inside of the [src/main/resources/application.properties](src/main/resources/application.properties), you can follow the guides [Database Setup](#Database-Setup), [Steam API Key](#Steam-Connection) and [Security Setup](#Security).

### Build with Maven

If you have Maven installed on your machine you can navigate to the root project directory with this README file and execute the following. Remember to follow the above Database setup procedures first.
```sh
mvn clean spring-boot:run
```
You can also use the built in Maven wrapper and execute the project by following this command.
```sh
./mvnw clean spring-boot:run
```

### Building Jar File

To generate a contained Jar file of the application make sure you are in the root repo directory and execute one of the commands below in a terminal. You should witness output about where its at in the process followed at the end by a "BUILD SUCCESS" or "BUILD FAILURE". The final Jar file will be located inside of a new "target/" directory inside of the root repo.

```sh
mvn -B -DskipTests clean package
```
```sh
./mvnw -B -DskipTests clean package
```

### Creating a Docker Image

To build a container that can execute the application from a safe location you can use my supplied [Dockerfile](Dockerfile) to do so. You should follow the [Database Setup](#Database-Setup), [Steam API Key](#Steam-Connection) and [Project Security](#Security) guide first to better understand some of these arguments.

```Dockerfile
CMD [ "java", \
        "-jar", \
        "SteamLeaderboards-API.jar", \
        "--spring.datasource.url=jdbc:mysql://{URL-ADDRESS}:{PORT}/{DATABASE-NAME}", \
        "--spring.datasource.username=ADMIN", \
        "--spring.datasource.password=PASSWORD", \
        "--com.aaronrenner.apikey=API_KEY_SECRET", \
        "--com.aaronrenner.tokenkey=TOKEN_KEY_SECRET"]
```

## CI/CD Integration

The delivery of our application requires that we can pass it through a Jenkins pipeline to automate the building, testing and version release. To replicate this setup it will be important to have your Jenkins be allowed to create Docker containers from the pipeline. Under the [ci/agents](ci/agents) you will find the [ci/agents/pods.yaml](ci/agents/pods.yaml) file which informs our Jenkins server which Docker images it will need to download, as well as the Jenkinsfile that contains our pipeline steps.

In this Pipeline you should replace these environment variables with those created above in this README

```groovy
environment {
    DB_USERNAME = "ADMIN"
    DB_PASSWORD = "PASSWORD"
    API_KEY = "API_KEY"
    TOKEN_KEY = "TOKEN_KEY"
}
```

## Links
### Documentation
* [SwaggerHub API Documentation](https://app.swaggerhub.com/apis/ARTechnology/steam-leaderboards/1.0.0)
### Guides
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
### Steam Connection
* [Obtaining an Steam Web API Key](https://steamcommunity.com/dev)

Please make sure you have observed the section of code below from [src/main/java/com/aaronrenner/SteamAPI/services/LeaderboardServiceIMPL.java](src/main/java/com/aaronrenner/SteamAPI/services/LeaderboardServiceIMPL.java).

```java
@Value("${com.aaronrenner.apikey}")
private String apikey;
```

After you have observed the location of the Api Key String and its use please fill your key into its location in [src/main/resources/application.properties](src/main/resources/application.properties)

```properties
com.aaronrenner.apikey=API_KEY
```

## Libraries

### Jump To
* [Required Dependencies](#spring-boot-required)
* [Lombok](#lombok)
* [JSON-Smart](#json-smart)
* [MySQL](#mysql)
* [JPA](#jpa)
* [Security](#security)

### Spring-Boot Required
* [Spring-Boot Maven](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/maven-plugin/reference/html/)
```pom
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>org.junit.vintage</groupId>
            <artifactId>junit-vintage-engine</artifactId>
        </exclusion>
    </exclusions>
</dependency>
        <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```
### Lombok
* [Lombok - Automated Class Method Generation](https://projectlombok.org/features/all) - [Maven](https://mvnrepository.com/artifact/org.projectlombok/lombok)
```pom
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```
### JSON-Smart
* [JSON Parser JAVADOC](https://javadoc.io/doc/net.minidev/json-smart/latest/index.html) - [Maven](https://mvnrepository.com/artifact/net.minidev/json-smart)
```pom
<dependency>
    <groupId>net.minidev</groupId>
    <artifactId>json-smart</artifactId>
</dependency>
```
### MySQL
* [MySQL Connector Maven](https://mvnrepository.com/artifact/mysql/mysql-connector-java)
```pom
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```
### JPA
* [Java Persistence API Maven](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa)
```pom
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```
### Security
* [Java JSON Web Token (JWT) Github](https://jwt.io/) - [Maven](https://mvnrepository.com/artifact/com.auth0/java-jwt)
```pom
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <!--You should check the 'Maven' hyperlink above for the most recent version-->
    <version>3.3.0</version>
</dependency>
```

After you have imported these libraries into the project navigate to the file [src/main/java/com/aaronrenner/SteamAPI/services/LoginServiceIMPL.java](src/main/java/com/aaronrenner/SteamAPI/services/LoginServiceIMPL.java) you can see a location of the Secret Token Key and even modify it.

```java
@Value("${com.aaronrenner.tokenkey}")
private String tokenkey = "";
```

The Secret Key is located inside [src/main/resources/application.properties](src/main/resources/application.properties)

```properties
com.aaronrenner.tokenkey=TOKEN_KEY
```
