#0
FROM maven:latest as builder
COPY . .
RUN mvn install -DskipTests
RUN find / | grep clamav-rest-.*.jar

#1
FROM centos:centos7

ARG VERSION=1.0.4
ENV CLAMD_HOST=localhost
ENV CLAMD_PORT=3310
ENV TIMEOUT=120000
ENV MAXSIZE=100MB

RUN yum update -y && yum install -y java-1.8.0-openjdk &&  yum install -y java-1.8.0-openjdk-devel && yum clean all


RUN mkdir /var/clamav-rest

COPY --from=0 /target/clamav-rest-$VERSION.jar /var/clamav-rest/clamav-rest.jar

WORKDIR /var/clamav-rest/

EXPOSE 8080

ADD bootstrap.sh /
ENTRYPOINT ["/bootstrap.sh"]