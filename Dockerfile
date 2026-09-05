FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:11-jre
WORKDIR /app
# Install curl and download webapp-runner
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl && \
    rm -rf /var/lib/apt/lists/* && \
    curl -fSL --retry 3 -o /app/webapp-runner.jar \
      "https://repo1.maven.org/maven2/com/heroku/webapp-runner/9.0.75/webapp-runner-9.0.75.jar" && \
    file /app/webapp-runner.jar
COPY --from=build /app/target/expense-tracker.war ./app.war
EXPOSE 8080
CMD ["java", "-jar", "webapp-runner.jar", "--port", "8080", "app.war"]
