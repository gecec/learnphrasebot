#!/sbin/dinit /bin/sh

if [[ ! -f "/srv/data/botdb.mv.db" ]]; then
  echo "No database file, will be created a new clear one"
fi

java -Dspring.profiles.active=docker \
     -Dspring.datasource.url=jdbc:h2:file:/srv/data/botdb \
     -Dspring.liquibase.url=jdbc:h2:file:/srv/data/botdb \
     -Dlogging.file=/srv/logs/debug.log \
     -Dbot.token=$BOT_TOKEN \
     -Dbot.username=$BOT_USERNAME \
     -jar /srv/learnphrasebot.jar
