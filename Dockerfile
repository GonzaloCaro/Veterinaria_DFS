FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copia el wallet de Oracle (asegúrate de que la carpeta Wallet_BTZS1DY20YFSTTBA esté en tu proyecto)
COPY Wallet_BTZS1DY20YFSTTBA /app/Wallet_BTZS1DY20YFSTTBA

# Copia el JAR
COPY target/veterinaria-0.0.1-SNAPSHOT.jar app.jar

# Actualiza la URL de JDBC para usar la ruta dentro del contenedor
ENV SPRING_DATASOURCE_URL="jdbc:oracle:thin:@btzs1dy20yfsttba_high?TNS_ADMIN=/app/Wallet_BTZS1DY20YFSTTBA"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]