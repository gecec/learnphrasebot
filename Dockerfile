FROM maven:3.5.2-jdk-8-alpine AS MAVEN

COPY ./pom.xml ./pom.xml

RUN mvn dependency:go-offline package -B

COPY ./src ./src

RUN mvn clean install

FROM openjdk:8-jre-alpine


ENV \
    TERM=xterm-color           \
    TIME_ZONE=Israel           \
    MYUSER=app                 \
    MYUID=1001                 \
    DOCKER_GID=999

COPY files/scripts/nop.sh /usr/bin/nop.sh
COPY files/scripts/app.sh /usr/bin/app.sh
COPY files/init.sh /init.sh

RUN \
    chmod +x /usr/bin/nop.sh /usr/bin/app.sh /init.sh && \
    apk add --no-cache --update su-exec tzdata curl ca-certificates dumb-init && \
    ln -s /sbin/su-exec /usr/local/bin/gosu && \
    mkdir -p /home/$MYUSER && \
    adduser -s /bin/sh -D -u $MYUID $MYUSER && chown -R $MYUSER:$MYUSER /home/$MYUSER && \
    delgroup ping && addgroup -g 998 ping && \
    addgroup -g ${DOCKER_GID} docker && addgroup ${MYUSER} docker && \
    mkdir -p /srv && mkdir -p /srv/data && mkdir -p /srv/logs && chown -R $MYUSER:$MYUSER /srv && \
    cp /usr/share/zoneinfo/${TIME_ZONE} /etc/localtime && \
    echo "${TIME_ZONE}" > /etc/timezone && date && \
    ln -s /usr/bin/dumb-init /sbin/dinit && \
    rm -rf /var/cache/apk/*

RUN chmod 775 /srv/logs

EXPOSE 80/tcp
EXPOSE 4458

COPY ld-musl-x86_64.path /etc/ld-musl-x86_64.path
COPY data /srv/data

COPY run.sh /srv/run.sh

COPY --from=MAVEN /target/learn*.jar /srv/learnphrasebot.jar

ENTRYPOINT ["/init.sh"]