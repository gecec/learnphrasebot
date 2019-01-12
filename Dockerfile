FROM openjdk:8-jre-alpine

EXPOSE 80/tcp
EXPOSE 4458

RUN mkdir -p /app/
RUN mkdir -p /app/data

COPY ld-musl-x86_64.path /etc/ld-musl-x86_64.path
COPY data /app/data

COPY run.sh /app/run.sh

ADD main/target/main-1.0-SNAPSHOT.jar /app/learnphrasebot.jar
# CMD ["java", "-jar", "-Dspring.profiles.active=default", "-Dlogging.config=logback.xml", "/app/learnphrasebot.jar"]
ENTRYPOINT ["/app/run.sh"]