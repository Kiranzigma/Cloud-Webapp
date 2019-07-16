#!/bin/bash
tail /etc/profile -n 5 > /tmp/tempFile.sh
sudo chmod 777 /tmp/tempFile.sh
echo "sudo yum install lsof -y" >> /tmp/tempFile.sh
echo "APP_PORT=sudo lsof -t -i:8081" >> /tmp/tempFile.sh
echo "sudo kill -9 \$APP_PORT" >> /tmp/tempFile.sh
echo "cd /var" >> /tmp/tempFile.sh
echo "sudo java -jar -Dspring.profiles.active=cloud -Ddatabase=\$DB_HOST -Dbucketname=\$S3_BUCKET LMSApp-0.0.1-SNAPSHOT.war" >> /tmp/tempFile.sh
cd /tmp/
./tempFile.sh > run.out 2> run.err &

