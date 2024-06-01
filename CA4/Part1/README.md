# CA4 Part1: Containers with Docker: Technical Report

**Author:** Inês Lemos

**Date:** 01/06/2024

**Discipline:** DevOps

**Program:** SWitCH DEV

**Institution:** Instituto Superior de Engenharia/ Instituto Politécnico do Porto

## Table of Contents
- [Table of Contents](#table-of-contents)
- [Introduction](#introduction)
- [Environment Setup](#environment-setup)
- [Dockerfile - version 1](#dockerfile---version-1)
- [Conclusion](#conclusion)

## Introduction

The primary goal of this assignment is to practice using Docker by creating Docker images and running containers with a chat application. This application was originally developed in CA2 and is available in a Bitbucket repository. By containerizing the chat server, we can ensure that it runs consistently across different environments. This assignment is divided into two versions:

- Building the chat server "inside" the Dockerfile.
- Building the chat server on the host computer and then copying the JAR file into the Dockerfile.
In this report, I document the steps I took to achieve these objectives, including the environment setup, the content of the Dockerfiles, and the process of building and running the Docker images.

## Environment Setup

To start working with Docker and the chat server from CA2, I first ensured that Docker was installed on my system.
Beside that, it is also necessary to have the chat server repository from Bitbucket. This repository contains the
basic Gradle application developed in CA2. It's possible to clone the repository using the following command:

```bash
git clone https://bitbucket.org/pssmatos/gradle_basic_demo.git
```

## Dockerfile - version 1

Here is what I did to set up and run the chat server inside a Docker container:

1. I made sure that the docker was running on my system.


2. I navigated to the directory where the Dockerfile was located.


3. Here is the content of the Dockerfile I used:

   ```dockerfile
    # Use a Gradle image with JDK 17 to build the application
    FROM gradle:jdk17 AS builder

    # Set the working directory for the build
    WORKDIR /CA4/Part1/V1
    
    # Clone the repository
    RUN git clone https://bitbucket.org/pssmatos/gradle_basic_demo.git
    
    # Set the working directory to the cloned repository
    WORKDIR /CA4/Part1/V1/gradle_basic_demo
    
    # Ensure the Gradle wrapper has the correct permissions
    RUN chmod +x gradlew
    
    # Build the application
    RUN ./gradlew build
    
    # Use a slim JRE image for the runtime
    FROM eclipse-temurin:17-jre
    
    # Set the working directory
    WORKDIR /app
    
    # Copy the built JAR file from the builder stage
    COPY --from=builder /CA4/Part1/V1/gradle_basic_demo/build/libs/basic_demo-0.1.0.jar /app/basic_demo-0.1.0.jar
    
    # Expose the port the server will run on
    EXPOSE 59001
    
    # Set the entry point to run the server
    ENTRYPOINT ["java", "-cp", "/app/basic_demo-0.1.0.jar", "basic_demo.ChatServerApp", "59001"]
   ```
   The Dockerfile is a script that contains a series of instructions on how to build a Docker image for the chat server.
   It starts by using a Gradle image with JDK 17 to clone and build the project from a Bitbucket repository. Once the
   build is complete, it switches to a slim JRE image to create a smaller, production-ready container. The built JAR
   file is copied from the builder stage to the final image, and the server is configured to run on port 59001.


4. I built the Docker image using the following command:
   ```bash
    docker build -t ineslemos/chat-server:version1 .
    ```

5. To ensure that the image was built correctly, I ran the following command:
   ```bash
    docker images
    ```
   Below is the output of the command:

   <img src="https://i.postimg.cc/zXh7TS6S/Screenshot-2024-06-01-at-15-29-52.png" alt="Docker images" width="600"/>

6. I ran the Docker container using the following command:
    ```bash
    docker run -p 59001:59001 ineslemos/chat-server:version1
    ```
   The `-p` flag is used to map the host port to the container port. In this case, port 59001 on the host is mapped to
   port 59001 on the container. Below is the output of the command:

   <img src="https://i.postimg.cc/g2PDsLP9/Screenshot-2024-06-01-at-15-35-07.png" alt="Docker run" width="600">

7. I changed to the directory where the chat client was located and ran the following commands to build and run the chat
   client:
    ```bash
    ./gradlew build
    ./gradlew runClient
    ```

8. I connected to the chat server using the chat client.
   Below is the output of the chat client connected to the chat server running in the Docker container with a sample
   message:

   <img src="https://i.postimg.cc/7ZMNWPx5/Screenshot-2024-06-01-at-17-45-38.png" alt="Chat Clients" width="500">

   In the terminal where the Docker container was running, I could see the entrance and exit of new clients in the chat
   server.

   <img src="https://i.postimg.cc/jjxh0V5Z/Screenshot-2024-06-01-at-17-48-31.png" alt="Chat Server" width="600">


9. Finally, I pushed the Docker image to Docker Hub with the following command:
    ```bash
    docker push ineslemos/chat-server:version1
    ```

   In the Docker Hub repository, the image was successfully pushed and available for use, as shown in the image below:

   <img src="https://i.postimg.cc/fb8fMHSZ/Screenshot-2024-06-01-at-18-49-22.png" alt="Docker Hub image" width="500">

## Dockerfile - version 2

For the second version, I built the chat server on my host machine and then copied the JAR file into the Docker image.
Here is what I did:

1. First, I navigated to the project directory and ran the Gradle build command to generate the JAR file:

    ```bash
    ./gradlew build
    ```
   This generated the basic_demo-0.1.0.jar file in the build/libs directory.


2. I then navigated to the directory where my Dockerfile for version 2 was located:

3. Here is the content of the Dockerfile I used:

   ```dockerfile
   # Use a Gradle image with JDK 21 to build the application
   FROM gradle:jdk21 AS builder
   
   # Set the working directory
   WORKDIR /app
   
   # Copy the JAR file from the host machine to the Docker image
   COPY CA2/Part1/gradle_basic_demo/build/libs/basic_demo-0.1.0.jar /app/basic_demo-0.1.0.jar
   
   # Expose the port the server will run on
   EXPOSE 59001
   
   # Set the entry point to run the server
   ENTRYPOINT ["java", "-cp", "/app/basic_demo-0.1.0.jar", "basic_demo.ChatServerApp", "59001"]
   ```

   This Dockerfile is simpler than the previous one because it doesn't need to clone the repository or build the
   project. It copies the JAR file generated by the Gradle build command on the host machine into the Docker image and
   configures the server to run on port 59001.

4. I built the Docker image using the following command:
    ```bash
    docker build -t ineslemos/chat-server:version2 -f Dockerfile ../../..
    ```
    
5. To ensure that the image was built correctly, I ran the following command:
    ```bash
    docker images
    ```
   
    Below is the output of the command:

   <img src="https://i.postimg.cc/135Kbh4M/Screenshot-2024-06-01-at-18-26-46.png" alt="Docker images" width="600"/>
   
6. I ran the Docker container using the following command:
    ```bash
    docker run -p 59001:59001 ineslemos/chat-server:version2
    ```
   
    Below is the output of the command:

   <img src="https://i.postimg.cc/cHMgsCb3/Screenshot-2024-06-01-at-18-40-27.png" alt="Docker run" width="600"/>

7. I changed to the directory where the chat client was located and ran the following commands to run the chat client:
    ```bash
    ./gradlew runClient
    ```

    I opened two clients in two different terminals to test the chat functionality. Below is the output of the chat:
    <img src="https://i.postimg.cc/Yqy42Jbd/Screenshot-2024-06-01-at-18-43-29.png" alt="Chat Clients" width="500"/>

   In the terminal where the Docker container was running, I could see the entrance and exit of new clients in the chat:

   <img src="https://i.postimg.cc/wjy110rg/Screenshot-2024-06-01-at-18-44-09.png" alt="Chat Server" width="600"/>

8. Finally, I pushed the Docker image to Docker Hub with the following command:
    ```bash
    docker push ineslemos/chat-server:version2
    ```

    In the Docker Hub repository, the image was successfully pushed and available for use, as shown in the image below:

    <img src="https://i.postimg.cc/mr7hwhRn/Screenshot-2024-06-01-at-18-48-57.png" alt="Docker Hub image" width="500"/>

## Conclusion

In this assignment, I successfully containerized a chat server application using Docker. By following the outlined steps, I was able to create two versions of the Docker image. The first version involved building the application within the Dockerfile itself, while the second version relied on building the application on the host machine and copying the resultant JAR file into the Docker image. Both approaches demonstrated the flexibility and power of Docker in managing and deploying applications consistently across various environments.

[Back to top](#ca4-part1-containers-with-docker-technical-report)