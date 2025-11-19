#!/bin/bash

echo "============================"
echo "🚀 Deploy Script Start"
echo "============================"

APP_HOME="/home/ubuntu/app"
JAR_PATH="$APP_HOME/jar/ShoppingCart-0.0.1-SNAPSHOT.jar"
LOG_PATH="$APP_HOME/logs/app.log"

mkdir -p $APP_HOME/logs
mkdir -p $APP_HOME/jar

echo ">>> Stop running application (if exists)"
PID=$(pgrep -f ShoppingCart-0.0.1-SNAPSHOT.jar)

if [ -n "$PID" ]; then
  echo ">>> Killing PID: $PID"
  kill -9 $PID
else
  echo ">>> No running application found"
fi

echo ">>> Start new application"
nohup java -jar $JAR_PATH \
  --spring.profiles.active=prod \
  > $LOG_PATH 2>&1 &

echo ">>> Deployment complete!"