FROM gradle:8.4.0-jdk17-alpine
LABEL maintainer="SzachMatyCompany"
WORKDIR /app
COPY /build/libs/GameLogicService.jar GameLogicService.jar
EXPOSE 8080
CMD ["/java", "-jar", "/app/GameLogicService.jar"]
