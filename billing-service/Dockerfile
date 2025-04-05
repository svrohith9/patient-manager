FROM eclipse-temurin:21-jdk-jammy
VOLUME /tmp
COPY target/billing-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 4001
EXPOSE 9001
ENTRYPOINT ["java","-jar","/app.jar"]
