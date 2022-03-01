FROM adoptopenjdk/openjdk11:jre-11.0.13_8-alpine
VOLUME /tmp
EXPOSE 5500
ADD target/SpringBackendMoneyTranfer-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "/app.jar"]