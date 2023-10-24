FROM maven:3.6.0-jdk-11-slim  AS build

ENV HOME=/usr/app

RUN mkdir -p $HOME
WORKDIR $HOME

COPY pom.xml $HOME

WORKDIR $HOME/common
COPY common/pom.xml $HOME/common/pom.xml
RUN mvn -B dependency:go-offline -Dmaven.artifact.threads=30

COPY versioning/pom.xml $HOME/versioning/pom.xml
COPY app-be/pom.xml $HOME/app-be/pom.xml
COPY resources/pom.xml $HOME/resources/pom.xml
COPY files/pom.xml $HOME/files/pom.xml
COPY aws/pom.xml $HOME/aws/pom.xml
COPY keygen/pom.xml $HOME/keygen/pom.xml
COPY balance/pom.xml $HOME/balance/pom.xml
COPY sender/pom.xml $HOME/sender/pom.xml
COPY jsonrpc/pom.xml $HOME/jsonrpc/pom.xml
COPY ipfs/pom.xml $HOME/ipfs/pom.xml
COPY telegram/pom.xml $HOME/telegram/pom.xml
COPY inapppurchase/pom.xml $HOME/inapppurchase/pom.xml
COPY email/pom.xml $HOME/email/pom.xml
COPY email-core/pom.xml $HOME/email-core/pom.xml
COPY exchange/pom.xml $HOME/exchange/pom.xml
COPY pdf/pom.xml $HOME/pdf/pom.xml
COPY social/pom.xml $HOME/social/pom.xml
COPY security/pom.xml $HOME/security/pom.xml
COPY blockchain/pom.xml $HOME/blockchain/pom.xml
COPY csv/pom.xml $HOME/csv/pom.xml
COPY dynamicasset/pom.xml $HOME/dynamicasset/pom.xml
COPY master/pom.xml $HOME/master/pom.xml
COPY assettype/pom.xml $HOME/assettype/pom.xml
COPY config/pom.xml $HOME/config/pom.xml
COPY assetinfo/pom.xml $HOME/assetinfo/pom.xml
COPY exchange-data/pom.xml $HOME/exchange-data/pom.xml
COPY assetimport/pom.xml $HOME/assetimport/pom.xml
COPY certbuilder/pom.xml $HOME/certbuilder/pom.xml
COPY certificate/pom.xml $HOME/certificate/pom.xml
COPY blockchainfile/pom.xml $HOME/blockchainfile/pom.xml
COPY user/pom.xml $HOME/user/pom.xml
COPY socialcore/pom.xml $HOME/socialcore/pom.xml
COPY account/pom.xml $HOME/account/pom.xml
COPY file-core/pom.xml $HOME/file-core/pom.xml
COPY credits/pom.xml $HOME/credits/pom.xml
COPY certificate-core/pom.xml $HOME/certificate-core/pom.xml
COPY lister/pom.xml $HOME/lister/pom.xml
COPY filecode/pom.xml $HOME/filecode/pom.xml
COPY bevis-asset-push/pom.xml $HOME/bevis-asset-push/pom.xml
COPY balance-core/pom.xml $HOME/balance-core/pom.xml
COPY blockchain-core/pom.xml $HOME/blockchain-core/pom.xml
COPY messaging/pom.xml $HOME/messaging/pom.xml

COPY dashboard/pom.xml $HOME/dashboard/pom.xml
COPY nft-core/pom.xml $HOME/nft-core/pom.xml
COPY nft/pom.xml $HOME/nft/pom.xml
#
## build all dependencies for offline use
WORKDIR $HOME/

RUN mvn -B dependency:go-offline -DexcludeGroupIds=com.bevis -Dmaven.artifact.threads=50 || true


COPY . $HOME/

##RUN ./mvnw clean install -DskipTests
RUN mvn -f $HOME/pom.xml -Pprod package -DskipTests

FROM openjdk:11-jre-slim

COPY --from=build /usr/app/app-be/target/app-be-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar

EXPOSE 9090

CMD ["java","-jar", "/usr/local/lib/app.jar"]
