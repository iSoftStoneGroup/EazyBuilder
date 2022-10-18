echo "mvn install eazybuilder-server....."
cd ../eazybuilder-server
mvn clean install -Dmaven.test.skip=true
cp ./pipeline/jenkins-client/target/jenkins-client*.jar ./pipeline/jenkins-client/src/main/docker/

echo "mvn install eazybuilder-web....."
cd ../eazybuilder-web
mvn clean install -Dmaven.test.skip=true
cp ./target/eazybuilder-web*.jar ./src/main/docker/


echo "update config....."
rm -rf ../eazybuilder-server/pipeline/jenkins-client/src/main/docker/config
cp -r ../eazybuilder-server/pipeline/jenkins-client/config/  ../eazybuilder-server/pipeline/jenkins-client/src/main/docker/

echo "start eazybuilder...."
docker-compose  -f ../dockercompose/docker-compose.yml up