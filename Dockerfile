FROM openjdk:17
WORKDIR /var/app/wato-backend/build/libs
COPY build/libs/wato-api.jar .
WORKDIR /var/app/wato-backend
CMD ["java","-jar","-Dspring.profiles.active=dev","build/libs/wato-api.jar"]