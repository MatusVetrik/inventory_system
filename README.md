# Inventory system

## Quick start

### Prerequisites

1. Node and npm installed
2. JDK installed
3. Maven installed
3. Docker running
4. Ports **8080**, **8081**, **5173**, **5050**, **5432**  available

### Setup project

1. Install backend dependencies and auto-generated objects
   ```mvn clean install -DskipTests```

2. Install npm modules
    * move to js directory with [package.json](./src/main/js/package.json) file
   ```bash 
   cd ./src/main/js
   ```
    * install node modules
   ```bash
   npm install
   ```
    * generate openapi clients
   ```bash
   npm run generate-client
   ```
    * install node modules in generated client
   ```bash
   npm install
   ```

### Start project

1. Start backend with docker containers
   ```mvn spring-boot:run```
    * Backend is running at http://localhost:8080
    * Keycloak is running at http://localhost:8081
    * PostgreSQL is running at http://localhost:5432
    * PgAdmin is running at http://localhost:5050
2. Start frontend
   ```bash 
   npm run dev
   ```
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
   ```bash 
   docker compose up -d
   ```
Then continue with [Start project](#start-project).
