rm -rf ./app-be/target
echo "Removed old"
./mvnw clean install -DskipTests
echo "Built"
cp ./app-be/target/app-be-0.0.1-SNAPSHOT.jar ./app-be-0.0.1-SNAPSHOT.jar
echo "Copyed to root"
zip -r bevis-bundle-0.0.1 ./app-be-0.0.1-SNAPSHOT.jar .ebextensions/ .platform/
echo "Archive created"
