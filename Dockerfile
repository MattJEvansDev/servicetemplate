FROM java:8
VOLUME /tmp
ADD build/libs/voteservice-0.1.0.jar voteservice-app.jar
EXPOSE 8083
RUN bash -c 'touch /voteservice-app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/voteservice-app.jar"]



