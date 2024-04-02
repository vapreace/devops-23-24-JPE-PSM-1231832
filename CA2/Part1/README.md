# CA2 Part1: Build Tools with Gradle: Technical Report

**Author:** Inês Lemos

**Date:** 18/03/2024

**Discipline:** DevOps

**Program:** SWitCH DEV

**Institution:** Instituto Superior de Engenharia/ Instituto Politécnico do Porto

## Table of Contents
- [Introduction](#introduction)
- [Environment Setup](#environment-setup)
- [Gradle Basic Demo](#gradle-basic-demo)
- [Add a new task](#add-a-new-task)
- [Add a unit test](#add-a-unit-test)
- [Add a new task of type Copy](#add-a-new-task-of-type-copy)
- [Add a new task of type Zip](#add-a-new-task-of-type-zip)
- [Conclusion](#conclusion)

## Introduction
This report outlines the work completed in the **Build Tools with Gradle** assignment for the DevOps course, focusing on practical applications of Gradle. The assignment is structured into several key tasks designed to provide hands-on experience with Gradle, starting from the initial setup to more advanced functionalities like task creation and unit testing.

Following the initial **Environment Setup**, the report details the implementation of the **Gradle Basic Demo**, a multithreaded chat server, showcasing the process of building, launching, and connecting multiple clients to the server. This section serves as a practical demonstration of Gradle's capabilities in managing a real-world application.
The report then describes how a new Gradle task was added to the project in the **Add a new task** section, showcasing the process of extending Gradle's capabilities to meet specific project requirements.
In the **Add a unit test** section, the focus shifts to enhancing the project's reliability through testing, highlighting how Gradle was used to integrate unit tests into the build process. The subsequent sections, **Add a new task of type Copy** and **Add a new task of type Zip**, demonstrate the use of Gradle for file manipulation tasks, essential for project maintenance and distribution.

The report concludes with the Conclusion section, which reflects on the learning outcomes of the assignment, the challenges faced, and the practical skills acquired in using Gradle for software development tasks.

## Environment Setup
The initial step involved creating a new directory for this new assignment, **/CA2/Part1** followed by cloning the example application from the provided Bitbucket repository. This repository came with a build.gradle file and the Gradle Wrapper, ensuring a consistent environment. After installation, I verified Gradle's setup by running gradle -v in the command line.

I then integrated the project into my Integrated Development Environment (IDE) which supports Gradle, making use of its features.To validate the project's configuration and readiness, I executed a basic Gradle build. This build step was crucial for verifying that all components were correctly set up and that the project was ready for further development.

These initial steps established a solid foundation, enabling me to smoothly proceed with the subsequent tasks outlined in the assignment.

## Gradle Basic Demo
The Gradle Basic Demo provided a practical exercise in working with a multi-threaded chat server capable of handling multiple clients simultaneously.

**Build Process:**

To prepare the demo for execution, I executed `./gradlew build` from the root directory of the project. This command compiled the source code and packaged it into an executable .jar file. The screenshot below confirms the successful build process.

<img  src="https://i.postimg.cc/1tPzm4JX/Screenshot-2024-03-18-at-19-07-42.png"  width="1000">

**Server Startup:**

Next, I launched the chat server using the command `java -cp build/libs/basic_demo-0.1.0.jar basic_demo.ChatServerApp 59001`. The following screenshot shows the server running and waiting for client connections.

<img  src="https://i.postimg.cc/QxMHK9sW/Screenshot-2024-03-18-at-19-09-15.png"  width="1000">

**Client Connections:**

For the client side, I initiated connections to the chat server by executing `/gradlew runClient`, ensuring each client connected to localhost on port 59001. The build.gradle file was set up to allow easy modifications for different connection settings. To illustrate the server's ability to manage multiple clients, I launched several client instances from different terminals.
The screenshots below capture the active chat sessions, demonstrating the multi-client functionality in action.

<img  src="https://i.postimg.cc/0N7Q17Gh/Screenshot-2024-03-18-at-17-30-29.png"  width="600">

<img  src="https://i.postimg.cc/SxBsPg9W/Screenshot-2024-03-18-at-17-33-03.png"  width="600">


## Add a new task
I introduced a `runServer` task to the `build.gradle` file to enhance our development workflow by simplifying the server startup process. This new task, named `runServer`, allows us to initiate the chat server directly through a Gradle command, eliminating the need for manual command line inputs each time we want to start the server.

The `runServer` task is defined as follows in the `build.gradle file, setting its type as JavaExec to execute Java applications, specifying its dependency on the classes task to ensure that all necessary classes are compiled before the server starts, and configuring it to launch the ChatServerApp main class on port 59001:
```java
task runServer(type: JavaExec, dependsOn: classes) {
    group = "DevOps"
    description = "Launches a chat server that listens on port 59001"

    classpath = sourceSets.main.runtimeClasspath

    mainClass = 'basic_demo.ChatServerApp'

    args '59001'
}
```

To test the functionality of this addition, I executed the task using `./gradlew runServer` in the command line. The immediate feedback from the terminal confirmed the task's successful execution, as shown in the screenshot below, demonstrating the server's active status. This enhancement has notably streamlined our development process, making it more efficient by reducing the steps required to get the server up and running.

<img  src="https://i.postimg.cc/NfPL6sfC/Screenshot-2024-03-18-at-17-50-30.png"  width="600">

This integration into our Gradle build script not only highlights the adaptability of Gradle as a build tool but also contributes significantly to our project's overall productivity by automating routine tasks.


## Add a unit test

I added a unit test to ensure the App class's functionality. The test was placed in a new directory, `src/test/java/basic_demo`, within a file named `AppTest.java`.
This test checks if the App class provides a non-null greeting message, which is a basic but crucial feature.

To ensure the test environment was properly configured, I added the JUnit dependency to the `build.gradle file, which is essential for running the unit tests:
```java
testImplementation 'junit:junit:4.12'
```

This inclusion guarantees that the project recognizes and executes JUnit tests without hitches.
Here's the content of AppTest.java, which contains the test case:

```java
package basic_demo;

import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {
    @Test public void testAppHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull("app should have a greeting", classUnderTest.getGreeting());
    }
}
```
To execute the test, I ran the command `./gradlew test`.
The screenshot below shows the terminal output after executing the command, indicating the test passed successfully.

<img  src="https://i.postimg.cc/cLgCTpvx/Screenshot-2024-03-18-at-18-15-33.png"  width="600">

## Add a new task of type Copy
The next task involved adding a new task of type Copy to the `build.gradle` file , designed to create a backup of the source code, ensuring that a reliable recovery point is available in case of any unforeseen issues during development.
The backup task is defined with Gradle's Copy task type, as shown below, to replicate the contents of the src directory into a designated backup location within the project. This step is essential for maintaining a current snapshot of our codebase, especially before making significant changes or updates:
```java
task backup(type: Copy) {
    group = "DevOps"
    description = "Copies the sources of the application to a backup folder"

    from 'src'
    into 'backup'
}
```

After implementing the task, I tested its functionality by executing `./gradlew backup` from the command line. The task ran successfully, as evidenced by the terminal output captured in the screenshot below. This output confirms that the source code was successfully copied to the backup location, demonstrating the task's effectiveness in safeguarding the project's code.

<img  src="https://i.postimg.cc/66c8QLsX/Screenshot-2024-03-18-at-18-26-53.png"  width="600">

Although the backup folder is not present in the remote repository, it has been successfully generated in my local development environment. This is evidenced by the presence of the backup folder in my local file system after executing the backup task in Gradle, as shown in the screenshot below.

<img  src="https://i.postimg.cc/gcqVTz1M/backup.png"  width="250">


The addition of the backup task to the Gradle build script has improved the project's resilience by facilitating easy and reliable code backups.

## Add a new task of type Zip

The final task involved creating a new task of type Zip to package the project's source code into a compressed .zip file.
This Zip task simplifies the process of packaging the src directory into a .zip file, useful for backups or distribution. This step is crucial for archiving iterations of our project or preparing the code for distribution.
Here's the task definition:
```java
task archive(type: Zip) {
    group = "DevOps"
    description = "Creates a zip archive of the source code"

    from 'src'
    archiveFileName = 'src_backup.zip'
    destinationDir(file('build'))
}
```

Upon defining the zip task, I executed it using the command ./gradlew zip. The successful execution of this task was confirmed by the terminal output, which indicated that the src directory had been successfully compressed into a ZIP archive.
Below is a screenshot of the terminal following the task's completion, showcasing the successful archive creation.

<img  src="https://i.postimg.cc/Wz5zfjm3/Screenshot-2024-03-18-at-18-37-46.png"  width="1000">

Although the zip file is not present in the remote repository, it has been successfully generated in my local development environment.
This is evidenced by the presence of the zip file in my local file system after executing the archive task in Gradle.

<img  src="https://i.postimg.cc/y8LxgdWb/zipFile.png"  width="250">

## Conclusion

The completion of this assignment has provided valuable insights into the practical applications of Gradle as a build tool. The tasks performed throughout the assignment emphasized Gradle's adaptability and versatility in managing various development tasks.
The automation of build processes, integration of unit tests, and execution of file manipulation tasks demonstrated the capabilities of Gradle. These tasks are essential for maintaining a robust and efficient development workflow.
The addition of new tasks to the `build.gradle` file showcased the extensibility of Gradle. The creation of tasks such as `runServer`, `backup`, and `archive` not only streamlined the development process but also improved the project's resilience and distribution capabilities.
The integration of a unit test into the build process highlighted the importance of testing in software development and how Gradle facilitates this process.
Overall, the knowledge and skills gained from this assignment have enhanced my understanding of Gradle and its role in software development. This experience will be beneficial in future projects, contributing to more efficient and reliable development workflows.

[Back to Top](#ca2-part1-build-tools-with-gradle-technical-report)