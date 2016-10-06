MAIN_CLASS=riddles.Main
MOD_VERSION=1.0
MOD_NAME=riddles

SRC_MAIN=src/main
SRC_JAVA_FILES=$(wildcard $(SRC_MAIN)/java/**/*.java)
SRC_RES_FILES=$(wildcard $(SRC_MAIN)/resources/*)
SRC_MODULE_INFO_FILES=$(wildcard $(SRC_MAIN)/jmod/module-info.java)

BUILD_ROOT_DIR=target
BUILD_COMPILE_DIR=$(BUILD_ROOT_DIR)/classes
BUILD_JAR=$(BUILD_ROOT_DIR)/riddles.jar
BUILD_JAR_MANIFEST=$(BUILD_ROOT_DIR)/manifest.mf
BUILD_MODULE_COMPILE_DIR=$(BUILD_ROOT_DIR)/jmod-classes
BUILD_MOD_JAR_DIR=$(BUILD_ROOT_DIR)/mod-jar
BUILD_MOD_JAR=$(BUILD_MOD_JAR_DIR)/riddles.jar
BUILD_JMOD_DIR=$(BUILD_ROOT_DIR)/jmod
BUILD_JMOD=$(BUILD_JMOD_DIR)/riddles.jmod
BUILD_JLINK_DIR=$(BUILD_ROOT_DIR)/riddles-app


.PHONY: clean build \
	run-cls \
	jar run-jar \
	compile-module \
	mod-jar run-mod-jar \
	jmod jlink run-jlink

clean:
	rm -rf $(BUILD_ROOT_DIR)

$(BUILD_ROOT_DIR) $(BUILD_MOD_JAR_DIR) $(BUILD_JMOD_DIR):
	mkdir -p $@

$(BUILD_COMPILE_DIR): $(SRC_RES_FILES) $(SRC_JAVA_FILES)
	mkdir -p $(BUILD_COMPILE_DIR)
	cp -r $(SRC_RES_FILES) $(BUILD_COMPILE_DIR)
	javac -d $(BUILD_COMPILE_DIR) $(SRC_JAVA_FILES)

build: $(BUILD_COMPILE_DIR)

run-cls: build
	java -classpath $(BUILD_COMPILE_DIR) $(MAIN_CLASS)

$(BUILD_JAR_MANIFEST): $(BUILD_ROOT_DIR)
	echo "Main-Class: $(MAIN_CLASS)" > $@

$(BUILD_JAR): build $(BUILD_JAR_MANIFEST)
	jar -cfm $@ $(BUILD_JAR_MANIFEST) -C $(BUILD_COMPILE_DIR) .

jar: $(BUILD_JAR)

run-jar: jar
	java -jar $(BUILD_JAR)

$(BUILD_MODULE_COMPILE_DIR): $(SRC_MODULE_INFO_FILES) $(SRC_RES_FILES)
	mkdir -p  $(BUILD_MODULE_COMPILE_DIR)
	cp -r $(SRC_RES_FILES) $(BUILD_MODULE_COMPILE_DIR)
	javac -target 1.8 -source 1.8 -d $(BUILD_MODULE_COMPILE_DIR) $(SRC_JAVA_FILES)
	javac -d $(BUILD_MODULE_COMPILE_DIR) $(SRC_MODULE_INFO_FILES)

compile-module: $(BUILD_MODULE_COMPILE_DIR)

$(BUILD_MOD_JAR): compile-module $(BUILD_MOD_JAR_DIR)
	jar --create --file $(BUILD_MOD_JAR) --main-class $(MAIN_CLASS) --module-version $(MOD_VERSION) \
	     -C $(BUILD_MODULE_COMPILE_DIR) .

mod-jar: $(BUILD_MOD_JAR)

run-mod-jar: mod-jar
	java -p $(BUILD_MOD_JAR_DIR) -m $(MOD_NAME)

$(BUILD_JMOD): $(BUILD_JMOD_DIR) compile-module
	-rm $@
	jmod create --class-path $(BUILD_MODULE_COMPILE_DIR) --main-class $(MAIN_CLASS) $(BUILD_JMOD)

jmod: $(BUILD_JMOD)

$(BUILD_JLINK_DIR)/release: jmod
	-rm -rf $(BUILD_JLINK_DIR)
	jlink --module-path $(BUILD_JMOD_DIR):$(JAVA_HOME)/jmods --add-modules $(MOD_NAME) --output $(BUILD_JLINK_DIR)

jlink: $(BUILD_JLINK_DIR)/release

run-jlink: jlink
	$(BUILD_JLINK_DIR)/bin/$(MOD_NAME)
