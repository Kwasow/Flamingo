FROM openjdk:21-jdk

WORKDIR /usr/scripts
COPY ../scripts/wait-for-it.sh .
RUN chmod +x wait-for-it.sh

ENTRYPOINT ["./wait-for-it.sh", "-h", "database", "-p", "3306"]

