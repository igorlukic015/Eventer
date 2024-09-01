# Eventer

### Table of Contents

-   [Description](#description)
-   [How To Use](#how-to-use)
-   [Author Info](#author-contact-info)

---

## Description

Application that allows users to view events in Serbia.
Users can subscribe, comment and receive news about subscribed events.
Events that are due will have a warning if the weather forecast may impact holding the event.
Admins can add new events and categories.

### Technologies

-   Spring Boot
-   Angular
-   FastAPI
-   TypeScript
-   Java
-   Python
-   Maven
-   Docker
-   RabbitMQ

[Back To The Top](#eventer)

---

## How To Use

### Installation

For this application you need to have:
- [Java](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) v21
- [NodeJS](https://nodejs.org/) v20.12.0
- [Apache Maven](https://maven.apache.org/) v3.9.2
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) latest version

#### Backend

For enabling maven to use the Resulter library you have to configure maven locally.
Add this configuration to the settings.xml file inside $USERPROFILE/.m2

To profiles add this profile

```
<profile>
  <id>github</id>
  <repositories>
    <repository>
      <id>central</id>
      <url>https://repo1.maven.org/maven2</url>
      <releases><enabled>true</enabled></releases>
      <snapshots><enabled>false</enabled></snapshots>
    </repository>
    <repository>
      <id>github</id>
      <name>GitHub Apache Maven Packages</name>
      <url>https://maven.pkg.github.com/igorlukic015/resulter</url>
    </repository>
  </repositories>
</profile>
```

to servers add this server

```
<server>
  <id>github</id>
  <username>igorlukic015</username>
  <password>{For the password you have to email the author}</password>
</server>
```

For installation of the application backend you have to run a couple of services

First run docker desktop on your machine.

Then position yourself in Eventer/src with command

```
cd ./Eventer/src 
```

and run command

```
docker-compose up -d
```

Now containers will be downloaded and started.

Next you need to start all the services.
You have to start services in this specific order.

Open the forecast service in PyCharm. You will be prompted to create a virtual environment and install dependencies from requirements.txt file.
Change the USE_REAL_DATA variable in the .env file to suit your need.
Then you can start the project.

Open the other services in InteliJ.
First start the rts service, than admin and user service must be started last.


#### Frontend

For installation of evneter-admin frontend position yourself in Eventer/clients/eventer-admin folder with command:

```
cd ./Eventer/src/clients/eventer-admin
```

To install dependencies run:

```
yarn install
```

To run the eventer-admin application run:

```
yarn start
```

For installation of eventer frontend position yourself in Eventer/clients/eventer folder with command:

```
cd ./Eventer/src/clients/eventer
```

To install dependencies run:

```
yarn install
```

To run the eventer-admin application run:

```
yarn start
```

[Back To The Top](#eventer)

---

## Author contact info

-   [Igor Lukić](mailto:igor.lukic015@gmail.com)

[Back To The Top](#eventer)
