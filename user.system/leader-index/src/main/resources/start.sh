#!/bin/bash
echo Starting application 
cat /dev/null > nohup.out
nohup java -Xms1024m -Xmx2048m -XX:PermSize=512M -XX:CompileThreshold=10000 -server -jar leader-index-1.0.0-SNAPSHOT.jar &
tail -f nohup.out
