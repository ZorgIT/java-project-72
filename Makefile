.DEFAULT_GOAL := build-run
.PHONY: build setup clean install run run-dist test lint check-deps report

setup:
	сhmod +x app/gradlew
	cd app && ./gradlew wrapper --gradle-version 8.8

clean:
	cd app && ./gradlew clean

build:
	cd app && ./gradlew clean build --stacktrace

install:
	cd app && ./gradlew clean installDist  # Используем явное указание задачи

run-dist:
	cd app && ./build/install/app/bin/app

run:
	cd app && ./gradlew run

test:
	cd app && ./gradlew jacocoTestReport

lint:
	cd app && ./gradlew checkstyleMain

check-deps:
	cd app && ./gradlew dependencyUpdates -Drevision=release

report:
	cd app && ./gradlew jacocoTestReport

build-run: build run
