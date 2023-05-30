FROM openjdk:11

COPY /build/classes/kotlin/main/ /tmp

WORKDIR /tmp

CMD kotlin main/fernandocostagomes/ApplicationKt

RUN apk add --no-cache bash

CMD ./gradlew run
