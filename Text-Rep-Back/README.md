# Charging Station Application

> Charging Station

## Build Setup

``` bash
# Install Gradle
https://gradle.org/install/

# Install Lombok
https://projectlombok.org/

# Setup dependencies for in-memory datasource
Add datasource config class

Add the line in build.gradle
	runtime("com.h2database:h2:1.3.176")

# build command
gradle --console=plain build copyJar

--consolde=plain is just to see the lines while building
build is to build
copyJar is a task added to copy the jar into a folder rather than going to bin

# Running Application single start point
java -jar jar/Charging-Station.jar
```


