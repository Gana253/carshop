## Carshop Application

This application is a prototype of online car shop. User can buy the cars which are listed.
## Tech Stack
1. Java 8
2. Spring Boot
3. Spring Data
4. Mysql (Dev/Prod)
5. H2(Integration Test)
5. Docker
6. Maven]

## Setup

Download the project from the github and make sure that Maven/Docker installed. If not please follow the below steps to install it.
1. Maven Install - !(https://www.baeldung.com/install-maven-on-windows-linux-mac#:~:text=Installing%20Maven%20on%20Mac%20OS,%3A%20apache%2Dmaven%2D3.3.)
2. Docker Install - !(https://docs.docker.com/desktop/)

Mysql will be started in docker container automatically when you run scripts. Instructions given below

## Run Scripts
**Build Application:**
For Building the application execute the build.sh file which is located in [scripts](scripts) folder. From the terminal run the below cmd.
Refer to the [./consoleOutput.txt](./consoleOutput.txt) file in the project path for the execution result.
```
cd scripts
./scripts/build.sh

```
**Test Execution:**
For running the test case execute the runtest.sh file which is located in [scripts](scripts) folder. From the terminal run the below cmd.
Refer to the [./testRunConsoleOutput.txt](./testRunConsoleOutput.txt) file in the project path for the execution result.
```
cd scripts
./scripts/runtest.sh

```
**Start Server:**
For starting the server goto scripts path execute the run.sh file which is located in [scripts](scripts) folder. From the terminal run the below cmd.
Note that docker composes for mysql to start has been included in the script file.
```
cd scripts
./run.sh

```

Server will be started at port: 8080 . You can change the port number in [src/main/resources/application.yml](src/main/resources/application.yml) if required.
```$xslt
server:
  port: 8080
```

 
## MySql DB -Docker
Since we have started the Mysql as docker container. Please follow the below steps for querying the table through docker container.
Database creation and Table creation will be done automatically each time on start of the application. I configured ddl-auto: create-drop by
default in [src/main/resources/application.yml](src/main/resources/application.yml) file. Please check application.yml file for the configuration.
For Mac Users , after installation
Get the container id for the mysql.
```
docker ps
```
Example :

|CONTAINER ID | IMAGE            | COMMAND                |  CREATED      | STATUS        | PORTS                            |  NAMES              | 
| :---:       | :---:            | :---:                  |  :---:        | :---:         | :---:                            |  :---:              |  
|9adba192a629 | mysql            | "docker-entrypoint.s…" |  4 hours ago  | Up 33 seconds | 0.0.0.0:3306-3306/tcp, 33060/tcp |  docker_carshop-mysql_1 |


Get the container id through the above command and Execute the below with replacing the container id accordingly
```
 docker exec -it <CONTAINER_ID> mysql -uroot -p
```
```
Now you can see terminal with mysql:> after this type your database to check the table values and query the data
```$xslt
   use carshop
```

## Dataset
 On application startuo have added flag to load mock data sets. Same can be enabled for tests as well if needed when starting the application. By default, flag will be false while running tests.
 
 Room data stored in [src/main/resources/dataset/jsonCars.json](src/main/resources/dataset/jsonCars.json) file .]
 
 Please change the configuration load: default-data: true in [src/main/resources/application.yml](src/main/resources/application.yml) file.
 
