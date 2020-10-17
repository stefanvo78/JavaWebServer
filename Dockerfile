# builder - first stage to build the application
FROM maven:3.5.4-jdk-11-slim AS build-env
ADD ./pom.xml pom.xml
ADD ./src src/
RUN mvn clean package

# runtime - build final runtime image
FROM adoptopenjdk/openjdk11:ubi


# add the application's jar to the container
COPY --from=build-env target/javawebserver-1.0.jar app.jar

CMD ["java", "-jar", "/opt/app/javawebserver.jar"]


# run application
EXPOSE $PORT
ENTRYPOINT ["java","-jar","app.jar"]