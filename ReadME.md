## Security Monitor

The development was done using Java 1.8 so ensure $JAVA_HOME is set for the correct version. 

###IDE Configuration
The project utilizes Apache Maven so configuring an IDE can be done using the pom.xml file.<br />
IntelliJ module files are included and can be used with the Community Edition for running the monitor and tests.

### Start the Application
To run the application from the command line execute:

```mvn clean compile exec:java -Dexec.arguments="<DIRECTORY_PATH>"```

The ```-Dexec``` argument is optional, if not specified the monitor will watch the current directory.

### Run the Tests
To run all the tests from the command line execute:

```mvn clean test```
