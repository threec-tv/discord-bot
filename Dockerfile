FROM adoptopenjdk:11-jre-hotspot as builder
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM amazoncorretto:17-alpine
COPY --from=builder dependencies/ ./
RUN true
COPY --from=builder snapshot-dependencies/ ./
RUN true
COPY --from=builder spring-boot-loader/ ./
RUN true
COPY --from=builder application/ ./
RUN true
ADD https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip /
RUN unzip newrelic-java.zip
RUN true
COPY newrelic.yml newrelic/newrelic.yml
RUN true
ENTRYPOINT ["java", "-javaagent:newrelic/newrelic.jar", "org.springframework.boot.loader.JarLauncher", " -Djdk.tls.client.protocols=TLSv1.2"]
EXPOSE 8080
