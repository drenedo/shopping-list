.PHONY: all
all: build

.PHONY: build
build:
	@./gradlew assemble --warning-mode all

.PHONY: test
test:
	@./gradlew check --warning-mode all

.PHONY: run
run:
	@./gradlew bootRun --warning-mode all
