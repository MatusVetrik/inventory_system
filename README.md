# Inventory system

## Quick start

### Prerequisites

1. Node and npm installed
2. JDK 21 installed
3. Maven installed
4. Docker installed and running
5. Ports **8080**, **8081**, **5173**, **5050**, **5432**  available
6. Open the project in IntelliJ to be able to correctly use predefined run scripts

### Setup project

1. Install backend dependencies and auto-generated objects
   ```mvn clean install -DskipTests```
2. Install node modules ```npm install```
3. generate openapi client ```generate-client```
4. Install node modules within generated client ```npm install```


### Start project
Start both frontend and backend
```app```  
Open the frontend running at http://localhost:5173


Start backend and frontend separately:
1. Start backend with docker containers
   ```Inventory backend```
    * Backend is running at http://localhost:8080
    * Keycloak is running at http://localhost:8081
    * PostgreSQL is running at http://localhost:5432
    * PgAdmin is running at http://localhost:5050
   

2. Start frontend
   ```frontend dev```
    * Frontend is running at http://localhost:5173
    * The application has three default users, each with a different role:
   
      | Username | Password | Role    |
      |----------|----------|---------|
      | user     | password | USER    |
      | manager  | password | MANAGER |
      | admin    | password | ADMIN   |

   

### Error using the newest docker desktop version

If you encounter an error while attempting to start the backend with Docker containers, uncomment
the following code in the
[application.yml](./src/main/resources/application.yaml) **application.yml** file.

```yaml
profiles:
  active: nodocker
```
Run docker compose in the root folder
   ```docker-compose```.
Then continue with [Start project](#start-project).
