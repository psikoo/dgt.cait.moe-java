FROM openjdk:17
WORKDIR /usr/src/app
COPY .env ./.env
COPY ./autodownload.jar ./
COPY ./request ./request
RUN mkdir ./images
CMD ["java", "-jar", "./autodownload.jar", "-v", "3"]
# To build docker image run:
# sudo docker buildx build -t java-dgt:1 .