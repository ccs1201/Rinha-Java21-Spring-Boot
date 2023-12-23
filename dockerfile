FROM openjdk:21
LABEL authors="ccs1201" name="rinha-spring-java21-vthreads-tvbox"
MAINTAINER linkedin.com/in/ccs1201
COPY target/rinha-backend-spring-boot-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]