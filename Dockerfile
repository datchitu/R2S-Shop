FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /R2Sshop

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /R2Sshop

COPY --from=build /R2Sshop/target/*.jar R2Sshop-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "R2Sshop-0.0.1-SNAPSHOT.jar"]

