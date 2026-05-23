FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/auth-service-1.0.0.jar app.jar
EXPOSE 8080
ENV JAVA_OPTS="-XX:+UseZGC -XX:MaxRAMPercentage=75.0"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
