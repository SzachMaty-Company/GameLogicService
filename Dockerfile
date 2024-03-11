FROM gradle:8.5-jdk-alpine AS build
WORKDIR /app
COPY ./build.gradle /app
COPY ./settings.gradle /app
RUN gradle dependencies

COPY /src /app/src
RUN gradle bootJar

FROM openjdk:17-oracle
COPY --from=build /app/lib/GameLogicService.jar .
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=prod", "-jar",  "GameLogicService.jar"]