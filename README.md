# Bevis Back-End

## Development
Before you can build this project, you must install and configure the following dependencies on your machine:

    sudo apt-get update

Install Java runtime environment:

    sudo apt-get install default-jre

Install Java SDK:

    sudo apt-get install openjdk-8-jdk

Run the project:

    ./mvnw

## Building for production

### Packaging as jar

To build the final jar and optimize the bevis application for production, run:

    ./mvnw -Pprod clean verify

To ensure everything worked, run:

    java -jar app-be/target/*.jar

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

    ./mvnw -Pprod,war clean verify

### Testing

To launch your application's tests, run:

    ./mvnw verify

### Deploy with a AWS ElasticBeanstalk

Run script inside the root of your project to build an archive for ElasticBeanstalk.
You may change the ElasticBeanstalk environment settings in ".ebextensions" folder.
```
./ebe.sh
```

Push "bevis-bundle-0.0.1" archive to AWS ElasticBeanstalk environment.

### Automating CI/CD by using AWS CodePipeline

Keep "buildspec.yml" in root of your folder to be able to deploy with AWS Developer tools stack

### Deploy as a AWS EC2 Linux 2 Service

#####Initial install environment.
Login as a root via SSH.

Install Java runtime environment:

    sudo yum install java-1.8.0-openjdk

Install Java SDK:

    sudo yum install java-1.8.0-openjdk-devel

Go to root folder:

    cd /home/ec2-user

Install Git

    sudo yum install git

Clone repository (replace url with your own bitbucket url):

    git clone https://<<bitbucket_username>>@bitbucket.org/<<bitbucket_username>>/bevis-be.git

Go to bevis directory:

    cd ./bevis-be

Load repository updates (input bitbucket password, when required)

    git pull origin master

Build backend app

    ./mvnw install -DskipTests

Create file (if not exist):

    nano /etc/systemd/system/bevis.service

Fill file bevis.service content:

```
[Unit]
Description=bevis
After=syslog.target

[Service]
User=root
ExecStart=/usr/bin/java -jar -Dspring.profiles.active=prod /home/ec2-user/bevis-be/target/bevis-be-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143

[Install]
WantedBy=multi-user.target
```

Reload service units:

    sudo systemctl daemon-reload

Start service:

    sudo service bevis start

Check service status:

    sudo service bevis status

Check server logs:

    tail -f /tmp/bevis.log

Open server port (9000 port is Java Backend port):

    sudo ufw allow 9000/tcp

-----------------------
#####Preparing new updates

Login as a root via SSH

Go to root folder:

```
cd /home/ec2-user/bevis-be
```

Load repository updates (input bitbucket password, when required)

```
git pull origin master
```

Remove old target folder (if folder exists):

```
rm -r target
```

Build backend app

```
./mvnw -Pprod install -DskipTests
```

Check if bevis-be-0.0.1-SNAPSHOT.jar exists in folder:

```
ls -al target/
```

Restart service (if service already started):

```
sudo service bevis restart
```

Check service status:

```
sudo service bevis status
```
Check server logs:

```
tail -f /tmp/bevis.log
```



