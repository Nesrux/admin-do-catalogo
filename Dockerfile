FROM eclipse-temurin:17-jre-alpine

COPY /build/libs/*.jar /opt/app/application.jar

RUN addgroup -S spring && adduser -S spring -S spring
USER spring:spring

CMD java -jar /opt/app/application.jar