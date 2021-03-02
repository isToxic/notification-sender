FROM java:8-jdk
#ENV GRADLE_USER_HOME=gradle/

COPY build/libs/notification-sender-0.0.1-SNAPSHOT.jar /app/
WORKDIR /app/
ENTRYPOINT ["java","-jar","notification-sender-0.0.1-SNAPSHOT.jar"]