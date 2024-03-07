# CA1: Version Control with Git: Technical Report

## Introduction
This report details the "Version Control with Git" assignment for our DevOps course. The assignment is divided into two parts: first, using basic version control without branches, and second, implementing branching for new features and bug fixes. I also explore an alternative to Git, Subversion (SVN), comparing its features and potential application to this assignment's goals.

## Environment Setup
To begin, I used the previously created repository. The project is based on a Tutorial React.js and Spring Data REST application, which I placed in a designated folder for this assignment, named `CA1`.

## Part 1: Development Without Branches

### Goals and Requirements
-   The initial part of the assignment focuses on understanding and utilizing basic version control operations without branching.
-   Tasks include setting up the project environment, making changes directly to the master branch, and committing those changes.
-   A key requirement is to introduce a new feature (e.g., adding a `jobYears` field to an Employee object) and ensuring proper version tagging, starting with an initial version and updating it after adding the new feature.
-   The emphasis is on practicing commits, understanding the commit history, and using tags for versioning.

### Key Developments
In the first part, all development was done in the master branch. The steps included:

1. **Copy the code of the Tutorial React.js and Spring Data REST Application into a new folder named `CA1`.**
I used these commands to create a new directory named CA1 and copy the tutorial directory recursively to CA1:
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
After completing the development and unit testing of the new `jobYears` field in the `Employee` class, I dedicated time to meticulously analyze both the server and client-side code of our application. This in-depth code review was aimed at ensuring that the new feature integrates flawlessly with existing functionalities and adheres to our application's architectural and coding standards.
During this process, I focused on understanding the data flow and interaction patterns between the server and client components, particularly how the new `jobYears` data is managed and presented throughout the system. This comprehensive analysis allowed me to confirm the correctness of the implementation and validate that the addition of `jobYears` would not introduce regressions or disrupt the user experience.

7. **End of the assignment**
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
The process of adding support for the email field in the application and ensuring robust validation closely mirrored the approach taken with the `jobYears` field in Part 1. The following outlines the key steps taken and highlights the new code introduced as well as the pertinent Git commands that facilitated this development phase.
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


## Alternative Solution Analysis

In seeking an alternative to Git for version control, Subversion (SVN) offers a distinct approach with its centralized model, contrasting Git's decentralized nature. This section compares SVN to Git in terms of version control features and describes how SVN could be utilized to achieve the goals set forth in this assignment.

### Comparison to Git

1.  **Centralized vs. Decentralized**: Unlike Git's distributed architecture, SVN operates on a centralized model, where a single repository serves as the source of truth. This can simplify administration and access control but might limit flexibility and offline work capabilities.
    
2.  **Versioning Model**: SVN tracks changes on a per-file basis and assigns incremental version numbers to the entire repository for each commit. Git, on the other hand, treats changes as snapshots of the entire repository, offering a more holistic view of the project's history.
    
3.  **Branching and Merging**: Git's lightweight branching and powerful merging capabilities are well-suited for handling diverse and concurrent development streams. SVN supports branching and merging as well, but these operations can be more cumbersome due to its centralized nature and the way it handles file copies.
    
4.  **Binary Files Handling**: SVN handles binary files more gracefully than Git, storing deltas for binary changes, which can be advantageous for projects with significant binary assets.
    

### Utilizing SVN for the Assignment

To apply SVN in the context of this assignment, the following design considerations could be made:

1.  **Repository Setup**: Establish a centralized SVN repository to host the Tutorial React.js and Spring Data REST application, ensuring all version-controlled files are centrally managed.
    
2.  **Branching Strategy**: While SVN branches are more heavyweight than Git branches, they can still be utilized for developing new features or fixes. For instance, branches like `features/email-field` and `fixes/fix-invalid-email` could be created to parallel the Git workflow outlined in the assignment.
    
3.  **Committing and Tagging**: Continuous integration of changes would involve committing to the appropriate SVN branch and using SVN tags to mark stable versions of the application, similar to Git's tagging system.
    
4.  **Merging and Deployment**: Once features or fixes are completed and tested, they would be merged back into the trunk (SVN's default branch), and the repository would be tagged to reflect new versions, such as `v1.3.1` for bug fixes.
    

By adopting SVN and tailoring its features to the assignment's requirements, a comparable solution to Git's workflow can be achieved, demonstrating the versatility and applicability of different version control systems in software development projects.

## Conclusion

Completing the "Version Control with Git" assignment has significantly broadened my understanding of version control systems and their role in software development. The first part of the assignment reinforced the foundation of version control, focusing on direct modifications to the master branch and the essential practice of committing and tagging. The progression to the second part, which introduced branching, allowed for a deeper dive into more complex scenarios involving feature additions and bug fixes, demonstrating the importance of isolating changes for clearer project history and easier management.
The exploration of SVN as an alternative to Git provided  insights into different version control paradigms. By comparing SVN's centralized approach to Git's distributed model, I gained a comprehensive perspective on how various systems can be tailored to meet project requirements, highlighting the adaptability required in DevOps practices.
This assignment not only enhanced my technical skills in using Git and understanding SVN but also highlighted the essential role of version control in facilitating collaborative development environments, ensuring code integrity, and managing project evolution efficiently.
