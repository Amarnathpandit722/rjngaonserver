FROM maven:3.9.5 AS build
WORKDIR /taxcollection
COPY pom.xml /taxcollection
RUN mvn dependency:resolve
COPY . /taxcollection
RUN mvn clean
RUN mvn package -DskipTests -X



FROM openjdk
COPY --from=build /taxcollection/target/*.jar taxcollection.jar
EXPOSE 8084
CMD ["java", "-jar", "taxcollection.jar"]4r