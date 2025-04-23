# We already have the same setup for 'rules_android', see 'MODULE.bazel' file,
# however, 'rules_jvm_external' don't support Android SDK being set by 'rules_android'.
# So we need to set it here as well.
android_sdk_repository(
    name = "androidsdk",
)

# Order for 'load' and 'setup' statements is important, do not change!
load("@rules_jvm_external//:repositories.bzl", "rules_jvm_external_deps")

rules_jvm_external_deps()

load("@rules_jvm_external//:setup.bzl", "rules_jvm_external_setup")

rules_jvm_external_setup()

load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "androidx.test:runner:1.6.2",
        "androidx.test:monitor:1.7.2",
        "junit:junit:4.13.2",
        "androidx.benchmark:benchmark-junit4:1.3.4",
        "androidx.benchmark:benchmark-common:1.3.4",
        "com.squareup.wire:wire-runtime-jvm:4.9.7",
    ],
    repositories = [
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ],
)

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_file")

http_file(
    name = "androidix_benchmark_common_from_http",
    url = "https://maven.google.com/androidx/benchmark/benchmark-common/1.3.4/benchmark-common-1.3.4.aar",
    sha256 = "4cad4af6a1fab90ce1a8e0a731fa866648743e2d6075985c8d22ccc8d05e92eb",
    downloaded_file_path = "downloaded_benchmark_common_1_3_4.aar",
)