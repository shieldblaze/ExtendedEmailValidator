FROM amazoncorretto:17-al2023-headless

WORKDIR /usr/src/app
COPY . .

RUN chmod +x ./mvnw
RUN ./mvnw -ntp -B clean package -DskipTests

EXPOSE 8080/TCP
ENTRYPOINT ["java", "-jar", "/usr/src/app/api/target/api-1.0.0.jar"]
