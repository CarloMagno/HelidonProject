FROM maven:3.6-jdk-8-slim
USER root
EXPOSE 8080
RUN mkdir /app
COPY . /app
WORKDIR app
RUN mvn clean install
CMD ["java","-jar","/app/target/my-app-0.0.1-SNAPSHOT.jar"]
