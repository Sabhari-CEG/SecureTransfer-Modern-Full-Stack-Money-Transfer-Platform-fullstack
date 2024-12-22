FROM eclipse-temurin:23-jdk-alpine
RUN apk add --no-cache curl
WORKDIR /app
COPY . .
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/healthcheck || exit 1
CMD ["./mvnw", "spring-boot:run"]
