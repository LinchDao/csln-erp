FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /workspace

COPY pom.xml ./
COPY src ./src

RUN mvn -B -DskipTests clean package

FROM eclipse-temurin:17-jre
WORKDIR /app

ENV TZ=Asia/Shanghai
ENV SPRING_PROFILES_ACTIVE=docker
ENV JAVA_OPTS=""

COPY --from=builder /workspace/target/csln-erp-0.0.1-SNAPSHOT.jar /app/app.jar
COPY --from=builder /workspace/src/main/resources/application-docker.yaml /app/default-config/application-docker.yaml

EXPOSE 8000

ENTRYPOINT ["sh", "-c", "mkdir -p /app/config && if [ ! -f /app/config/application-docker.yaml ]; then cp /app/default-config/application-docker.yaml /app/config/application-docker.yaml; fi && java $JAVA_OPTS -jar /app/app.jar"]
