# Hotel Reservation System - Customer Service

[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=arghyagiri_hrs-customer-service)](https://sonarcloud.io/summary/new_code?id=arghyagiri_hrs-customer-service)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

[Spring Boot](http://projects.spring.io/spring-boot/) based app.

### Business Workflows:

* 1. Customer Registration
* 2. Update Profile
* 3. View Hotel Listings
#### Design Patterns & Annotations: 
* Use API Gateway to route requests to Customer Service.

#### Interactions: 
* Will interact with Hotel Management Service for viewing listings.

## Requirements

For building and running the application you need:

- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method
in the `com.tcs.training.hotelRoom.CustomerApplication` class from your IDE.

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

### 7. Notification Service send e-mail to hotelRoom registered email address

![img.png](readme/img5.png)



## Reservation Flow

### 1. Create Amenities in Hotel Management Service
![img.png](readme/img0.png)

### 2. Create Room Listing in Hotel Management Service
![img_1.png](readme/img1_.png)

### 3. View listing of rooms[from Hotel Management Service]
![img_2.png](readme/img2_.png)

### 4. Select room and select reservation dates
![img_3.png](readme/img3_.png)

### 5. Provide payment details
![img_4.png](readme/img4_.png)

### 6. If payment failed, room reservation is not completed and room is still shown as available
![img_5.png](readme/img5_.png)
![img_6.png](readme/img6_.png)

### 7. If payment is successful, room is booked and notification is sent
![img_7.png](readme/img7.png)
![img_8.png](readme/img8.png)


## Reservation Cancellation Flow

### 1. User login to check bookings
![img.png](img.png)

### 2. Booking list is shown
![img_1.png](img_1.png)

### 3. Cancel reservation
![img_2.png](img_2.png)

### 4. Cancellation notification sent




## Copyright

Released under the Apache License 2.0. See
the [LICENSE](https://github.com/arghyagiri/microservice-e2/blob/main/LICENSE) file.

