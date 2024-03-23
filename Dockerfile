FROM gradle:8.5-jdk-alpine AS build
WORKDIR /app
COPY ./build.gradle /app
COPY ./settings.gradle /app
RUN gradle dependencies

COPY /src /app/src
RUN gradle bootJar

# Install Python and dependencies for Flask app
RUN apk update && apk add --no-cache python3 python3-dev py3-pip build-base
WORKDIR /app/AI
COPY ./AI/requirements.txt /app/AI/requirements.txt
RUN pip install --no-cache-dir -r /app/AI/requirements.txt

COPY ./AI /app/AI

# Set up environment variables for Flask
ENV FLASK_APP=app.py
ENV FLASK_RUN_HOST=0.0.0.0
ENV FLASK_RUN_PORT=5000


FROM openjdk:17-oracle
COPY --from=build /app/lib/GameLogicService.jar .
COPY --from=build /app/AI /app/AI
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "GameLogicService.jar"]
CMD ["python", "-m", "flask", "run", "--host=0.0.0.0"]