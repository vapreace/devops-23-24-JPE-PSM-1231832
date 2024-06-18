# CA5: CI/CD Pipelines with Jenkins

**Author:** Inês Lemos

**Date:** 14/06/2024

**Discipline:** DevOps

**Program:** SWitCH DEV

**Institution:** Instituto Superior de Engenharia/ Instituto Politécnico do Porto

## Table of Contents

- [Introduction](#introduction)
- [Setup and prerequisites](#setup-and-prerequisites)
- [Part 1: Setting up Jenkins Pipeline for Gradle Basic Demo Application](#part-1-setting-up-jenkins-pipeline-for-gradle-basic-demo-application)
- [Part 2: Setting up Jenkins Pipeline for React and Spring Data REST Basic Application](#part-2-setting-up-jenkins-pipeline-for-react-and-spring-data-rest-basic-application)
- [Conclusion](#conclusion)

## Introduction

In this assignment, the goal was to set up CI/CD pipelines using Jenkins for two different applications developed in
previous assignments. The first application was the Gradle Basic Demo application from `CA2/Part1`, and the second
application was the React and Spring Data REST Basic Application from `CA2/Part2`.
Through this practical assignment, I aim to understand and implement the fundamental principles of CI/CD, leveraging
Jenkins' capabilities to automate the build, test, and deployment processes. This document serves as a
comprehensive guide, detailing each step involved in the pipeline setup and configuration.

## Setup and prerequisites

To successfully set up Jenkins and configure the CI/CD pipeline, it was essential to have the following tools and
software installed:

- **Git**: was necessary for version control and repository management. I verified the installation using the `git
  --version` command.
- **Docker**: was required to build and run the Docker containers. I checked the Docker version using the `docker
  --version` command.
- **Jenkins**: was the primary tool for setting up the CI/CD pipeline. I ensured to set up Jenkins on my local machine
  and verified the installation by accessing the Jenkins dashboard in a web browser.

## Part 1: Setting up Jenkins Pipeline for Gradle Basic Demo Application

The first part of the **CA5** assignment involved setting up a Jenkins pipeline for the Gradle Basic Demo application
from `CA2/Part1`.
The Jenkins pipeline was defined in the Jenkinsfile located in the project directory. Here is the content of the
Jenkinsfile:

```groovy
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out from repository'
                git branch: 'main', url: 'https://github.com/vapreace/devops-23-24-JPE-PSM-1231832.git'
            }
        }
        stage('Assemble') {
            steps {
                dir('CA2/Part1/gradle_basic_demo') {
                    echo 'Assembling...'
                    sh 'chmod +x gradlew'
                    sh './gradlew clean assemble'
                }
            }
        }
        stage('Test') {
            steps {
                dir('CA2/Part1/gradle_basic_demo') {
                    echo 'Running Tests...'
                    sh './gradlew test'
                    junit 'build/test-results/test/*.xml'
                }
            }
        }
        stage('Archive') {
            steps {
                dir('CA2/Part1/gradle_basic_demo') {
                    echo 'Archiving artifacts...'
                    archiveArtifacts artifacts: 'build/libs/*.jar', allowEmptyArchive: true
                }
            }
        }
    }
}
```

The pipeline consisted of four stages:

- **Checkout**: This stage checked out the project from the GitHub repository.
- **Assemble**: This stage assembled the Gradle project by running the `./gradlew clean assemble` command.
- **Test**: This stage ran the tests for the Gradle project by executing the `./gradlew test` command.
- **Archive**: This stage archived the built JAR file as an artifact.

To create a new pipeline job in Jenkins, I navigated to Jenkins and clicked on `New Item`. I then entered a name for
the pipeline and selected `Pipeline`. Under `Pipeline`, I chose `Pipeline script from SCM`. I selected `Git` as the
SCM and entered the repository URL. I specified `main` as the branch and set the `Script Path` to
CA5/gradle_basic_demo/Jenkinsfile. Finally, I saved the configuration.

To run the pipeline, I clicked on `Build Now` and monitored the progress in the Jenkins console output. Below is a
screenshot of the Jenkins pipeline job after a successful build:

<img src="https://i.postimg.cc/Y0JvRrf7/Screenshot-2024-06-13-at-10-47-23.png">

## Part 2: Setting up Jenkins Pipeline for React and Spring Data REST Basic Application

The second part of the **CA5** assignment involved setting up a Jenkins pipeline for the React and Spring Data REST
Basic Application from `CA2/Part2`.
The Jenkins pipeline was defined in the Jenkinsfile located in the project directory. Here is the content of the
Jenkinsfile:

```groovy
pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'docker-credentials'
        DOCKER_IMAGE = "ineslemos/jenkins-image"
        DOCKER_REGISTRY = "https://index.docker.io/v1/"
        REPO_URL = 'https://github.com/vapreace/devops-23-24-JPE-PSM-1231832.git'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out from repository...'
                git branch: 'main', url: env.REPO_URL
            }
        }
        stage('Create Dockerfile') {
            steps {
                dir('CA2/Part2/react-and-spring-data-rest-basic') {
                    echo 'Creating Dockerfile...'
                    script {
                        writeFile file: 'Dockerfile', text: '''
                        # Use the official OpenJDK image as a parent image
                        FROM openjdk:17-jdk-alpine

                        # Set the working directory in the container
                        WORKDIR /app

                        # Copy the JAR file from the host to the container
                        COPY build/libs/*.war app.war

                        # Expose the port that the application will run on
                        EXPOSE 8080

                        # Run the JAR file
                        ENTRYPOINT ["java", "-war", "app.war"]
                        '''
                    }
                }
            }
        }
        stage('Assemble') {
            steps {
                dir('CA2/Part2/react-and-spring-data-rest-basic') {
                    echo 'Assembling...'
                    sh 'chmod +x gradlew'
                    sh './gradlew clean assemble'
                }
            }
        }
        stage('Test') {
            steps {
                dir('CA2/Part2/react-and-spring-data-rest-basic') {
                    echo 'Testing...'
                    sh './gradlew test'
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }
        stage('Javadoc') {
            steps {
                dir('CA2/Part2/react-and-spring-data-rest-basic') {
                    echo 'Generating Javadoc...'
                    sh './gradlew javadoc'
                    publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'build/docs/javadoc', reportFiles: 'index.html', reportName: 'Javadoc'])
                }
            }
        }
        stage('Archive') {
            steps {
                dir('CA2/Part2/react-and-spring-data-rest-basic') {
                    echo 'Archiving...'
                    archiveArtifacts artifacts: 'build/libs/*.war', allowEmptyArchive: true
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                dir('CA2/Part2/react-and-spring-data-rest-basic') {
                    script {
                        echo 'Building Docker Image...'
                        sh 'docker info'
                        def app = docker.build("${env.DOCKER_IMAGE}:${env.BUILD_ID}", '.')
                        docker.withRegistry(env.DOCKER_REGISTRY, env.DOCKER_CREDENTIALS_ID) {
                            app.push()
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
```

The pipeline consisted of seven stages:

- **Checkout**: This stage checked out the project from the GitHub repository.
- **Create Dockerfile**: This stage created a Dockerfile for the Spring Boot application.
- **Assemble**: This stage assembled the Gradle project by running the `./gradlew clean assemble` command.
- **Test**: This stage ran the tests for the Gradle project by executing the `./gradlew test` command.
- **Javadoc**: This stage generated the Javadoc for the project.
- **Archive**: This stage archived the built WAR file as an artifact.
- **Build Docker Image**: This stage built the Docker image for the Spring Boot application and pushed it to Docker Hub.

To configure Docker credentials in Jenkins, I navigated
to `Manage Jenkins` > `Manage Credentials` > `System` > `Global credentials` (unrestricted) > `Add Credentials`.
Then, I selected `Username with password` as the kind, I entered my Docker Hub username and password and I set the ID
to docker-credentials.

To create a new pipeline job in Jenkins, I navigated to Jenkins and clicked on `New Item`. I then entered a name for
the pipeline and selected `Pipeline`. Under `Pipeline`, I chose `Pipeline script from SCM`. I selected `Git` as the
SCM and entered the repository URL. I specified `main as the branch and set the Script Path to
`CA5/react-and-spring-data-rest-basic/Jenkinsfile`. Finally, I saved the configuration.

To run the pipeline, I clicked on `Build Now` and monitored the progress in the Jenkins console output. Below is a
screenshot of the Jenkins pipeline job after a successful build:

<img src="https://i.postimg.cc/sgqKjhqJ/Screenshot-2024-06-18-at-11-18-15.png">

## Conclusion

In conclusion, this assignment provided valuable hands-on experience in setting up CI/CD pipelines using Jenkins for
two different applications. By configuring the Jenkins pipelines for the Gradle Basic Demo application and the React
and Spring Data REST Basic Application, I gained a deeper understanding of the CI/CD process and the automation of
build, test, and deployment tasks. The setup and configuration of the pipelines involved defining stages, executing
commands, archiving artifacts, and building Docker images. By automating these processes, we can significantly reduce
manual intervention, minimize errors, and accelerate the software delivery lifecycle.

[Back to top](#ca5-cicd-pipelines-with-jenkins)