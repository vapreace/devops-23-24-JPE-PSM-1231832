# CA3 Part1: Virtualization with Vagrant: Technical Report

**Author:** Inês Lemos

**Date:** 04/05/2024

**Discipline:** DevOps

**Program:** SWitCH DEV

**Institution:** Instituto Superior de Engenharia/ Instituto Politécnico do Porto

## Table of Contents

- [Introduction](#introduction)
- [Create a VM](#create-a-vm)
- [Configure Network and Services](#configure-network-and-services)
- [Clone the Repository](#clone-the-repository)
- [Set Up Development Environment](#set-up-development-environment)
- [Execute the spring boot tutorial basic project](#execute-the-spring-boot-tutorial-basic-project)
- [Execute the gradle_basic_demo project - Part 1](#execute-the-gradle_basic_demo-project---part-1)
- [Execute the gradle_basic_demo project - Part 2](#execute-the-gradle_basic_demo-project---part-2)
- [Conclusion](#conclusion)

## Introduction

This technical report documents the processes and outcomes of **Class Assignment 3 - Part 1**, centered on 
virtualization
techniques using VirtualBox within the DevOps curriculum. The main objective was to gain hands-on experience in setting
up and managing virtual environments that are crucial for modern software development and operations.

In the report, I detail the creation and configuration of a virtual machine, the setup of the development environment,
and the execution of various projects.

## Create a VM

- The first step was to download VirtualBox from https://www.virtualbox.org/wiki/Downloads and install it.
- I launched VirtualBox and clicked `New` to begin setting up a new virtual machine. I named the VM and selected the
  appropriate type and version to match the operating system I intended to install.
- I allocated enough memory to ensure smooth operation and created a virtual hard disk to accommodate the virtual
  machine’s requirements.
- In the VM settings under the storage section, I mounted the ISO file of the OS to the virtual CD/DVD drive. I started
  the VM and followed the on-screen instructions to install the OS.
- After the installation was complete, I installed the VirtualBox Guest Additions.
- I configured the virtual machine settings to prepare for the Ubuntu 18.04 minimal installation. I connected the VM to
  the Ubuntu installation media available via the provided minimal CD link. I allocated 2048 MB of RAM to ensure
  adequate performance. For networking, I set Network Adapter 1 to NAT for internet access and configured Network
  Adapter 2 as a Host-only Adapter (vboxnet0), facilitating isolated communication with the host machine.

## Configure Network and Services

Once the basic virtual machine setup was completed, I focused on configuring the network and essential services to
enhance functionality and accessibility of the VM.

- I opened the VirtualBox Host Network Manager from the main menu by selecting `File` -> `Host Network Manager`.
- I clicked the `Create` button, which added a new Host-only network to the list. This setup allowed me to specify a 
  name
  for the network within the virtual machine's network settings.
- After setting up the Host-only Adapter (vboxnet0), I checked the IP address range, which was `192.168.56.1/24`. I
  chose
  `192.168.56.5` as the IP for the second adapter of the VM, ensuring it fell within the designated subnet.
- Upon booting the virtual machine, I proceeded to update the package repositories using `sudo apt update`.
- I installed the network tools package with `sudo apt install net-tools` to facilitate network configuration.
- To assign the chosen IP address, I edited the network configuration file by executing `sudo nano
  /etc/netplan/01-netcfg.yaml`. I ensured the file reflected the following settings for network configuration:

```yaml
network:
  version: 2
  renderer: networkd
  ethernets:
    enp0s3:
      dhcp4: yes
    enp0s8:
      addresses:
        - 192.168.56.5/24
```

- After editing, I applied the changes with `sudo netplan apply`.
- To remotely manage the VM, I installed and configured the OpenSSH server with `sudo apt install openssh-server`
  followed by enabling password authentication by editing `/etc/ssh/sshd_config` to uncomment the line
  `PasswordAuthentication yes`. I then restarted the SSH service with `sudo service ssh restart.
- I also set up an FTP server to transfer files to and from the VM by installing vsftpd using `sudo apt install vsftpd`.
  I
  enabled write access within the FTP server configuration by editing `/etc/vsftpd.conf` to uncomment the line
  `write_enable=YES` and then restarted the service with `sudo service vsftpd restart`.

## Clone the Repository

To clone my individual repository inside the virtual machine, I first needed to set up secure SSH access between the VM
and my GitHub repository. Here’s how I accomplished this:

- I generated a new SSH key pair in the VM to ensure secure communication with GitHub. Using the terminal, I executed
  the following command:

```bash
ssh-keygen -t ed25519 -C "email@example.com"
```

- To add the newly created SSH key to my GitHub account, I first accessed the SSH key content by displaying it in the
  terminal:

```bash
cat ~/.ssh/id_ed25519.pub
```

- I then logged into my GitHub account, navigated to `Settings` -> `SSH and GPG keys`, and clicked on New SSH key. I 
  pasted the key into the field provided and saved it, which allowed my VM to authenticate with GitHub securely.
- With SSH configured, I cloned my repository into the desired directory within the VM using the following command:

```bash
git clone git@github.com:userName/repositoryName.git
```

## Set Up Development Environment

After setting up the virtual machine and ensuring it was properly configured for network access, I proceeded to install
the necessary tools required for the projects.

- I began by updating and upgrading the installed packages to ensure all
  software on the VM was up-to-date. This was accomplished using the following commands:

```bash
sudo apt update
sudo apt upgrade
```

- Next, I installed Git, for version control and source code management:

```bash
sudo apt install git
```

- I also installed both the JDK (Java Development Kit) and JRE (Java Runtime Environment) for Java-based projects:

```bash
sudo apt install openjdk-17-jdk openjdk-17-jre
```

- Maven, which is needed for project dependency management and building Java projects, was installed next:

```bash
sudo apt install maven
```

- Installing Gradle required a few more steps due to its packaging:

```bash
wget https://services.gradle.org/distributions/gradle-8.6-bin.zip
sudo mkdir /opt/gradle
sudo unzip -d /opt/gradle gradle-8.6-bin.zip
```

- To ensure Gradle could be executed from any location within the terminal, I added its bin directory to the system
  PATH by modifying the `.bashrc` file:

```bash
echo "export GRADLE_HOME=/opt/gradle/gradle-8.6" >> ~/.bashrc
echo "export PATH=\$GRADLE_HOME/bin:\$PATH" >> ~/.bashrc
source ~/.bashrc
```

These installations equipped the virtual machine with the necessary tools to build and manage Java applications
effectively, allowing me to proceed with executing and testing the projects.
To confirm that all tools were installed correctly and are functioning as expected, I executed the following commands to
check their versions:

```bash
git --version
java --version
mvn --version
gradle --version
```

## Execute the spring boot tutorial basic project

In this section, I executed the **Spring Boot tutorial basic project**, which was part of the prerequisites from
previous
assignments. The goal was to build and run the project within the virtual machine environment set up previously.

1. I navigated to the `basic` directory where the project files are located. This directory contains the Spring Boot
   application setup.
2. To launch the Spring Boot application, I executed the following command in the terminal within the project directory:

```bash
./mvnw spring-boot:run
```

3. To ensure the application was accessible from external devices such as the host machine or other devices on the same
   network, I specified the VM's IP address when accessing it. To determine the IP address, I used the `ifconfig`
   command. Here is the URL I used to access the application:

```
http://192.168.56.5:8080/
```

The application loaded successfully, displaying the expected content, which indicates that the backend was
functioning correctly and the Spring Boot framework was properly serving the content. I captured a screenshot of the
application’s landing page as it appeared in the browser to document the successful execution and setup.

<img src=https://i.postimg.cc/9MThdkRR/Screenshot-2024-05-02-at-11-14-09.png width="600">

## Execute the gradle_basic_demo project - Part 1

In this section, I describe the process of building and running the **gradle_basic_demo project**. This project required
execution across two environments: the virtual machine and my host machine.

1. I navigated to the `gradle_basic_demo` directory within the virtual machine. To build the project, I executed the
   following command:

```bash
./gradlew build
```

2. Since the virtual machine setup was based on Ubuntu Server without a desktop environment, running GUI applications
   like the project’s chat client was not feasible on the VM. To address this, I opened a terminal on my host machine,
   navigated to the `gradle_basic_demo` directory (which also required a clone on the host), and ran the client component
   using the command below. This setup allowed the client on my host machine to communicate with the server running in
   the VM by specifying the VM's IP address and the port number:

```bash
./gradlew runClient --args="192.168.56.5 59001"
```

I successfully opened two chat windows from the host machine, demonstrating the functionality of the client-server
communication. The chat application functioned as expected, with messages being sent and received.
A screenshot captured this interaction, illustrating the active connection and data exchange facilitated by the network
setup.

<img src="https://i.postimg.cc/MpTJPHf8/Screenshot-2024-05-02-at-11-51-44.png" width="600">

## Execute the gradle_basic_demo project - Part 2

In this part of the assignment, I focused on building and executing another component of the **gradle_basic_demo
project** using the virtual machine.

1. I navigated to the `basic` folder within the `gradle_basic_demo` directory.
2. The following commands were executed to build the application and then start the Spring Boot server, making the
application accessible via the web.

```bash
./gradlew build
./gradlew bootRun
```

3. Once the server was running, I accessed the application by entering the following URL into a web browser. This URL
   directed me to the landing page of the Spring Boot application, hosted within the virtual machine, confirming that
   the server was operational and could handle client requests over the network.

```bash
http://192.168.56.5:8080/
```

## Conclusion

This technical report has documented the setup and execution of a virtual environment using VirtualBox for **Class
Assignment 3 Part 1**. The tasks included creating a virtual machine, configuring its network and services, and 
deploying
development tools necessary for running software projects.

The virtualization efforts undertaken have provided practical insights into the configuration and management of virtual
machines in a DevOps context. Successfully executing the Spring Boot tutorial and gradle_basic_demo projects within this
environment demonstrated the capability to simulate real-world software deployment and operation scenarios effectively.

Key outcomes from this assignment include a deeper understanding of network setup in virtualized environments and the
complexities of software configuration on virtual platforms. Challenges such as configuring network interfaces and
ensuring proper communication between host and guest machines were encountered and resolved, contributing to a more
comprehensive understanding of virtualization technologies.

Overall, the experiences from this assignment are vital for developing the skills necessary for managing complex
environments and will be beneficial in my ongoing educational and professional development in DevOps.

[Back to top](#ca3-part1-virtualization-with-vagrant-technical-report)