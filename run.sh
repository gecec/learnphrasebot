#!/bin/sh

java -Dspring.profiles.active=docker -Dlogging.config=logback.xml -jar /app/learnphrasebot.jar