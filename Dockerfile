# ============================================================
# Stage 1 — Builder: compila o projeto e gera o .war
# ============================================================
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /build

# Copia primeiro o wrapper do Gradle e os arquivos de configuração de build.
# Isso permite que o Docker cacheie a etapa de download de dependências:
# se nenhum desses arquivos mudou, a próxima build reaproveita as deps já baixadas.
COPY gradlew .
COPY gradle gradle
COPY settings.gradle build.gradle ./
COPY modules/vitrine-api/build.gradle modules/vitrine-api/build.gradle
COPY modules/vitrine-persistence/build.gradle modules/vitrine-persistence/build.gradle
COPY modules/vitrine-service/build.gradle modules/vitrine-service/build.gradle
COPY modules/vitrine-web/build.gradle modules/vitrine-web/build.gradle

# No Linux o bit de execução do gradlew costuma sumir quando o arquivo veio do Windows.
# E em repos clonados no Windows o gradlew costuma vir com CRLF — o que faz o shebang
# `#!/bin/sh\r` apontar para um interpretador inexistente e o RUN falhar com "not found".
# `sed` normaliza para LF, depois `chmod` garante o bit de execução.
RUN sed -i 's/\r$//' ./gradlew && chmod +x ./gradlew

# Pré-baixa as dependências. Se só mudou código-fonte (não build.gradle),
# a próxima build pula essa etapa inteira graças ao cache de layers.
RUN ./gradlew --no-daemon dependencies

# Agora copia o código-fonte de fato.
COPY modules modules

# Build do .war. --no-daemon é obrigatório em container: não queremos um daemon
# vivo numa imagem que vai ser descartada.
RUN ./gradlew --no-daemon :vitrine-web:war

# ============================================================
# Stage 2 — Runtime: Tomcat enxuto com o .war
# ============================================================
FROM tomcat:10.1-jre21

# Remove a aplicação default do Tomcat (página "It works!", manager, examples).
# Não usamos nada disso e em produção é uma fonte de CVE.
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia o .war da stage builder. Renomear para ROOT.war faz a app responder
# na raiz do servidor: http://host:8080/api/... em vez de
# http://host:8080/vitrine-web-1.0-SNAPSHOT/api/...
COPY --from=builder /build/modules/vitrine-web/build/libs/vitrine-web-1.0-SNAPSHOT.war \
     /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

# Default da imagem tomcat — explicitar é bom para deixar claro o que roda.
CMD ["catalina.sh", "run"]
