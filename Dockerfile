FROM maven:3.5.2-jdk-8-alpine AS MAVEN

COPY ./pom.xml ./pom.xml
COPY ./model/pom.xml ./model/pom.xml
COPY ./bot/pom.xml ./bot/pom.xml
COPY ./main/pom.xml ./main/pom.xml

RUN mvn dependency:go-offline package -B

COPY ./model/src ./model/src
COPY ./bot/src ./bot/src
COPY ./main/src ./main/src

RUN mvn clean install

FROM openjdk:8-jre-alpine

EXPOSE 80/tcp
EXPOSE 4458

RUN mkdir -p /app/
RUN mkdir -p /app/data

COPY ld-musl-x86_64.path /etc/ld-musl-x86_64.path
COPY data /app/data

COPY run.sh /app/run.sh

RUN ["chmod", "+x", "/app/run.sh"]

COPY --from=MAVEN /main/target/main-*.jar /app/learnphrasebot.jar

# CMD ["java", "-jar", "-Dspring.profiles.active=default", "-Dlogging.config=logback.xml", "/app/learnphrasebot.jar"]
ENTRYPOINT ["/app/run.sh"]