FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY Wallet_BTZS1DY20YFSTTBA /app/Wallet_BTZS1DY20YFSTTBA

COPY target/veterinaria-0.0.1-SNAPSHOT.jar app.jar

ENV SPRING_DATASOURCE_URL="jdbc:oracle:thin:@btzs1dy20yfsttba_high?TNS_ADMIN=/app/Wallet_BTZS1DY20YFSTTBA"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]