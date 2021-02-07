FROM adoptopenjdk/openjdk15:debian

RUN apt-get update
RUN apt-get -y install python3 python3-pip

EXPOSE 8081

ADD pymodel pymodel
RUN pip3 install -r pymodel/requirements.txt
ADD build/libs/forecaster-0.0.1-SNAPSHOT.jar forecaster.jar

ENTRYPOINT ["java", "-jar", "forecaster.jar"]