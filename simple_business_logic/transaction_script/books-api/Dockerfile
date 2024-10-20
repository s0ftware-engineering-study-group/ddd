FROM amazoncorretto:21-alpine-jdk

LABEL authors="rmnzs"
EXPOSE 3000

RUN addgroup -S ddd && adduser -S booksapi -G ddd
USER booksapi

WORKDIR /api

COPY ./build/libs/books-api-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "books-api-0.0.1-SNAPSHOT.jar"]
