mvn clean install
ant
mvn install
bash ./../ecs/app.sh build product devo ecs-mini 1
docker run -p 8080:8080 381780986962.dkr.ecr.ap-southeast-1.amazonaws.com/devo/ecs-mini:1
