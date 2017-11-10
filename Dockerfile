FROM openjdk:8

WORKDIR /app

ADD /target/scala-2.12/scalatra-blockchain-assembly-0.1.0-SNAPSHOT.jar /app/scalatra-blockchain.jar

EXPOSE 8080

CMD ["java", "-jar", "scalatra-blockchain.jar"]
