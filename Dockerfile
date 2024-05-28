FROM openjdk:18-jdk-alpine
VOLUME /tmp
COPY target/employee-management-0.0.1-SNAPSHOT.jar employee-management.jar
ENTRYPOINT ["java","-jar","/employee-management.jar"]
