# CA4 Part2: Containers with Docker: Technical Report

**Author:** Inês Lemos

**Date:** 03/06/2024

**Discipline:** DevOps

**Program:** SWitCH DEV

**Institution:** Instituto Superior de Engenharia/ Instituto Politécnico do Porto

## Table of Contents

- [Introduction](#introduction)
- [DB Dockerfile](#db-dockerfile)
- [Web Dockerfile](#web-dockerfile)
- [Docker Compose](#docker-compose)
- [Tag and Push Images](#tag-and-push-images)
- [Working with volumes](#working-with-volumes)
- [Alternative solution](#alternative-solution)
- [Conclusion](#conclusion)

## Introduction

In this report, I document the process of containerizing a web application using Docker. The goal of this project was to
demonstrate the steps required to build, deploy, and manage a web application and a database within Docker containers.
Additionally, I explored an alternative deployment solution using Heroku, a cloud platform that simplifies application
deployment and management. This report covers the creation of Dockerfiles for both the database and the web application,
the use of Docker Compose to orchestrate the services, and the process of tagging and pushing Docker images to a
repository. By following these steps, I gained a deeper understanding of containerization and modern deployment
practices.

## DB Dockerfile

I started by creating a `Dockerfile` for the database service, which in this case was an H2 database server. The 
Dockerfile was placed in a `db` directory with this content:

```dockerfile
FROM ubuntu:latest

RUN apt-get update && \
    apt-get install -y openjdk-11-jdk-headless && \
    apt-get install unzip -y && \
    apt-get install wget -y

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app/

RUN wget https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar

EXPOSE 8082
EXPOSE 9092

CMD ["java", "-cp", "./h2-1.4.200.jar", "org.h2.tools.Server", "-web", "-webAllowOthers", "-tcp", "-tcpAllowOthers", "-ifNotExists"]
```

##### Explanation:

- **Base Image**: ubuntu:latest is used as the base image to ensure a clean and updated environment.
- **Install Java**: OpenJDK 11 is installed to provide the necessary Java runtime for the H2 database. Additional tools
  like unzip and wget are also installed.
- **Directory Setup**: A directory /usr/src/app is created to hold the application files.
- **Download H2 Database**: The H2 database jar file is downloaded from Maven's repository.
- **Port Exposure**: Ports 8082 and 9092 are exposed for web and TCP access respectively.
- **Start Command**: The command specified runs the H2 database server with options to allow web and TCP access and to
  create the database if it does not already exist.

## Web Dockerfile

Then, I created a `Dockerfile` in a `web` directory with this content:

```dockerfile
# Create a basic container with Java 17 and running Tomcat 10
FROM tomcat:10-jdk17-openjdk-slim

# Create a directory for the project and clone the repository there
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app/

# Update package list and install Git
RUN apt-get update && apt-get install -y git

# Clone the repository and navigate to the project directory
RUN git clone https://github.com/vapreace/devops-23-24-JPE-PSM-1231832.git .

# Navigate to the project directory
WORKDIR /usr/src/app/CA2/Part2/react-and-spring-data-rest-basic

# Change the permissions of the gradlew file to make it executable
RUN chmod +x gradlew

# Run the gradle build command
RUN ./gradlew build

# Copy the generated WAR file to the Tomcat webapps directory
RUN cp ./build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/

# State the port that our application will run on
EXPOSE 8080

# Start Tomcat automatically when the container starts
CMD ["catalina.sh", "run"]
```

##### Explanation:

- **Base Image**: tomcat:10-jdk17-openjdk-slim provides a slimmed-down version of Tomcat 10 with OpenJDK 17.
- **Directory Setup**: A directory /usr/src/app is created to house the project files.
- **Install Git**: Git is installed to enable cloning of the repository.
- **Clone Repository**: The project repository is cloned from GitHub into the working directory.
- **Build Project**: The script navigates to the specific project directory, makes the Gradle wrapper executable, and
  runs the Gradle build command to compile the project.
- **Deploy WAR File**: The resulting WAR file is copied to the Tomcat webapps directory for deployment.
- **Port Exposure**: Port 8080 is exposed to allow external access to the web application.
- **Start Command**: The command specified runs Tomcat to serve the application.

## Docker Compose

To manage both the database and web application containers, I created a `docker-compose.yml` file. This file defines the
services and how they interact with each other.

```yaml
version: '3.7'

services:
  web:
    build: ./web
    ports:
      - "8080:8080"
    networks:
      my_custom_network:
        ipv4_address: 192.168.56.10
    depends_on:
      - db

  db:
    build: ./db
    ports:
      - "8082:8082"
      - "9092:9092"
    volumes:
      - ./data:/usr/src/data-backup
    networks:
      my_custom_network:
        ipv4_address: 192.168.56.11

networks:
  my_custom_network:
    external: true
```

##### Explanation:

- **Version**: The version 3.7 is specified for compatibility with the features used.
- **Services**:
    - **Web Service**:
        - The web service is built from the ./web directory.
        - Port 8080 is mapped to the host.
        - The service is connected to my_custom_network with a static IP 192.168.56.10.
        - It depends on the db service, ensuring the database starts before the web application.
    - **DB Service**:
        - The database service is built from the ./db directory.
        - Ports 8082 and 9092 are mapped to the host.
        - A volume is mounted to ensure data persistence.
        - The service is connected to my_custom_network with a static IP 192.168.56.11.
- **Networks**:
    - my_custom_network: An external custom network is used, which needs to be created outside of this docker-compose.
      yml file to ensure proper IP address assignment.

To build and run the services defined in the `docker-compose.yml` file, I executed the following command:

```bash
docker-compose up --build
```

When the service was running, I could access the web application at http://localhost:8080 and the H2 database console
at http://localhost:8082.
Below are screenshots showing successful access to the web application and the H2 database console:

<img src="https://i.postimg.cc/76Wrygyh/Screenshot-2024-06-02-at-21-30-19.png" alt="Web Application" width="600">

<img src="https://i.postimg.cc/BvtG7hdk/Screenshot-2024-06-02-at-21-43-37.png" alt="H2 Database Console" width="600">

## Tag and Push Images

To ensure that the images were correctly tagged and pushed to the Docker Hub repository, I followed these steps:

First, I listed all the Docker images to see their IDs using the following command:

```bash
docker images
```

This allowed me to identify the Image IDs for the images I wanted to tag and push.

Next, I tagged the images with the correct repository name and tag. Using the Image IDs obtained from the previous step,
I executed the following commands:

```bash
docker tag e0cf7b8ed2d9 ineslemos/part2-web:web
docker tag ca111ad47479 ineslemos/part2-db:db
```

These commands assigned the `web` tag to the `part2-web` image and the `db` tag to the `part2-db` image, which is shown in the output of the `docker images` command.

<img src="https://i.postimg.cc/sXXC8Tqb/Screenshot-2024-06-02-at-22-10-48.png" alt="Docker images" width="600">

Before pushing the images, I logged into my Docker Hub account with the command:

```bash
docker login
```

Finally, I pushed the images to the Docker Hub repository using the following commands:

```bash
docker push ineslemos/part2-web:web
docker push ineslemos/part2-db:db
```

These commands uploaded the `part2-web` image with the `web` tag and the `part2-db` image with the `db` tag to my
repository on Docker Hub.

Below is a screenshot showing the successful push of the images to Docker Hub:

<img src="https://i.postimg.cc/ZqJSPbcb/Screenshot-2024-06-02-at-22-15-53.png" alt="Docker Hub images" width="600">

## Working with volumes

To ensure that the database file was correctly placed in the volume, I used the `docker-compose exec` command to 
access the
running db container and manually copy the necessary file:

```bash
docker-compose exec db bash
```

Inside the container shell, I copied the `h2-1.4.200.jar` file to the volume directory:

```bash
cp /usr/src/app/h2-1.4.200.jar /usr/src/data-backup
exit
```

This command sequence enters the `db` container, copies the specified file to the volume directory, and then exits the
container shell. This ensures that the database file is backed up to the volume and persisted on the host.

## Alternative solution

As an alternative deployment solution, I explored using `Heroku` to deploy the web application.

1. First, I created a Heroku account and installed the Heroku CLI on my local machine. I then logged in to my Heroku
   account using the command:

    ```bash
    heroku login
    ```

2. Next, I created a new Heroku app using the command:

    ```bash
    heroku create my-app-name
    ```

3. To deploy the web application to Heroku, I pushed the WAR file to the Heroku app's Git repository:

    ```bash
    git push heroku master
    ```

4. After the deployment was complete, I opened the web application in the browser using the command:

    ```bash
    heroku open
    ```

5. To push the images to the Heroku Container Registry, I used the following commands:

    ```bash
    heroku container:login
    heroku container:push web --app my-app-name
    heroku container:push db --app my-app-name
    ```

6. Finally, I released the images to the Heroku app using the command:

    ```bash
    heroku container:release web db --app my-app-name
    ```

7. After the release was successful, I could access the web application deployed on Heroku using the provided URL.
   In the browser, I could see the web application running successfully.

## Conclusion

Through this project, I successfully containerized a web application and a database using Docker, and orchestrated their
deployment with Docker Compose. The process involved creating Dockerfiles for each service, setting up volumes for data
persistence, and managing the services using Docker Compose. Additionally, I explored deploying the application on
Heroku as an alternative solution, highlighting the flexibility and scalability of cloud platforms for application
deployment.