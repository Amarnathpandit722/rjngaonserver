FROM openjdk:8
EXPOSE 8084
ADD target/taxcollection.jar taxcollection.jar
ENTRYPOINT ["java","-jar","/taxcollection.jar"]