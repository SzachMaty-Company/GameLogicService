FROM openjdk:17-oracle
LABEL maintainer="SzachMatyCompany"
COPY /build/libs/GameLogicService-0.0.1-SNAPSHOT.jar GameLogicService.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/GameLogicService.jar"]
