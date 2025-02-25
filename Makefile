.DEFAULT_GOAL := build-run
.PHONY: build setup clean install run run-dist test lint check-deps report check-gradle-version

# Настройки
GRADLE_CMD := ./app/gradlew
APP_DIR := app
REQUIRED_GRADLE_VERSION := 8.8

# Проверка версии Gradle
check-gradle-version:
	@if ! grep -q "gradle-${REQUIRED_GRADLE_VERSION}" ${APP_DIR}/gradle/wrapper/gradle-wrapper.properties; then \
		echo "ERROR: Gradle version must be ${REQUIRED_GRADLE_VERSION}. Run 'make setup' to fix."; \
		exit 1; \
	fi

# Цели с зависимостью от проверки версии
setup: check-gradle-version
	${GRADLE_CMD} wrapper --gradle-version ${REQUIRED_GRADLE_VERSION}

clean: check-gradle-version
	${GRADLE_CMD} clean

build: check-gradle-version
	${GRADLE_CMD} clean build --stacktrace

install: check-gradle-version
	${GRADLE_CMD} clean installShadowDist

run-dist: check-gradle-version
	${APP_DIR}/build/install/app/bin/app

run: check-gradle-version
	${GRADLE_CMD} run

test: check-gradle-version
	${GRADLE_CMD} jacocoTestReport

lint: check-gradle-version
	${GRADLE_CMD} checkstyleMain

check-deps: check-gradle-version
	${GRADLE_CMD} dependencyUpdates -Drevision=release

report: check-gradle-version
	${GRADLE_CMD} jacocoTestReport

build-run: build run
