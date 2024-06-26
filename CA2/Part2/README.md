# CA2 Part2: Build Tools with Gradle: Technical Report

**Author:** Inês Lemos

**Date:** 17/04/2024

**Discipline:** DevOps

**Program:** SWitCH DEV

**Institution:** Instituto Superior de Engenharia/ Instituto Politécnico do Porto

### Table of Contents
- [Introduction](#introduction)
- [Set Up Initial Gradle Project](#set-up-initial-gradle-project)
- [Integrate Existing Code](#integrate-existing-code)
- [Configure Frontend Plugin for Gradle](#configure-frontend-plugin-for-gradle)
- [Add Gradle Tasks for File Management](#add-gradle-tasks-for-file-management)
- [Alternative Solution](#alternative-solution)
- [Conclusion](#conclusion)

## Introduction
This document provides a comprehensive report of the tasks completed in the second part of the Class Assignment 2 for the DevOps course, which concentrates on the application of Gradle as a build automation tool. The assignment involves a series of systematically structured tasks that transition a Spring Boot application from Maven to Gradle, highlighting the practical usage and advantages of Gradle in a software development lifecycle.

The **Set Up Initial Gradle Project** section covers the foundational setup. **Integrate Existing Code** addresses the migration of the application's source code into the Gradle project structure. **Configure Frontend Plugin for Gradle** outlines the incorporation of a plugin to manage frontend assets. In **Add Gradle Tasks for File Management**, custom tasks are created for improved project maintenance. An **Alternative Solution** evaluates other build tools, and the **Conclusion** reflects on the learning experience and the application of Gradle in software development workflows.

## Set Up Initial Gradle Project
The initial setup of the Gradle project involved several key steps essential for transitioning from a Maven-based structure to a Gradle-based one.
The process began with the creation of a new branch specifically for this part of the assignment to ensure that the project's setup and subsequent changes were isolated and manageable. This was accomplished by executing the command:

```bash
git checkout -b tut-basic-gradle
```

Following this, a new Spring Boot project was initialized using the Spring Initializr web interface at https://start.spring.io/.
The project was configured to include critical dependencies for the application, such as Rest Repositories, Thymeleaf, JPA, and H2.
This setup ensures that all necessary modules are available for the application's functionality and are managed by Gradle.

The generated .zip file containing the project skeleton was downloaded and extracted into the `CA2/Part2/` folder within the repository.
This structure forms the basis of an "empty" Spring application ready to be built using Gradle.
To verify the project setup and available Gradle tasks, the following command was executed in the project's root directory:

```bash
./gradlew tasks
```

The output of this command provided a list of available tasks and functionalities that can be executed using the Gradle build tool.
```bash
Application tasks
-----------------
bootRun - Runs this project as a Spring Boot application.
bootTestRun - Runs this project as a Spring Boot application using the test runtime classpath.

Build tasks
-----------
assemble - Assembles the outputs of this project.
bootBuildImage - Builds an OCI image of the application using the output of the bootJar task
bootJar - Assembles an executable jar archive containing the main classes and their dependencies.
build - Assembles and tests this project.
buildDependents - Assembles and tests this project and all projects that depend on it.
buildNeeded - Assembles and tests this project and all projects it depends on.
classes - Assembles main classes.
clean - Deletes the build directory.
jar - Assembles a jar archive containing the classes of the 'main' feature.
resolveMainClassName - Resolves the name of the application's main class.
resolveTestMainClassName - Resolves the name of the application's test main class.
testClasses - Assembles test classes.

Build Setup tasks
-----------------
init - Initializes a new Gradle build.
wrapper - Generates Gradle wrapper files.

Documentation tasks
-------------------
javadoc - Generates Javadoc API documentation for the 'main' feature.

Help tasks
----------
buildEnvironment - Displays all buildscript dependencies declared in root project 'react-and-spring-data-rest-basic'.
dependencies - Displays all dependencies declared in root project 'react-and-spring-data-rest-basic'.
dependencyInsight - Displays the insight into a specific dependency in root project 'react-and-spring-data-rest-basic'.
dependencyManagement - Displays the dependency management declared in root project 'react-and-spring-data-rest-basic'.
help - Displays a help message.
javaToolchains - Displays the detected java toolchains.
outgoingVariants - Displays the outgoing variants of root project 'react-and-spring-data-rest-basic'.
projects - Displays the sub-projects of root project 'react-and-spring-data-rest-basic'.
properties - Displays the properties of root project 'react-and-spring-data-rest-basic'.
resolvableConfigurations - Displays the configurations that can be resolved in root project 'react-and-spring-data-rest-basic'.
tasks - Displays the tasks runnable from root project 'react-and-spring-data-rest-basic'.
```

The output confirmed the successful creation of various tasks associated with building and running the application, among others, as listed above.
This comprehensive list of tasks provided insights into the capabilities now available through the Gradle build tool, setting the stage for further customization and development in subsequent steps of the project.

## Integrate Existing Code
This stage of the project involved incorporating the existing codebase from a basic tutorial setup into the newly established Gradle project framework.
The integration process was meticulous to ensure that all components function correctly under the new build management system.

The following steps were taken to integrate the existing code into the Gradle project:

- **Replace the Source Directory**: The original `src` directory of the Gradle project was deleted to make way for the integration of the established codebase. The `src` folder, along with all its subfolders, was copied from the basic tutorial folder into the new Gradle project directory.

- **Include Additional Configuration Files**: Essential configuration files `webpack.config.js` and `package.json` were also copied into the root of the new project directory to maintain the frontend build configurations and dependencies.

- **Remove Redundant Directories**: Following the migration, the `src/main/resources/static/built` directory was deleted. This directory is designated to be automatically generated by Webpack during the build process and should not be manually included in version control to avoid conflicts and redundancy.


The following steps were taken to fix a compilation error that arose after migrating the codebase from `CA1` to `CA2-Part2`:

- **Adjust Import Statements**: In alignment with the updated project dependencies and Java EE to Jakarta EE transition, modifications were made to the Java classes.
In the `Employee.java` class, import statements were updated from `javax.persistence` to `jakarta.persistence`.

- **Package Manager Configuration**: The `package.json` was updated to specify a fixed version of the package manager by adding `"packageManager": "npm@9.6.7"`. This ensures that the project uses a consistent version of the package manager across different environments.

After the successful integration and configuration adjustments, the application was tested to ensure its operational integrity:

- **Running the Application**: The command `./gradlew bootRun` was executed to start the application, which compiles and launches the backend.

- **Verifying the Frontend**: Accessing http://localhost:8080 in a web browser should show an empty page.
This behavior is expected at this stage because the Gradle setup is currently missing a plugin necessary for handling the frontend code, a gap that will be addressed in subsequent steps of the project setup.

This approach ensures that the foundational codebase is seamlessly integrated into the Gradle environment, setting the stage for further enhancements and the addition of more complex functionalities.

## Configure Frontend Plugin for Gradle

To align the build processes of the frontend with the newly adopted Gradle system, the `org.siouan.frontend-gradle-plugin` was introduced. This plugin is crucial for managing frontend assets, similar to how the `frontend-maven-plugin` is used in Maven projects.

- **Adding the Plugin**: The Gradle build script was updated to include the `org.siouan.frontend` plugin appropriate for the Java version used in the project. For Java 17, the following line was added to the `plugins` block in `build.gradle`:
   ```groovy
   id "org.siouan.frontend-jdk17" version "8.0.0"
   ```

- **Configuring the Plugin**: To ensure the frontend assets are correctly handled, configurations specific to node version and script commands were added to build.gradle. This setup specifies the version of Node.js used and the scripts for assembling, cleaning, and checking the frontend:
    ```groovy
    frontend {
        nodeVersion = "16.20.2"
        assembleScript = "run build"
        cleanScript = "run clean"
        checkScript = "run check"
    }
    ```

- **Updating package.json**: The scripts section in package.json was updated to manage the execution of Webpack and other frontend-related tasks:
    ```json
    "scripts": {
        "webpack": "webpack",
        "build": "npm run webpack",
        "check": "echo Checking frontend",
        "clean": "echo Cleaning frontend",
        "lint": "echo Linting frontend",
        "test": "echo Testing frontend"
    }
    ```
- **Testing the Configuration**: After configuring the frontend plugin, the build and runtime behaviors were tested:

  - Build Test: Running `./gradlew build` confirmed that the project builds successfully with the frontend integration. 
  - Application Execution: `./gradlew bootRun` was executed and the application was accessed at http://localhost:8080. Unlike previous stages, the webpage was now populated with frontend content, indicating that the Gradle plugin successfully managed the frontend resources during the build and serve processes.
  This configuration demonstrates the effective integration of frontend build management into the Gradle environment, enhancing the project's ability to handle complex full-stack development workflows seamlessly.

## Add Gradle Tasks for File Management
To enhance the management of the project's files, specifically focusing on the distribution and cleanup processes, two custom Gradle tasks were defined: `copyJar` and `cleanWebpack`.

1. **Task: `copyJar`**
    - **Purpose**: This task is responsible for copying the `.jar` file generated by the `bootJar` task directly from the output directory to a `dist` folder at the project root. This approach ensures that only the correct, fully assembled `.jar` file is targeted for distribution, minimizing errors and ensuring that deployments contain the most current build.
    - **Configuration**:
      ```groovy
      task copyJar(type: Copy) {
          dependsOn bootJar
          from bootJar.outputs
          into file("dist")
      }
      ```
    - **Dependencies**: It explicitly depends on the `bootJar` task, ensuring that the copy operation is executed only after the `bootJar` has successfully completed, thus maintaining a clear and reliable build dependency.


2. **Task: `cleanWebpack`**
    - **Purpose**: The task aims to delete all files generated by Webpack, located in the `src/main/resources/static/built` directory. This ensures that the build environment remains clean and that only the necessary files are included in each build, preventing potential conflicts from stale or outdated files.
    - **Configuration**:
      ```groovy
      task cleanWebpack(type: Delete) {
          delete 'src/main/resources/static/built'
      }
      clean.dependsOn cleanWebpack
      ```
    - **Dependencies**: This task is configured to run automatically before the standard `clean` task of Gradle, making it part of the regular cleanup process.


Both tasks were executed to verify their functionality:

- **Executing `copyJar`**:
    - Command: `./gradlew copyJar`
    - **Outcome**: The `.jar` file, specifically the output of the `bootJar` task, was successfully copied to the `dist` directory. This test confirmed that the task accurately targets and relocates the correct artifact, thereby validating its integration into the build process and its readiness for subsequent distribution stages.


- **Executing `cleanWebpack`**:
    - Command: `./gradlew cleanWebpack`
    - **Outcome**: The contents of the `src/main/resources/static/built` directory were successfully deleted. This test verified that the cleanup task functions as intended, maintaining a clean build environment.

These tasks were integrated into the build process to automate file management tasks, thereby enhancing the efficiency and reliability of the build and deployment processes. Each task performed its designated function correctly, contributing to streamlined project maintenance and management.

## Alternative Solution

**Implementing the Assignment Goals with Maven**

To match the setup and functionality achieved with Gradle, I will outline the steps required to configure Maven for the Spring Boot application.
This alternative solution will mirror the Gradle setup, including the integration of frontend assets, custom build tasks, and file management.
Here’s a detailed step-by-step guide on setting up Maven for our Spring Boot application, including the necessary pom.xml configurations:

- **Project Setup:**
I created a `pom.xml` for the Spring Boot application, including dependencies for REST, Thymeleaf, JPA, and H2.
Here is a snippet of the pom.xml that includes these dependencies:
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-rest</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

- **Frontend Integration:**
It was necessary to configure the frontend-maven-plugin to handle Node and npm installation as well as building the frontend:
```xml
<plugins>
    <plugin>
        <groupId>com.github.eirslett</groupId>
        <artifactId>frontend-maven-plugin</artifactId>
        <version>1.11.0</version>
        <configuration>
            <nodeVersion>v16.20.2</nodeVersion>
            <workingDirectory>src/main/resources/static</workingDirectory>
        </configuration>
        <executions>
            <execution>
                <id>install node and npm</id>
                <goals>
                    <goal>install-node-and-npm</goal>
                </goals>
            </execution>
            <execution>
                <id>npm install</id>
                <goals>
                    <goal>npm</goal>
                    <configuration>
                        <arguments>install</arguments>
                    </configuration>
                </goals>
            </execution>
            <execution>
                <id>npm run build</id>
                <goals>
                    <goal>npm</goal>
                    <configuration>
                        <arguments>run build</arguments>
                    </configuration>
                </goals>
            </execution>
        </executions>
    </plugin>
</plugins>
```

- **Copy JAR Task:** To copy the generated .jar file to a distribution folder, the maven-resources-plugin was configured:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-resources-plugin</artifactId>
    <version>3.2.0</version>
    <executions>
        <execution>
            <id>copy-jar</id>
            <phase>package</phase>
            <goals>
                <goal>copy-resources</goal>
            </goals>
            <configuration>
                <outputDirectory>${project.build.directory}/dist</outputDirectory>
                <resources>
                    <resource>
                        <directory>${project.build.directory}</directory>
                        <includes>
                            <include>*.jar</include>
                        </includes>
                    </resource>
                </resources>
            </configuration>
        </execution>
    </executions>
</plugin>
```

- **Delete Webpack Files Task:** To delete the Webpack-generated files, the maven-clean-plugin was configured:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-clean-plugin</artifactId>
    <version>3.1.0</version>
    <executions>
        <execution>
            <id>delete-webpack-files</id>
            <phase>clean</phase>
            <goals>
                <goal>clean</goal>
            </goals>
            <configuration>
                <filesets>
                    <fileset>
                        <directory>src/main/resources/static/built</directory>
                        <includes>
                            <include>*</include>
                        </includes>
                    </fileset>
                </filesets> 
            </configuration>
        </execution>
    </executions>
</plugin>
```

**Comparison Between Maven and Gradle**

This table provides a concise overview of the core differences between Maven and Gradle, which can help in choosing the right tool based on the project needs.

| Feature                  | Maven                                                       | Gradle                                                      |
|--------------------------|-------------------------------------------------------------|-------------------------------------------------------------|
| **Build Language**       | XML-based configuration.                                    | Uses Groovy or Kotlin DSL for configuration scripts.        |
| **Performance**          | Generally slower due to its linear phase-dependent approach.| Faster, supports incremental builds and up-to-date checks.  |
| **Flexibility**          | Less flexible, rigid lifecycle phases.                      | Highly customizable and flexible due to scripting support.  |
| **Dependency Management**| Uses centralized repository system.                         | Offers powerful dependency management with dynamic versions.|
| **Ease of Use**          | Simple to set up with standardized lifecycle phases.        | Steeper learning curve but more powerful due to flexibility.|
| **Plugins**              | Wide range of available plugins but adding new plugins can be more complex.| Extensive plugin ecosystem; easier to write and apply custom plugins.|
| **Community and Support**| Well-established, large community and lots of resources.    | Growing community, well-supported with ample documentation. |
| **Use Case**             | Better suited for conventional Java projects.               | Ideal for multi-project builds and projects needing high customization.|

Maven has been presented as a viable alternative to Gradle for building and managing a Spring Boot project.
By detailing the setup and configuration necessary to replicate the functionality achieved with Gradle, we've demonstrated Maven's robust capabilities in dependency management, build automation, and plugin integration.
Maven's structured approach, characterized by its predictable build lifecycle and extensive plugin ecosystem, makes it particularly suitable for projects that benefit from a standardized build process.
While it may lack some of Gradle's flexibility and performance optimizations, such as incremental builds and a dynamic scripting environment, Maven's simplicity and widespread usage in the Java community continue to make it a strong contender for many development scenarios.

## Conclusion

This technical report has detailed the transition of a Spring Boot application from Maven to Gradle, highlighting the robust capabilities of Gradle in managing dependencies, integrating frontend tools, and customizing build tasks.
Through the comparison with Maven, we've explored how each tool aligns with different project needs, showcasing Maven's structured approach and Gradle's flexibility in scriptable environments.

The experience has not only enhanced my practical understanding of build automation tools but also underscored the importance of choosing the right tool based on specific project requirements and team expertise.
This knowledge will guide future decisions, ensuring efficient and effective management of software development projects.

[Back to Top](#ca2-part2-build-tools-with-gradle-technical-report)
