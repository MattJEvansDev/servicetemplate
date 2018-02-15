FROM java:8
VOLUME /tmp
RUN ls
ARG BUILD_NAME=service
ARG BUILD_VERSION=0.1.0
ADD $BUILD_NAME-$BUILD_VERSION.jar $BUILD_NAME-app.jar
EXPOSE 8083
RUN bash -c 'touch /$BUILD_NAME-app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/$BUILD_NAME-app.jar"]



