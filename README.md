# Hotel Reservation System - Customer Service

[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=arghyagiri_notification-service)](https://sonarcloud.io/summary/new_code?id=arghyagiri_notification-service)

[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

[Spring Boot](http://projects.spring.io/spring-boot/) based app.

## Requirements

For building and running the application you need:

- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method
in the `com.tcs.training.customer.CustomerApplication` class from your IDE.

Alternatively you can use
the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html)
like so:

```shell
mvn spring-boot:run
```

## Local Application Urls

### Base Url

http://localhost:8084

### Swagger UI

http://localhost:8084/swagger-ui/index.html

## Application endpoints:

* Retrieve all customers: ```GET /all-customers```
* Customer Registration: ```GET /signup```

## Sign Up Flow

### 1. Customer Sign Up Page
![img.png](readme/img.png)

### 2. Customer Data validation
![img.png](readme/img1.png)

### 3. Data Inputs
![img.png](readme/img2.png)

### 4. Successful registration
![img.png](readme/img3.png)

### 5. Notification Process Event trigger
![img.png](readme/img4.png)

### 6. Notification Service consumes and process this event
![img.png](readme/img6.png)

### 7. Notification Service send e-mail to customer registered email address

![img.png](readme/img5.png)


## Copyright

Released under the Apache License 2.0. See
the [LICENSE](https://github.com/arghyagiri/microservice-e2/blob/main/LICENSE) file.

