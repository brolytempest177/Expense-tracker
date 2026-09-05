FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:11-jre
WORKDIR /app
# Download webapp-runner directly
RUN curl -L -o webapp-runner.jar https://repo1.maven.org/maven2/com/heroku/webapp-runner/9.0.68/webapp-runner-9.0.68.jar
COPY --from=build /app/target/expense-tracker.war ./app.war
EXPOSE 8080
CMD ["java", "-jar", "webapp-runner.jar", "--port", "8080", "app.war"]
