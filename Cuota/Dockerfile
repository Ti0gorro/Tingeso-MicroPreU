FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} Cuota-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/Cuota-0.0.1-SNAPSHOT.jar"]