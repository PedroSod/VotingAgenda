FROM openjdk:11
EXPOSE 8080
ADD target/agendaVoting.jar agendaVoting.jar
ENTRYPOINT ["java","-jar","-Dspring.data.mongodb.uri=mongodb://mongo:27017/agendavoting","/agendaVoting.jar"]