spring-server-mvn
=================

Project providing a rest api of organisations which are documented with open api


### Prerequisites

* Java 21
* Maven >= 3.9.6
* Docker >= 28.2.2

### Development

#### Docker Postgres

```bash
#### Prerequisites
docker volume create pg_server_spring
# or with mysql_fdw 
docker run --name pg_server_spring --add-host=host.docker.internal:host-gateway -p 54320:5432  -v pg_server_spring:/var/lib/postgresql/data -e POSTGRES_PASSWORD=postgres -d postgres:17.5
```

Connect with a db client

```postgresql
CREATE schema pg17_server_spring authorization postgres;
```

#### Environment

* Create your own `setenv.sh`
```bash
export SM_SONAR_TOKEN=<CHANGEME>
export SM_SONAR_URL=http://localhost:9000
export SM_DB_URL=jdbc:postgresql://localhost:54320/postgres?currentSchema=pg17_server_spring
export SM_DB_USERNAME=postgres
export SM_DB_PASSWORD=postgres
```

* Create your own `application-dev.yml` to overwrite the values in `application.yml` f.e.

```yml
spring:
  jpa:
    hibernate:
      ddl-auto: 'create'
```
This will f.e. create all the tables on the postgres db 

> when started with the dev profile the application inserts some faked data in see LoadDatabase 


#### Build

```bash
./mvnw clean compile
```

#### Test

The project uses the h2 database in the test 

```bash
./mvnw test
```

#### Run

```bash
./mvnw spring-boot:run
```

Using the dev profile which will also insert faked organisations see the LoadDatabase class

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Dependencies

The project keeps the current dependencies of the project in the deps.txt please execute it when adding new maven 
dependencies

```bash
./mvnw dependency:tree -DoutputFile=`pwd`/deps.txt
```

#### Open API

The project uses open api for documenting the rest api one can reach the documentation
http://localhost:8080/api-docs resp. for testing the ui http://localhost:8080/api-ui-docs.html

#### Code Quality

To run sonarqube locally one can install sonar per docker

```bash
docker volume create --name sonarqube_data
docker volume create --name sonarqube_logs
docker volume create --name sonarqube_extensions
docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true --add-host=host.docker.internal:host-gateway -p 9000:9000 sonarqube:community
```
> See https://hub.docker.com/_/sonarqube

After the docker container is running login (with the password provided) create admin user with password.
Also create a user and copy the token to the environment variable `SM_SONAR_TOKEN` see the Environment section

> check https://docs.sonarsource.com/sonarqube-server/latest/user-guide/managing-tokens/ for instructions how create it

#### CI/CD 

This project uses jenkins pipeline see `Jenkinsfile` in the project, for development purposes one can install jenkins 
via docker

```bash
docker volume create --name jenkins_home
docker run -p 18080:8080 -p 50000:50000 --add-host=host.docker.internal:host-gateway -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts-jdk11
```

> See `https://hub.docker.com/_/jenkins`

After the docker container is running open http://localhost:18080 create admin user, use default plugins installed and 
add the sonarqube plugin

The sonarqube plugin needs to be configured, add the tools:

- Jenkins must be configured with the following tools:
    - **JDK 21** (tool name: `jdk21`)
    - **Maven** (tool name: `maven`)
    - A configured **SonarQube server** (e.g., `sonar-server`) under “Manage Jenkins > Configure System” 

- Add a webhook to the spring-server-mvn project in sonarqube pointing to the jenkins host `http://host.docker.internal:18080/sonarqube-webhook/`


Spring Boot Documentation (HELP.md)
-----------------------------------

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.3/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.3/maven-plugin/build-image.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.5.3/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.3/reference/web/servlet.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.






