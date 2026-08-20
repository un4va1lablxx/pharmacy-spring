FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

COPY gradle /app/gradle
COPY gradlew build.gradle settings.gradle /app/

RUN ./gradlew dependencies --no-daemon

COPY src /app/src
RUN ./gradlew bootJar --no-daemon -x test

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*-1.0.0.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
