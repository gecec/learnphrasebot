#!/sbin/dinit /bin/sh

java -Dspring.profiles.active=docker -Dspring.datasource.url=jdbc:h2:file:/srv/data/botdb -Dspring.liquibase.url=jdbc:h2:file:/srv/data/botdb -jar /srv/learnphrasebot.jar