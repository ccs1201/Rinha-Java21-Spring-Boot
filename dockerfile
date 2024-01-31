FROM openjdk:21
LABEL authors="ccs1201" name="rinha-spring"
MAINTAINER linkedin.com/in/ccs1201
COPY target/rinha-backend-spring-boot-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]