# Build stage
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy toàn bộ code vào container
COPY . .

# Sửa lỗi quyền thực thi cho mvnw
RUN chmod +x mvnw

# Build bằng Maven Wrapper
RUN ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy file jar từ stage build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
