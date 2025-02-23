.DEFAULT_GOAL := build-run
.PHONY: build setup clean install run run-dist test lint check-deps report

setup:
	cd app && chmod +x ./gradlew && ./gradlew wrapper --gradle-version 8.8

clean:
	cd app && chmod +x ./gradlew && ./gradlew clean

build:
	cd app && chmod +x ./gradlew && ./gradlew clean build --stacktrace

install:
	cd app && chmod +x ./gradlew && ./gradlew clean installDist

run-dist:
	cd app && chmod +x ./gradlew && ./build/install/app/bin/app

run:
	cd app && chmod +x ./gradlew && ./gradlew run

test:
	cd app && chmod +x ./gradlew && ./gradlew jacocoTestReport

lint:
	cd app && chmod +x ./gradlew && ./gradlew checkstyleMain

check-deps:
	cd app && chmod +x ./gradlew && ./gradlew dependencyUpdates -Drevision=release

report:
	cd app && chmod +x ./gradlew && ./gradlew jacocoTestReport

build-run: build run
