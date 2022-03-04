# Read Me

## How to build
The project uses Java, Spring Boot, Spring Web, and Maven to build

* Download and install Java 11
* Build the project <code>mvn clean package</code>
* Run the project <code>java -Duser.timezone=UTC -Xms128m -Xmx128m -Xss1m -jar target/issue-tracker-0.0.1-SNAPSHOT.jar</code>

## How to access
The project uses in-memory H2 database and Apache Tomcat

Base URL = http://localhost:8080/issue-tracker

## APIs

| Description      | Method | Path                     |
|------------------|--------|--------------------------|
| Create Developer | POST   | /developers              |
| Update Developer | PUT    | /developers/{id}         |
| Get Developer    | GET    | /developers/{id}         |
| Delete Developer | DELETE | /developers/{id}         |
| Get Developers   | GET    | /developers              |
| Create Story     | POST   | /stories                 |
| Update Story     | PUT    | /stories/{id}            |
| Get Story        | GET    | /stories/{id}            |
| Delete Story     | DELETE | /stories/{id}            |
| Assign Developer | POST   | /stories/{id}/developers |
| Create Bug       | POST   | /bugs                    |
| Update Bug       | PUT    | /bugs/{id}               |
| Get Bug          | GET    | /bugs/{id}               |
| Delete Bug       | DELETE | /bugs/{id}               |
| Assign Developer | POST   | /bugs/{id}/developers    |
| Get Plan         | GET    | /plan                    |

You can find example Postman collection in repository 