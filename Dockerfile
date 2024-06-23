FROM maven:3.9.7-eclipse-temurin-21-alpine AS Build

WORKDIR /

COPY ./ ./

RUN mvn clean package


FROM eclipse-temurin:21-alpine AS Run

WORKDIR /

COPY --from=Build target/userregister-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "userregister-0.0.1-SNAPSHOT.jar"]
