.DEFAULT_GOAL := build-run
.PHONY: build setup clean install run run-dist test lint check-deps report

# Пути относительно корня проекта
GRADLE_CMD := ./app/gradlew
APP_DIR := app

setup:
	$(GRADLE_CMD) wrapper --gradle-version 8.8

clean:
	$(GRADLE_CMD) clean

build:
	$(GRADLE_CMD) clean build --stacktrace

install:
	$(GRADLE_CMD) clean  installShadowDist

run-dist:
	$(APP_DIR)/build/install/app/bin/app

run:
	$(GRADLE_CMD) run

test:
	$(GRADLE_CMD) jacocoTestReport

lint:
	$(GRADLE_CMD) checkstyleMain

check-deps:
	$(GRADLE_CMD) dependencyUpdates -Drevision=release

report:
	$(GRADLE_CMD) jacocoTestReport

build-run: build run