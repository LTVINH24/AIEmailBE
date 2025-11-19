# Chọn JDK 25 base image
FROM eclipse-temurin:21-jdk-alpine

# Thư mục làm việc trong container
WORKDIR /app

# Copy file jar đã build sẵn
COPY target/*.jar app.jar

# Expose port
EXPOSE 8080

# Lệnh chạy app
ENTRYPOINT ["java", "-jar", "app.jar"]
