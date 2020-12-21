FROM gradle:6.7.1-jdk11 as build
COPY . /home/src
WORKDIR /home/src
RUN gradle clean build -x check

FROM openjdk:11-jre-slim
COPY --from=build /home/src/build/libs/scalableweb-0.0.1-SNAPSHOT.jar /home/app/scalableweb.jar
COPY wait-for-it.sh /home/app/wait-for-it.sh
RUN chmod +x /home/app/wait-for-it.sh
EXPOSE 8096:8096
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "/home/app/scalableweb.jar"]
