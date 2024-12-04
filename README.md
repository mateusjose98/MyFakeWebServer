# # WebServer Project

## Overview
This project is a simple web server implemented in Java. It listens for HTTP requests on a specified port and serves static files from a predefined directory.

## Project Structure
- `src/main/java/com/github/mateusjose98/core/WebServer.java`: The main class that initializes and starts the web server.
- `src/main/java/com/github/mateusjose98/util/WebConfig.java`: Configuration class that defines the web root directory and MIME types.
- `.gitignore`: Specifies files and directories to be ignored by Git.

## Requirements
- Java 11 or higher
- Maven

## Setup
1. Clone the repository:
    ```sh
    git clone https://github.com/mateusjose98/MyFakeWebServer
    cd MyFakeWebServer
    ```

2. Build the project using Maven:
    ```sh
    mvn clean install
    ```

## Running the Server
To start the server, run the `WebServer` class. By default, it listens on port 8080.

```sh
mvn exec:java -Dexec.mainClass="com.github.mateusjose98.core.WebServer"
```

