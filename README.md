# CSYE 6225 - Summer 2019

## Team Information

| Name | NEU ID | Email Address |
| --- | --- | --- |
| Nithin Muvva | 001440858 | muvva.n@husky.neu.edu |
| Kiran Kumar Kathiresan | 001427809 | kathiresan.k@husky.neu.edu |
| Akhil Prasad | 001449445 | prasad.ak@husky.neu.edu |


## Technology Stack
Operating System - Linux (Ubuntu)
Programming Language - Java
Relational Database - MySQL
Backend Framework - Spring and Hibernate

## Build Instructions
1. Build the Application using Maven Build

## Deploy Instructions
1. Run the "LmsAppApplication.java" as Java Application
2. When the Application successfully deploys run Postman and go to "localhost:8080/user/register" with a post request
3. You will need to add email address and password in the Body header in Json Format
4. After successful creation of the user, an authorization token will be generated in the header. Copy the authorization header.
5. For logging in, use "localhost:8080/" address with a get request and authorization header
6. When loging in use Basic Auth in postman with the username and password registered as Authentication to get logged in
7. Add the authorization header and hit the rest of uri's for the book rest service appropriately

## Running Tests
1. Run the "LmsAppApplicationTests.java" file as a JUnit Test

## CI/CD



