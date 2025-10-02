echo "> dgt-java"
git pull
chmod +x ./build.sh
mvn clean validate compile assembly:assembly -DdescriptorId=jar-with-dependencies
cp ./target/autodownload-1-jar-with-dependencies.jar ./autodownload.jar
chmod +x request/get.sh
chmod +x request/post.sh
docker build -t dgt-java:1 .
docker compose down
docker compose up -d