FROM openjdk:8-jdk-alpine
MAINTAINER zxk175 <zxk175@qq.com>

VOLUME /tmp

ADD well-zuul-1.0.0.jar app.jar
RUN sh -c 'touch app.jar'
ENV JAVA_OPTS="-Xmx300m -Xms300m"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar app.jar" ]
EXPOSE 9000