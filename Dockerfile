FROM java:8-jdk
#ENV GRADLE_USER_HOME=gradle/

COPY * /root/
WORKDIR /root/gradle
RUN chmod +x gradlew
RUN /root/gradlew assemble
RUN /root/gradlew check
RUN echo "End CI"