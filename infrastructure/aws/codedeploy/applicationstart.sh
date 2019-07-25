#!/bin/bash
tail /etc/profile -n 5 > /tmp/tempFile.sh
sudo chmod 777 /tmp/tempFile.sh

#echo "sudo yum install lsof -y" >> /tmp/tempFile.sh
#echo "APP_PORT=sudo lsof -t -i:8080" >> /tmp/tempFile.sh
#echo "sudo kill -9 \$APP_PORT" >> /tmp/tempFile.sh
#echo "cd /var" >> /tmp/tempFile.sh
#echo "cd /opt/tomcat/bin" >> /tmp/tempFile.sh
echo "export PROFILE=cloud" >> /tmp/tempFile.sh
echo "sudo rm /opt/tomcat/bin/setenv.sh" >> /tmp/tempFile.sh
#echo "cd /opt/tomcat/webapps" >> /tmp/tempFile.sh
echo "sudo bash /opt/tomcat/bin/shutdown.sh" >> /tmp/tempFile.sh
echo "sudo touch /opt/tomcat/bin/setenv.sh" >> /tmp/tempFile.sh
echo "sudo chmod 777 /opt/tomcat/bin/setenv.sh" >> /tmp/tempFile.sh 
echo "sudo echo \"export JAVA_OPTS=\\\"-Dspring.profiles.active=\$PROFILE -Ddatabase=\$DB_HOST -Dbucketname=\$S3_BUCKET\\\"\" >> /opt/tomcat/bin/setenv.sh" >> /tmp/tempFile.sh
#echo "sudo echo 'export database=\$DB_HOST' >> setenv.sh" >> /tmp/tempFile.sh
#echo "sudo echo 'export bucketname=\$S3_BUCKET >> setenv.sh" >> /tmp/tempFile.sh   
#echo "Environment=\"JAVA_OPTS=-Djava.awt.headless=true -Dspring.profiles.active=cloud -Ddatabase=\$DB_HOST -Dbucketname=\$S3_BUCKET -Djava.security.egd=file:/dev/./urandom\"" >> /tmp/tempFile.sh
#echo "sudo java -jar -Dspring.profiles.active=cloud -Ddatabase=\$DB_HOST -Dbucketname=\$S3_BUCKET LMSApp-0.0.1-SNAPSHOT.war" >> /tmp/tempFile.sh
echo "sudo bash /opt/tomcat/bin/startup.sh" >> /tmp/tempFile.sh
cd /tmp/
./tempFile.sh > run.out 2 > run.err &

