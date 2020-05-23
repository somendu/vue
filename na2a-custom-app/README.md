# Na2a Custom App

This application consists out of two projects; `na2a-custom-app` and `na2acustom-app-ui`. The first is a server layer with business logic, where the latter is the JS front-end.

## Build

Requirements:

* Java 8
* Gradle 4.10 or higher
* Node.JS v8 or higher

First, build the front-end:

```sh
cd applicability-app
# optional, only the first time or when dependencies have changed
npm run install

npm run build
```

Then the back-end:

```sh
cd applicability
gradle build
```

and the built application is in `na2a-custom-app/build/libs/na2a-custom-app.jar`.

## Deployment

1.  Create a directory `C:\Apps\Na2aCustomApp`
2.  Copy a build (`na2a-custom-app/build/libs/na2a-custom-app.jar`) to this directory
3.  Create `C:\Apps\Na2aCustomApp\application.yaml`:

    ```yaml
    informatica.pim:
      server: http://localhost:1512
      username: internal_username
      password: internal_password
    ```

    > Replace internal\_username and internal\_password by the actual credentials of the 'under the hood' user

3.  Get a build of [https://repo.jenkins-ci.org/releases/com/sun/winsw/winsw/](WinSW), save it in `C:\Apps\Na2aCustomApp` and rename the executable to `na2a-custom-app.exe`.

4.  Create `C:\Apps\Na2aCustomApp\na2a-custom-app.xml`:

    ```xml
    <configuration>
      <id>na2a-custom-app</id>
      <name>Na2a Custom App</name>
      <description>TME Custom App</description>
      <executable>java</executable>
      <workingdirectory>C:\Apps\Na2aCustomApp</workingdirectory>
      <arguments>-jar na2a-custom-app.jar</arguments>
    </configuration>
    ```

5.  Open a terminal with administrative access and run:

    ```
    cd \Apps\Na2aCustomApp
    na2a-custom-app install
    ```

6.  Go to Services and start the Na2a Custom App service.

## Deploying updates

1.  Stop Na2a Custom App service
2.  Replace `C:\Apps\Na2aCustomApp\na2a-custom-app.jar`
3.  Start Na2a Custom App service

## Requirements
1. In order to retrieve api-tokens (security) with the custom application, the jar 'na2a-token.jar' needs to be available on the PIM server
   ```
   cd \Informatica\PIM\server\plugins
   copy na2a-token.jar in folder location       
   ```
   This jar is a separate plugin. The code can be found in git at 'na2a / SDK / na2a-token @ develop'
