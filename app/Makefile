.DEFAULT_GOAL := build-run
.PHONY: build setup clean install run run-dist test lint check-deps report

setup:
	./gradlew wrapper --gradle-version 8.8

clean:
	./gradlew clean

build:
	./gradlew clean build --stacktrace

install:
	./gradlew clean install

run-dist:
	./build/install/app/bin/app

run:
	./gradlew run

test:
	./gradlew jacocoTestReport

lint:
	./gradlew checkstyleMain

check-deps:
	./gradlew dependencyUpdates -Drevision=release

report:
	./gradlew jacocoTestReport


build-run: build run