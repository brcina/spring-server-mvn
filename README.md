spring-server-mvn
=================

Project providing a rest api of organisations which are documented with open api


### Prerequisites

* Java 21
* Maven
* Docker

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
> check https://docs.sonarsource.com/sonarqube-server/latest/user-guide/managing-tokens/ for instructions how create it

* Create your own `application-dev.yml` to overwrite the values in `application.yml` f.e.

```yml
spring:
  jpa:
    hibernate:
      ddl-auto: 'create'
```


#### Build

```bash
./mvnw clean compile
```

#### Test

```bash
./mvnw test
```

Using another profile

```bash
./mvnw test -Dspring.profiles.active=dev
```


#### Run

```bash
./mvnw spring-boot:run
```

Using another profile

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Code Quality

To run sonarqube locally one can install sonar per docker

```bash
docker volume create --name sonarqube_data
docker volume create --name sonarqube_logs
docker volume create --name sonarqube_extensions
docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true --add-host=host.docker.internal:host-gateway -p 9000:9000 sonarqube:community
```

After the docker container is running login (with the password provided) create admin user with password.
Also create a user and copy the token to the environment variable `SM_SONAR_TOKEN` see the Environment section

#### CI/CD 

This project uses jenkins pipeline see `Jenkinsfile` in the project, for development purposes one can install jenkins 
via docker

```bash
docker volume create --name jenkins_home
docker run -p 18080:8080 -p 50000:50000 --add-host=host.docker.internal:host-gateway -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts-jdk11
```

After the docker container is running open http://localhost:18080 create admin user, use default plugins installed and 
add the sonarqube plugin

The sonarqube plugin needs to be configured, add the tools:

- Jenkins must be configured with the following tools:
    - **JDK 21** (tool name: `jdk21`)
    - **Maven** (tool name: `maven`)
    - A configured **SonarQube server** (e.g., `sonar-server`) under “Manage Jenkins > Configure System”

- Add a webhook to the spring-server-mvn project in sonarqube pointing to the jenkins host `http://host.docker.internal:18080/sonarqube-webhook/`






