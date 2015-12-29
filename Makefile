
check:
	./gradlew --info clean check connectedDebugAndroidTest bintrayUpload

publish: check
	./gradlew -PdryRun=false --info library:bintrayUpload
	./gradlew releng
