# 
# The image built by this Dockerfile can be run with default envirenmental varible values
#   docker run -p 9090:9090 -it helix-datahub
# or with overwritten environmental variable values
#   docker run -p 9090:9090 \
#    -e PORT=9090 \
#    -e DATAHUB_CONFIG_FILE=config.yml \
#    -e REDIS_URL="redis://192.168.0.6:6379" \
#    -e REDIS_URL_B="redis://192.168.0.6:6380" \
#    -e HELIX_REPLICATION_ROLE=NONE \
#    -e HELIX_REPLICATION_HOSTS="192.168.0.6:9092" \
#    -it helix-datahub
#
# FROM java:8-jre
FROM openjdk:8-jdk
EXPOSE 9090
COPY target/helix-datahub-1.0.jar /app/helix-datahub-1.0.jar
COPY config*.yml /app/
WORKDIR /app
ENV JAVA_OPTS=
# The port the Jetty webserver needs to bind to
ENV PORT=9090
ENV DATAHUB_CONFIG_FILE=config.yml
ENV REDIS_URL=redis://192.168.0.6:6379
ENV REDIS_URL_B=redis://192.168.0.6:6380
ENV HELIX_REPLICATION_ROLE=NONE
ENV HELIX_REPLICATION_HOSTS=192.168.0.6:9092
CMD java $JAVA_OPTS -Ddw.server.connector.port=$PORT -jar helix-datahub-1.0.jar server $DATAHUB_CONFIG_FILE