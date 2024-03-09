
# CA1: Version Control with Git: Technical Report

**Author:** Inês Lemos

**Date:** 08/03/2024

**Discipline:** DevOps

**Course:** SWitCH DEV

**Institution:** Instituto Superior de Engenharia/Instituto Politécnico do Porto

## Table of Contents

- [Introduction](#introduction)
- [Environment Setup](#environment-setup)
- [Part 1: Development Without Branches](#part-1-development-without-branches)
  - [Goals and Requirements](#goals-and-requirements)
  - [Key Developments](#key-developments)
- [Part 2: Development Using Branches](#part-2-development-using-branches)
  - [Goals and Requirements](#goals-and-requirements-1)
  - [Key Developments](#key-developments-1)
- [Final Results](#final-results)
  - [Implementation](#implementation)
  - [Branches](#branches)
  - [Tags](#tags)
  - [Issue Tracking](#issue-tracking)
- [Alternative Solution](#alternative-solution)
  - [Comparison of SVN and Git](#comparison-of-svn-and-git)
  - [Utilizing SVN for the Assignment](#utilizing-svn-for-the-assignment)
- [Conclusion](#conclusion)

## Introduction
This report details the "Version Control with Git" assignment for the DevOps discipline. The assignment is divided into two parts: **Part 1**, using basic version control without branches, and **Part 2**, implementing branching for new features and bug fixes. The outcomes of this project are detailed in the **Final Results** section, which visually demonstrates the evolved state of the application post the integration of all new functionalities and resolution of issues. I also explore an **Alternative Solution** to Git, Subversion (SVN), comparing its features and potential application to this assignment's goals.

## Environment Setup
Initially, I cloned an existing repository containing the Tutorial React.js and Spring Data REST application to have a local copy of the tutorial project. Following this, I set up my own repository to host the class assignments and ensure that all developments were tracked under my version control.
**Creating My Repository:** I created a new folder on my local machine for the DevOps class assignments and initialized it as a Git repository. This was the first step in establishing my workspace for the project.
```shell
mkdir ~/myDevOpsRepo
cd ~/myDevOpsRepo
git init
```
**Copying the Tutorial Application:** To incorporate the tutorial application into my project, I copied its contents into my newly created repository. This ensured that I had all the necessary starting files for the assignment within my own version control system.
```shell
cp -r ~/tutorial/* .
```
**Linking to GitHub:** With the tutorial application copied into my repository, I then linked my local repository to a new GitHub repository. This connection allowed me to push my changes to a remote server, facilitating backup and sharing.
```shell
git remote add origin <repository-URL>
```
**First Commit:** After setting up the repository and ensuring all files were in place, I added the README file to the repository. This initial change was committed with the message "add readme", marking the commencement of my work on the assignments.
```shell
git add .
git commit -m "add readme"
```
Reflecting on the process, I now recognize the importance of a clear initial commit message, such as "initial commit". This practice sets a clear starting point for the repository's history and is considered a good standard in version control workflows.
**Pushing to Remote:** Finally, I pushed my initial commit to the GitHub repository, officially starting the version history of my assignments in a remote location.
```shell
git push -u origin master
```
This process ensured that I had a clean, organized start to the class assignments, with a clear link to the foundational tutorial application while maintaining my repository for all subsequent developments.

## Part 1: Development Without Branches

### Goals and Requirements
-   The initial part of the assignment focuses on understanding and utilizing basic version control operations without branching.
-   Tasks include setting up the project environment, making changes directly to the master branch, and committing those changes.
-   A key requirement is to introduce a new feature (e.g., adding a `jobYears` field to an Employee object) and ensuring proper version tagging, starting with an initial version and updating it after adding the new feature.
-   The emphasis is on practicing commits, understanding the commit history, and using tags for versioning.

### Key Developments
In the first part, all development was done in the master branch. The steps included:

1. **Copy the code of the Tutorial React.js and Spring Data REST Application into a new folder named `CA1`.**

These commands were used to create a new directory named `CA1` and copy the tutorial directory recursively to `CA1`:
```shell
mkdir CA1
cp -r path/to/tutorial/* CA1/
```
2. **Commit the changes (and push them).**

Once the `CA1` directory was set up with the Tutorial application, I proceeded to commit these changes to the master branch with the following commands:
```shell
git add .
git commit -m "add folder CA1 with Tutorial"
git push
```
3. **Tagging the repository to mark the version of the application.**

Following the versioning pattern outlined in the assignment, major.minor.revision, I tagged the initial setup as `v1.1.0` and subsequently pushed this tag to the remote repository:
```shell
git tag -a v1.1.0 -m "v1.1.0"
push origin v1.1.0
```
4. **Develop a new feature to add a new field to the application.**

The core task of this first part was to develop a new feature by adding a `jobYears` field to the application, which records the number of years an employee has been with the company. Additionally, I implemented unit tests to ensure the creation of Employees and the validation of their attributes were functioning correctly, especially to enforce that only integer values were allowed for the `jobYears` field and not null and not empty values were allowed for String-type fields.
The following files were modified to incorporate this new feature:
- **Employee.java**: This Java class, representing the employee model, was updated to include a new integer field named `jobYears`. The modification involved adding the field itself along with its getter and setter methods to allow for data encapsulation and access and the validation of all parameters. Below are the key additions and modifications made to the `Employee` class to support the new functionality and ensure robust data validation:

```java
public Employee(String firstName, String lastName, String description, String jobTitle, int jobYears) {  
  if (!validStringParameters(firstName) || !validStringParameters(lastName) || !validStringParameters(description) || !validStringParameters(jobTitle) || !validJobYears(jobYears)) {  
  throw new IllegalArgumentException("Invalid parameters");  
    }  
  this.firstName = firstName;  
    this.lastName = lastName;  
    this.description = description;  
    this.jobTitle = jobTitle;  
    this.jobYears = jobYears;  
}

private boolean validStringParameters(String x) {  
  return x != null && !x.isEmpty();  
}  

private boolean validJobYears(int x) {  
  return x >= 0;  
}

public int getJobYears() {  
  return jobYears;  
}

public void setJobYears(int jobYears) {  
  if (!validJobYears(jobYears)) {  
  throw new IllegalArgumentException("Invalid parameters");  
    }  
  this.jobYears = jobYears;  
}
```
- **EmployeeTest.java**: To ensure the reliability of the new `jobYears` field, this file was updated to include unit tests. Key aspects of the testing include:
	- Initialization: Employed `@BeforeEach` for setting up a consistent test environment with a valid `Employee` instance.
	- Validation Tests: Conducted tests to validate that the constructor and setters reject invalid inputs (null or empty Strings and  negative `jobYears`), safeguarding against improper object creation.
	- Positive Scenarios: Confirmed that valid inputs result in successful object creation, with no exceptions thrown, ensuring the `Employee` class functions as intended under correct usage.
	- Equality and Hashing: Verified the correct implementation of `equals` and `hashCode` methods, essential for accurate object comparison.
	- String Representation: Tested the `toString` method to ensure it accurately represents `Employee` object details, facilitating easier debugging and logging.
- **DatabaseLoader.java**: This class, responsible for pre-loading the database with sample data, was altered to include `jobYears` information for the sample employees. This change ensured that the application could demonstrate the functionality of the new field right from the start.
- **app.js**: The React components within `app.js` were modified to support the display of the new `jobYears` field within the employee list. The `EmployeeList` and `Employee` components now include a column for "Job Years" in the rendered table, allowing users to view the number of years an employee has been with the company alongside their other details.

5. **Debug the server and client parts of the solution.**

After verifying the `jobYears` field's integration, I ran the application using `./mvnw spring-boot:run` to test its real-time functionality at `http://localhost:8080/`. This step was crucial for hands-on testing of the feature within the application's interface, ensuring its seamless operation and compatibility with existing functionalities. Concurrently, I conducted a thorough code review to check data handling on the server side and the accurate representation of `jobYears` on the client side, guaranteeing the feature's correctness and maintaining high code quality.

6. **End of the assignment**

Once satisfied with the stability and performance of the new feature, I committed the changes to the repository with a descriptive message outlining the enhancements. Following this, the updated code was pushed to the remote server to share the advancements with the team and maintain the project's collaborative workflow. To mark this significant update, I tagged the commit with `v1.2.0`, following the semantic versioning pattern adopted for the project. At the end of the assignment, I markes the repository with the tag `ca1-part1`.

## Part 2: Development Using Branches

### Goals and Requirements
-   The second part advances to using branches for feature development and bug fixes, emphasizing isolated development environments and merge strategies.
-   Requirements include creating feature branches for new developments or bug fixes, ensuring that changes do not interfere with the main codebase until they are ready to be merged.
-   The part concludes with tagging the master branch after successful merges to mark new versions of the application, showcasing effective branch management and integration in version control.

### Key Developments
In the second part, the focus shifted towards utilizing branch-based development to enhance the application's features and fix any existing bugs while ensuring that the master branch remained stable for "publishing" the stable versions of the Tutorial React.js and Spring Data REST Application. The steps included:

1. **Start using the master branch**

To ensure I was working in the correct branch, particularly the master branch for publishing stable versions, I employed the `git branch` command. This step was crucial during this second part, for verifying my current working branch, marked by an asterisk (*) in the command output. 

2. **Develop new features in branches**

In the development phase focused on adding an email field to our application, branch management was crucial. The process was initiated by creating a dedicated feature branch, followed by switching to this new branch for development activities. To begin, a new branch named `email-field` was created to encapsulate all developments related to the email field feature. After creating the `email-field` branch, the next step involved switching the working context to this branch to commence feature development. After successfully creating the `email-field` branch, the next step involved switching the working context to this branch to commence feature development. Following the switch to the newly created `email-field` branch, I employed the `git branch` command once more to confirm that the switch was successful. The commands used were as follows:
```shell
git branch email-field
git checkout email-field
git branch
```
3. **Integration and Testing of the Email Field**

The process of adding support for the email field in the application and ensuring robust validation closely mirrored the approach taken with the `jobYears` field in Part 1. The following outlines the key steps taken.
- **Code Implementation**: Similar to the previous feature development, I extended the `Employee` class to include an `email` field along with its getter and setter methods. This involved updating data models, forms, and views to accommodate the new field, ensuring it was fully integrated into the application's frontend and backend.
- **Unit Testing**: Following the established pattern, I wrote comprehensive unit tests to verify the correct creation of Employee instances with the new email field and to enforce validation rules, such as non-null and non-empty values for the email attribute.
- **Debugging**: The server and client parts of the application underwent thorough debugging to identify and rectify any issues arising from the addition of the email field, ensuring seamless operation and user experience.

4. **Merge the code with the master**

The completion of the email field feature involved a series of steps to integrate the changes into the main branch and update the application's version. Initially, the finalized changes in the `email-field` branch were committed. This branch was then pushed to the remote repository, setting the stage for merging into the main branch. To preserve the history, a no-fast-forward merge was used. After merging, the changes were pushed to the remote repository to update the main branch. Finally, the new version was tagged and pushed to mark this significant update. The commands used were as follows:
```shell
# Commit the feature changes:
git add .
git commit -m "email field added"

# Push the feature branch upstream:
git push --set-upstream origin email-field

# Switch to the main branch and merge changes:
git checkout main
git merge --no-ff email-field

# Push the merged changes to update the main branch:
git push

# Tag the new version and push the tag:
git tag -a v1.3.0 -m "v1.3.0"
git push origin v1.3.0
```

5. **Create a new branch to fix a bug**

In addressing the bug fix for email validation in the `Employee` class, a branch named `fix-invalid-email` was created following the established workflow. The development, testing, and merging processes mirrored those used in previous features and fixes, with an emphasis on maintaining code integrity and application stability.
The crux of the bug fix involved enhancing the `Employee` class to include validation logic for the email field, ensuring it adheres to the requirement of containing an "@" sign. The following code snippet illustrates the validation logic added:
```java
private boolean validEmail(String email) {  
  return email != null && email.contains("@");  
}
```

6. **End of the assignment**

After implementing the fix and conducting thorough testing to confirm its effectiveness, the changes were merged into the master branch, and the application version was updated to `v1.3.1` to indicate the minor fix. This version increment highlights the continuous improvement of the application's functionality and reliability. At the end of the assignment I marked the repository with the tag `ca1-part2`.

## Final Results

### Implementation
Following the implementation of all the new features, the final state of the application is illustrated below:

![enter image description here](https://i.postimg.cc/bNH2HsD1/Screenshot-2024-03-08-at-09-16-43.png)
In our application's employee model, the fields "First Name", "Last Name", and "Description" were pre-existing components of the model and have not been modified in the scope of this project. The development enhancements began with the addition of the "Job Title" field in a prior exercise. Subsequently, during Part 1 of this CA1, the "Job Years" field was introduced to track the duration of employees' tenure within the company. The latest enhancement, implemented in Part 2 of CA1, involved adding the "Email" field, further augmenting our employee data model with contact information.

### Branches
The image below showcases the current branches within the repository, as revealed by executing the `git branch` command. 

<img  src="https://i.postimg.cc/hvmrcHhv/Screenshot-2024-03-08-at-20-21-55.png"  width="300">

The subsequent image illustrates the chronological sequence of branches, highlighting the most recent contributions to the repository.
Through this assignment, I learned the importance of using branches for isolating changes related to specific features or fixes. This practice not only keeps the main codebase stable but also provides a clear and organized history of changes.

<img  src="https://i.postimg.cc/zX8mtSFt/Screenshot-2024-03-08-at-11-33-33.png"  width="300">

### Tags

Below is a visual depiction of the project's tags, generated using the `git tag` command.

<img  src="https://i.postimg.cc/50CvC8Jk/Screenshot-2024-03-08-at-20-22-19.png"  width="170">

The use of tags taught me how to mark specific points in the project's history as significant. This is crucial for tracking the progress of the project over time and for quickly reverting to previous versions if necessary.

### Issue Tracking
During the development process, two issues were created on GitHub to track and manage problems that arose. These issues were names "Create README.md file" and "Explore an alternative solution to GIT". They were then resolved and closed by including `fixes #1` and `fixes #2` in the commit messages. This practice not only provides a clear history of the problem and its solution, but also automatically closes the issue once the commit is pushed to the repository.
Issues can serve multiple purposes in a project. They can be used to track bugs, feature requests, or general tasks. They can also be assigned to specific team members, have labels for easy searching, and can be linked to specific commits or pull requests.
In future assignments, the aim is to utilize issues throughout the entire development process. This will help in managing tasks, tracking progress, and facilitating collaboration, especially when working in a team setting.


This section provided a comprehensive view of the application's evolution through the addition of new features, the strategic use of branching for development, and the marking of significant milestones with tags. The visual representations of the repository's branches and tags not only demonstrate the practical application of version control concepts but also highlight the collaborative and iterative nature of software development. The inclusion of issue tracking further underscores the importance of maintaining a clear and organized project history, ensuring that all developments are well-documented and traceable.

## Alternative Solution

In seeking an alternative to Git for version control, Subversion (SVN) offers a distinct approach with its centralized model, contrasting Git's decentralized nature. This section compares SVN to Git in terms of version control features and describes how SVN could be utilized to achieve the goals set forth in this assignment.

### Comparison of SVN and Git

| Feature              | SVN                                                                                             | Git                                                                                                                    |
|----------------------|-------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------|
| Architecture         | Centralized model, with a single repository as the authoritative source.                         | Distributed architecture, enabling multiple full-version repositories for enhanced redundancy and collaboration.       |
| Versioning Model     | Utilizes a linear, per-file versioning system, assigning incremental version numbers per commit. | Adopts a snapshot-based approach, encapsulating the state of the entire repository at each commit for comprehensive tracking. |
| Branching and Merging| Facilitates branch creation and merging, though the process may require more manual oversight.   | Provides efficient branching and merging capabilities, ideal for parallel development workflows.                        |
| Binary Files Handling| Efficiently manages binary file changes through delta storage, optimizing for large binary assets.| Stores complete binary files per change, which may increase repository size but ensures ease of access to all versions.    |
    

### Utilizing SVN for the Assignment

To apply SVN in the context of this assignment, the following design considerations could be made:

1.  **Repository Setup**: The `svnadmin create /path/to/repository` command can be used to establish a centralized SVN repository to host the Tutorial React.js and Spring Data REST application. This ensures that all version-controlled files are centrally managed.

2.  **Branching Strategy**: Although SVN branches are more heavyweight than Git branches, they can still be utilized for developing new features or fixes. For instance, branches like `features/email-field` and `fixes/fix-invalid-email` can be created using the `svn copy ^/trunk ^/branches/branch-name -m "Creating a new branch"` command. This parallels the Git workflow outlined in the assignment.

3.  **Committing and Tagging**: Continuous integration of changes can involve committing to the appropriate SVN branch using the `svn commit -m "message"` command. SVN tags can be used to mark stable versions of the application with the `svn copy ^/trunk ^/tags/tag-name -m "Creating a new tag"` command, similar to Git's tagging system.

4.  **Merging and Deployment**: Once features or fixes are completed and tested, they can be merged back into the trunk (SVN's default branch) using the `svn merge ^/branches/branch-name` command. The repository can be tagged to reflect new versions, such as `v1.3.1` for bug fixes, using the tagging command mentioned above.

By adopting SVN and tailoring its features to the assignment's requirements, a solution comparable to Git's workflow can be achieved. This demonstrates the versatility and applicability of different version control systems in software development projects.

## Conclusion

Completing the `Version Control with Git` assignment has significantly broadened my understanding of version control systems and their role in software development. The `Part 1` of the assignment reinforced the foundation of version control, focusing on direct modifications to the master branch and the essential practice of committing and tagging. The progression to the `Part 2`, which introduced branching, allowed for a deeper dive into more complex scenarios involving feature additions and bug fixes, demonstrating the importance of isolating changes for clearer project history and easier management.
The `Final Results` segment of this report encapsulates the tangible outcomes of this learning experience, showcasing the application's enhanced functionality through the successive addition of new features. This visual portrayal underscores the practical application of version control principles in real-world software development scenarios. The use of GitHub issues for problem tracking and management was also introduced and utilized, providing a clear history of problems and their solutions. This practice demonstrated the versatility and applicability of issues in software development projects.
The exploration of SVN as an `Alternative Solution` to Git provided  insights into different version control paradigms. By comparing SVN's centralized approach to Git's distributed model, I gained a comprehensive perspective on how various systems can be tailored to meet project requirements, highlighting the adaptability required in DevOps practices.
This assignment not only enhanced my technical skills in using Git and understanding SVN but also highlighted the essential role of version control in facilitating collaborative development environments, ensuring code integrity, and managing project evolution efficiently.