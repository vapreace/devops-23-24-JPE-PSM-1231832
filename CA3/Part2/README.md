# CA3 Part2: Virtualization with Vagrant: Technical Report

**Author:** Inês Lemos

**Date:** 13/05/2024

**Discipline:** DevOps

**Program:** SWitCH DEV

**Institution:** Instituto Superior de Engenharia/ Instituto Politécnico do Porto

## Table of Contents

- [Introduction](#introduction)
- [Environment Setup](#environment-setup)
- [Setup Base Project](#setup-base-project)
- [Vagrantfile](#vagrantfile)
- [Connecting Spring Boot to H2 Database](#connecting-spring-boot-to-h2-database)
- [Running the Project](#running-the-project)
- [Vagrant commands](#vagrant-commands)
- [Alternative Solution](#alternative-solution)
- [Conclusion](#conclusion)

## Introduction

This technical report documents the processes and outcomes of **Class Assignment 3 - Part 2**, centered on
virtualization with Vagrant. The assignment required setting up a virtualized environment using Vagrant to run a Spring
Boot application connected to an H2 database. Throughout this report, I detail the steps taken to configure the Vagrant
environment, connect the Spring Boot application to the H2 database, and run the project successfully. Additionally, I
explore an alternative solution using VMware with Vagrant and highlight the key differences between VMware and
VirtualBox.

## Environment Setup

To set up a virtualized environment using Vagrant, I followed these general steps:

**Download Vagrant**:
I visited the [official Vagrant website](https://www.vagrantup.com/downloads) and
downloaded the appropriate version for my system.

**Install Vagrant**:
I ran the installer I downloaded from the Vagrant website. The installation process was
straightforward; I just followed the prompts provided by the installer.

**Verify Installation**:
To ensure that Vagrant was installed correctly, I opened a terminal or command prompt
and ran:

   ```bash
   vagrant --version
   ```

This command displayed the installed version of Vagrant, which confirmed that the installation was successful.

**Update .gitignore**: To keep my repository clean, I added the following lines to the `.gitignore` to prevent tracking
the Vagrant-related directory and any .war files:

```
.vagrant/
*.war
```

## Setup Base Project

**Clone the Base Project**:
I started by cloning the base Vagrant project to get the necessary configuration files. I executed the following
command:

```bash
git clone https://bitbucket.org/pssmatos/vagrant-multi-spring-tut-demo/
```

This step cloned the repository that contains the initial setup and configurations needed for our project.

**Copy the Vagrantfile**:
After cloning the base project, I copied the Vagrantfile from the cloned repository to my project's specific directory.
Here's how I did it:

```bash
cp -r vagrant-multi-spring-tut-demo/Vagrantfile C/Users/Utilizador/Desktop/DevOps/devops-23-24-JPE-1231838/CA3/Part2
```

This action ensured that my project directory has the initial Vagrant configuration to proceed with the setup.

## Vagrantfile

The Vagrantfile is the configuration file that defines the virtual machine (VM) settings and provisions. After setting
up the initial Vagrantfile, I made the following key modifications to tailor it to our project requirements:

1. **Changed the Repository URL**: I updated the repository URL in the Vagrantfile to point to my specific project.
2. **Changed the Path**: I modified the path in the Vagrantfile to point to the correct directory.
3. **Added bootRun Command**: I added the `./gradlew bootRun` command to run the Spring Boot application.
4. **Updated the Java Version**: I changed the Java version used in the setup to OpenJDK 17.

Here's the updated Vagrantfile:

```ruby
# See: https://manski.net/2016/09/vagrant-multi-machine-tutorial/
# for information about machine names on private network
Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/bionic64"

  # This provision is common for both VMs
  config.vm.provision "shell", inline: <<-SHELL
    sudo apt-get update -y
    sudo apt-get install -y iputils-ping avahi-daemon libnss-mdns unzip \
        openjdk-17-jdk-headless
    # ifconfig
  SHELL

  #============
  # Configurations specific to the database VM
  config.vm.define "db" do |db|
    db.vm.box = "ubuntu/bionic64"
    db.vm.hostname = "db"
    db.vm.network "private_network", ip: "192.168.56.11"

    # We want to access H2 console from the host using port 8082
    # We want to connet to the H2 server using port 9092
    db.vm.network "forwarded_port", guest: 8082, host: 8082
    db.vm.network "forwarded_port", guest: 9092, host: 9092

    # We need to download H2
    db.vm.provision "shell", inline: <<-SHELL
      wget https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar
    SHELL

    # The following provision shell will run ALWAYS so that we can execute the H2 server process
    # This could be done in a different way, for instance, setiing H2 as as service, like in the following link:
    # How to setup java as a service in ubuntu: http://www.jcgonzalez.com/ubuntu-16-java-service-wrapper-example
    #
    # To connect to H2 use: jdbc:h2:tcp://192.168.33.11:9092/./jpadb
    db.vm.provision "shell", :run => 'always', inline: <<-SHELL
      java -cp ./h2*.jar org.h2.tools.Server -web -webAllowOthers -tcp -tcpAllowOthers -ifNotExists > ~/out.txt &
    SHELL
  end

  #============
  # Configurations specific to the webserver VM
  config.vm.define "web" do |web|
    web.vm.box = "ubuntu/bionic64"
    web.vm.hostname = "web"
    web.vm.network "private_network", ip: "192.168.56.10"

    # We set more ram memmory for this VM
    web.vm.provider "virtualbox" do |v|
      v.memory = 1024
    end

    # We want to access tomcat from the host using port 8080
    web.vm.network "forwarded_port", guest: 8080, host: 8080

    web.vm.provision "shell", inline: <<-SHELL, privileged: false
      # sudo apt-get install git -y
      # sudo apt-get install nodejs -y
      # sudo apt-get install npm -y
      # sudo ln -s /usr/bin/nodejs /usr/bin/node
      # sudo apt install -y tomcat9 tomcat9-admin
      # If you want to access Tomcat admin web page do the following:
      # Edit /etc/tomcat9/tomcat-users.xml
      # uncomment tomcat-users and add manager-gui to tomcat user

      # Change the following command to clone your own repository!
      git clone https://github.com/vapreace/devops-23-24-JPE-PSM-1231832.git
      cd devops-23-24-JPE-PSM-1231832/CA2/Part2/react-and-spring-data-rest-basic
      chmod u+x gradlew
      ./gradlew clean build
      ./gradlew bootRun 
      # To deploy the war file to tomcat9 do the following command:
      # sudo cp ./build/libs/basic-0.0.1-SNAPSHOT.war /var/lib/tomcat9/webapps
    SHELL
  end
end

```

## Connecting Spring Boot to H2 Database

To connect the Spring Boot application to the H2 database, I made the following changes to the
`react-and-spring-data-rest-basic` project:

**Modify application.properties**:
I added the necessary properties to `src/main/resources/application.properties` to connect to the H2 database:

```properties
server.servlet.context-path=/basic-0.0.1-SNAPSHOT
spring.data.rest.base-path=/api
spring.datasource.url=jdbc:h2:tcp://192.168.33.11:9092/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.h2.console.settings.web-allow-others=true
```

**Update React App.js**:
The `src/App.js` needed adjustments to match the new backend path:

```javascript
client({method: 'GET', path: '/basic-0.0.1-SNAPSHOT/api/employees'}).done(response => {
```

## Running the Project

Before running the project, I ensured that Virtual Box was initialized and that the repository to be cloned was public.
Then, I navigated to the project directory and executed the following command:

```bash
vagrant up
```

This command started the VMs and provisioned them according to the Vagrantfile configurations.

After the VMs were up and running, I navigated to http://localhost:8080/basic-0.0.1-SNAPSHOT/ in my web browser to check
if the Spring Boot application was running correctly. Below is a screenshot of the result:

<img src="https://i.postimg.cc/MGZ7GHwv/Screenshot-2024-05-13-at-17-25-28.png" width="800"/>

I also accessed the H2 console by visiting http://localhost:8082/h2-console and connected to the H2 database using the
JDBC URL `jdbc:h2:tcp://192.168.33.11:9092/./jpadb`. Here's a snapshot of the H2 Login page, where I entered the
connection details:

<img src="https://i.postimg.cc/rwwHB8HN/Screenshot-2024-05-13-at-17-25-42.png" width="800"/>

After connecting to the H2 database, I could view the tables and data stored in the database. This setup allowed me to
run the Spring Boot application and interact with the H2 database seamlessly. Below is a screenshot of the H2 console
showing the database table `EMPLOYEE`:

<img src="https://i.postimg.cc/0jwfRNRr/Screenshot-2024-05-13-at-17-25-59.png" width="800"/>

These steps confirmed that the Spring Boot application was functioning as expected and could communicate with the H2
database.

## Vagrant commands

Here are the main Vagrant commands I used during the setup and troubleshooting:

| Command             | Description                                                                             |
|---------------------|-----------------------------------------------------------------------------------------|
| `vagrant init`      | Initializes a new Vagrant environment by creating a Vagrantfile.                        |
| `vagrant up`        | Starts and provisions the Vagrant environment as defined in the Vagrantfile.            |
| `vagrant halt`      | Stops the Vagrant machine, effectively powering it down.                                |
| `vagrant reload`    | Restarts the Vagrant machine, reloading the Vagrantfile if it has changed.              |
| `vagrant destroy`   | Stops and destroys all resources that were created during the machine creation process. |
| `vagrant ssh`       | Connects to the machine via SSH.                                                        |
| `vagrant status`    | Shows the current status of the Vagrant machine.                                        |
| `vagrant suspend`   | Suspends the machine, saving its current running state.                                 |
| `vagrant resume`    | Resumes a suspended Vagrant machine.                                                    |
| `vagrant provision` | Provisions the Vagrant machine based on the configuration specified in the Vagrantfile. |

These commands helped manage and navigate the virtualized environments efficiently.

## Alternative Solution

In this section, I explore VMware as an alternative virtualization tool to VirtualBox. Below is a detailed comparison
between VMware and VirtualBox, followed by instructions on how VMware can be used with Vagrant to achieve the goals
outlined for this assignment.

**Comparison of VMware and VirtualBox**

- **VirtualBox:**
    - **Overview**: A free, open-source hypervisor from Oracle, favored for its ease of use and support for various
      OSes.
    - **Pros**:
        - Free and open-source.
        - User-friendly GUI.
        - Supports many guest operating systems.
    - **Cons**:
        - Limited advanced features.
        - Can be slower with 3D graphics and larger VMs.

- **VMware (Workstation and Fusion):**
    - **Overview**: A professional-grade solution from VMware, known for performance and advanced features.
    - **Pros**:
        - High performance and stability.
        - Advanced features like snapshots, cloning, and shared VMs.
        - Integrates well with other VMware enterprise products.
    - **Cons**:
        - Expensive, requiring a license post-trial.
        - Steeper learning curve for enterprise-grade features.

**Using VMware with Vagrant**

Integrating VMware with Vagrant involves a few steps:

1. **Install the Vagrant VMware Utility**. This is necessary for Vagrant to manage VMware VMs.

```bash
# Example for installing on Linux
wget https://releases.hashicorp.com/vagrant-VMware-utility/1.0.14/vagrant-VMware-utility_1.0.14_x86_64.deb
sudo dpkg -i vagrant-VMware-utility_1.0.14_x86_64.deb
```

2. **Install the Vagrant Plugin for VMware**. This plugin allows Vagrant to interact with VMware.

```bash
vagrant plugin install vagrant-VMware-desktop
```

3. **Configure the Vagrantfile**. Update your Vagrantfile to use VMware as the provider.

```ruby
Vagrant.configure("2") do |config|
  config.vm.box = "hashicorp/bionic64"
  config.vm.provider "VMware_desktop" do |v|
    v.vmx["memsize"] = "1024"
    v.vmx["numvcpus"] = "2"
  end
end
```

Switching to VMware with Vagrant provides a robust solution that enhances the virtualization capabilities of our
development environment. It supports advanced features and offers better performance, which can be particularly
beneficial for complex and larger projects.

This alternative solution aligns with the objectives of enhancing our virtualization setup to improve both the
development experience and the ease of transition to production-like environments.

## Conclusion

This technical report has documented the setup and execution of **Class Assignment 3 - Part 2**, focusing on
virtualization with Vagrant. By configuring the Vagrant environment, connecting the Spring Boot application to the H2
database, and running the project successfully, I have demonstrated the practical application of virtualization concepts
in a real-world scenario. The alternative solution using VMware with Vagrant has also been explored, highlighting the
differences between VMware and VirtualBox and the benefits of using VMware for advanced virtualization needs.