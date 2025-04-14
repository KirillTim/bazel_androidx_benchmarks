# bazel_androidx_benchmarks

This sample app shows the problem when you build an android app that depends on the `androidx.benchmark` and try to run it.
The problem is that the final APK does not include classes from the package `com.squareup.wire` (and some others) on which the app depends.

There different steps to reproduce that result in similar but different runtime crashes:

If you just build a test APK without any proguard file, the crash happens when you run the test:
```
bazelisk build //src/test:benchmark_app
adb install  bazel-bin/src/test/benchmark_app.apk
adb shell am instrument -w -r com.simple/androidx.benchmark.junit4.AndroidBenchmarkRunner
```

the test output is follows:

```
INSTRUMENTATION_STATUS: class=com.simple.SimpleBenchmark
INSTRUMENTATION_STATUS: current=1
INSTRUMENTATION_STATUS: id=AndroidJUnitRunner
INSTRUMENTATION_STATUS: numtests=1
INSTRUMENTATION_STATUS: stream=
com.simple.SimpleBenchmark:
INSTRUMENTATION_STATUS: test=runBenchmark
INSTRUMENTATION_STATUS_CODE: 1
INSTRUMENTATION_STATUS: class=com.simple.SimpleBenchmark
INSTRUMENTATION_STATUS: current=1
INSTRUMENTATION_STATUS: id=AndroidJUnitRunner
INSTRUMENTATION_STATUS: numtests=1
INSTRUMENTATION_STATUS: stack=java.lang.NoClassDefFoundError: Failed resolution of: Lcom/squareup/wire/WireEnum;
	at androidx.benchmark.perfetto.PerfettoCapture.start(PerfettoCapture.kt:257)
	at androidx.benchmark.perfetto.PerfettoCaptureWrapper.start(PerfettoCaptureWrapper.kt:76)
	at androidx.benchmark.perfetto.PerfettoCaptureWrapper.record(PerfettoCaptureWrapper.kt:131)
	....
	at androidx.test.runner.AndroidJUnitRunner.onStart(AndroidJUnitRunner.java:463)
	at android.app.Instrumentation$InstrumentationThread.run(Instrumentation.java:2594)
Caused by: java.lang.ClassNotFoundException: Didn't find class "com.squareup.wire.WireEnum" on path: DexPathList[[zip file "/data/app/~~qtSw-_VulXjivEoSyaylKw==/com.simple-uzKHvVeH2yS7sUoVxI7FyQ==/base.apk"],nativeLibraryDirectories=[/data/app/~~qtSw-_VulXjivEoSyaylKw==/com.simple-uzKHvVeH2yS7sUoVxI7FyQ==/lib/x86_64, /system/lib64, /system_ext/lib64]]
	at dalvik.system.BaseDexClassLoader.findClass(BaseDexClassLoader.java:259)
	at java.lang.ClassLoader.loadClass(ClassLoader.java:637)
	at java.lang.ClassLoader.loadClass(ClassLoader.java:573)
	... 35 more

INSTRUMENTATION_STATUS: stream=
Error in runBenchmark(com.simple.SimpleBenchmark):
java.lang.NoClassDefFoundError: Failed resolution of: Lcom/squareup/wire/WireEnum;
	at androidx.benchmark.perfetto.PerfettoCapture.start(PerfettoCapture.kt:257)
	at androidx.benchmark.perfetto.PerfettoCaptureWrapper.start(PerfettoCaptureWrapper.kt:76)
	....
	at androidx.test.runner.AndroidJUnitRunner.onStart(AndroidJUnitRunner.java:463)
	at android.app.Instrumentation$InstrumentationThread.run(Instrumentation.java:2594)
Caused by: java.lang.ClassNotFoundException: Didn't find class "com.squareup.wire.WireEnum" on path: DexPathList[[zip file "/data/app/~~qtSw-_VulXjivEoSyaylKw==/com.simple-uzKHvVeH2yS7sUoVxI7FyQ==/base.apk"],nativeLibraryDirectories=[/data/app/~~qtSw-_VulXjivEoSyaylKw==/com.simple-uzKHvVeH2yS7sUoVxI7FyQ==/lib/x86_64, /system/lib64, /system_ext/lib64]]
	at dalvik.system.BaseDexClassLoader.findClass(BaseDexClassLoader.java:259)
	at java.lang.ClassLoader.loadClass(ClassLoader.java:637)
	at java.lang.ClassLoader.loadClass(ClassLoader.java:573)
	... 35 more

INSTRUMENTATION_STATUS: test=runBenchmark
INSTRUMENTATION_STATUS_CODE: -2
INSTRUMENTATION_STATUS: class=com.simple.SimpleBenchmark
INSTRUMENTATION_STATUS: current=1
INSTRUMENTATION_STATUS: id=AndroidJUnitRunner
INSTRUMENTATION_STATUS: numtests=1
INSTRUMENTATION_STATUS: stack=java.lang.RuntimeException: Could not launch activity
	at androidx.test.runner.MonitoringInstrumentation.startActivitySync(MonitoringInstrumentation.java:551)
	at androidx.benchmark.IsolationActivity$Companion.launchSingleton(IsolationActivity.kt:140)
	at androidx.benchmark.junit4.AndroidBenchmarkRunner.waitForActivitiesToComplete(AndroidBenchmarkRunner.kt:85)
	at androidx.test.runner.MonitoringInstrumentation.finish(MonitoringInstrumentation.java:402)
	at androidx.test.runner.AndroidJUnitRunner.onStart(AndroidJUnitRunner.java:474)
	at android.app.Instrumentation$InstrumentationThread.run(Instrumentation.java:2594)
Caused by: java.lang.RuntimeException: Unable to resolve activity for: Intent { act=android.intent.action.MAIN flg=0x30000000 cmp=com.simple/androidx.benchmark.IsolationActivity }
	at android.app.Instrumentation.startActivitySync(Instrumentation.java:607)
	at android.app.Instrumentation.startActivitySync(Instrumentation.java:564)
	at androidx.test.runner.MonitoringInstrumentation.access$201(MonitoringInstrumentation.java:102)
	at androidx.test.runner.MonitoringInstrumentation$4.call(MonitoringInstrumentation.java:527)
	at androidx.test.runner.MonitoringInstrumentation$4.call(MonitoringInstrumentation.java:524)
	at java.util.concurrent.FutureTask.run(FutureTask.java:264)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:644)
	at java.lang.Thread.run(Thread.java:1012)

INSTRUMENTATION_STATUS: stream=
Process crashed while executing runBenchmark(com.simple.SimpleBenchmark):
java.lang.RuntimeException: Could not launch activity
	at androidx.test.runner.MonitoringInstrumentation.startActivitySync(MonitoringInstrumentation.java:551)
	at androidx.benchmark.IsolationActivity$Companion.launchSingleton(IsolationActivity.kt:140)
	at androidx.benchmark.junit4.AndroidBenchmarkRunner.waitForActivitiesToComplete(AndroidBenchmarkRunner.kt:85)
	at androidx.test.runner.MonitoringInstrumentation.finish(MonitoringInstrumentation.java:402)
	at androidx.test.runner.AndroidJUnitRunner.onStart(AndroidJUnitRunner.java:474)
	at android.app.Instrumentation$InstrumentationThread.run(Instrumentation.java:2594)
Caused by: java.lang.RuntimeException: Unable to resolve activity for: Intent { act=android.intent.action.MAIN flg=0x30000000 cmp=com.simple/androidx.benchmark.IsolationActivity }
	at android.app.Instrumentation.startActivitySync(Instrumentation.java:607)
	at android.app.Instrumentation.startActivitySync(Instrumentation.java:564)
	at androidx.test.runner.MonitoringInstrumentation.access$201(MonitoringInstrumentation.java:102)
	at androidx.test.runner.MonitoringInstrumentation$4.call(MonitoringInstrumentation.java:527)
	at androidx.test.runner.MonitoringInstrumentation$4.call(MonitoringInstrumentation.java:524)
	at java.util.concurrent.FutureTask.run(FutureTask.java:264)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1145)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:644)
	at java.lang.Thread.run(Thread.java:1012)

INSTRUMENTATION_STATUS: test=runBenchmark
INSTRUMENTATION_STATUS_CODE: -2
INSTRUMENTATION_RESULT: shortMsg=Process crashed.
INSTRUMENTATION_CODE: 0

```

But if you provide some custom progurard file, e.g. the one generated by the Android Studio when add benchmark module, the following happens:

Run the build:
```
bazelisk build //src/test:benchmark_app

```
and the output lines are:

```
INFO: From R8 Optimizing, Desugaring, and Dexing //src/test:benchmark_app:
Warning: Missing class com.squareup.wire.EnumAdapter (referenced from: void perfetto.protos.AndroidPowerConfig$BatteryCounters$Companion$ADAPTER$1.<init>(kotlin.reflect.KClass, com.squareup.wire.Syntax, perfetto.protos.AndroidPowerConfig$BatteryCounters) and 13 other contexts)
Missing class com.squareup.wire.FieldEncoding (referenced from: void perfetto.protos.AndroidPowerConfig$Companion$ADAPTER$1.<init>(com.squareup.wire.FieldEncoding, kotlin.reflect.KClass, com.squareup.wire.Syntax) and 55 other contexts)
Missing class com.squareup.wire.Message (referenced from: void perfetto.protos.AndroidPowerConfig.<init>(java.lang.Integer, java.util.List, java.lang.Boolean, java.lang.Boolean, okio.ByteString) and 72 other contexts)
Missing class com.squareup.wire.ProtoAdapter (referenced from: com.squareup.wire.ProtoAdapter perfetto.protos.AndroidPowerConfig$BatteryCounters.ADAPTER and 153 other contexts)
Missing class com.squareup.wire.Syntax (referenced from: void perfetto.protos.AndroidPowerConfig$BatteryCounters$Companion$ADAPTER$1.<init>(kotlin.reflect.KClass, com.squareup.wire.Syntax, perfetto.protos.AndroidPowerConfig$BatteryCounters) and 69 other contexts)
Missing class com.squareup.wire.WireEnum (referenced from: void perfetto.protos.AndroidPowerConfig$BatteryCounters$Companion$ADAPTER$1.<init>(kotlin.reflect.KClass, com.squareup.wire.Syntax, perfetto.protos.AndroidPowerConfig$BatteryCounters) and 17 other contexts)
Missing class com.squareup.wire.WireField$Label (referenced from: java.util.List perfetto.protos.AndroidPowerConfig.battery_counters and 35 other contexts)
Missing class com.squareup.wire.WireField (referenced from: java.util.List perfetto.protos.AndroidPowerConfig.battery_counters and 182 other contexts)
Missing class com.squareup.wire.internal.Internal (referenced from: void perfetto.protos.AndroidPowerConfig.<init>(java.lang.Integer, java.util.List, java.lang.Boolean, java.lang.Boolean, okio.ByteString) and 14 other contexts)
```

If you then try to run the test it fails with the missing classes in test runner:
```
INSTRUMENTATION_STATUS: class=com.simple.SimpleBenchmark
INSTRUMENTATION_STATUS: current=1
INSTRUMENTATION_STATUS: id=AndroidJUnitRunner
INSTRUMENTATION_STATUS: numtests=1
INSTRUMENTATION_STATUS: stream=
com.simple.SimpleBenchmark:
INSTRUMENTATION_STATUS: test=initializationError
INSTRUMENTATION_STATUS_CODE: 1
INSTRUMENTATION_STATUS: class=com.simple.SimpleBenchmark
INSTRUMENTATION_STATUS: current=1
INSTRUMENTATION_STATUS: id=AndroidJUnitRunner
INSTRUMENTATION_STATUS: numtests=1
INSTRUMENTATION_STATUS: stack=java.lang.RuntimeException: java.lang.NoSuchMethodException: androidx.test.runner.lifecycle.Stage.values []
	at java.lang.Enum.enumValues(Enum.java:313)
	at java.lang.Enum.-$$Nest$smenumValues(Unknown Source:0)
	at java.lang.Enum$1.create(Enum.java:320)
	at java.lang.Enum$1.create(Enum.java:318)
	at libcore.util.BasicLruCache.get(BasicLruCache.java:63)
	at java.lang.Enum.getSharedConstants(Enum.java:332)
	at java.lang.Class.getEnumConstantsShared(Class.java:4118)
	at java.util.EnumSet.getUniverse(EnumSet.java:410)
	at java.util.EnumSet.noneOf(EnumSet.java:112)
	at java.util.EnumSet.range(EnumSet.java:364)
	at androidx.test.internal.runner.junit3.AndroidTestSuite$3.run(Unknown Source:16)
	at androidx.test.runner.MonitoringInstrumentation$5.run(SourceFile:0)
	at android.os.Handler.handleCallback(Handler.java:959)
	at android.os.Handler.dispatchMessage(Handler.java:100)
	at android.os.Looper.loopOnce(Looper.java:232)
	at android.os.Looper.loop(Looper.java:317)
	at android.app.ActivityThread.main(ActivityThread.java:8705)
	at java.lang.reflect.Method.invoke(Native Method)
	at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:580)
	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:886)
Caused by: java.lang.NoSuchMethodException: androidx.test.runner.lifecycle.Stage.values []
	at java.lang.Class.getMethod(Class.java:2950)
	at java.lang.Class.getDeclaredMethod(Class.java:2929)
	at java.lang.Enum.enumValues(Enum.java:310)
	... 19 more

INSTRUMENTATION_STATUS: stream=
Process crashed while executing initializationError(com.simple.SimpleBenchmark):
java.lang.RuntimeException: java.lang.NoSuchMethodException: androidx.test.runner.lifecycle.Stage.values []
	at java.lang.Enum.enumValues(Enum.java:313)
	at java.lang.Enum.-$$Nest$smenumValues(Unknown Source:0)
	at java.lang.Enum$1.create(Enum.java:320)
	at java.lang.Enum$1.create(Enum.java:318)
	at libcore.util.BasicLruCache.get(BasicLruCache.java:63)
	at java.lang.Enum.getSharedConstants(Enum.java:332)
	at java.lang.Class.getEnumConstantsShared(Class.java:4118)
	at java.util.EnumSet.getUniverse(EnumSet.java:410)
	at java.util.EnumSet.noneOf(EnumSet.java:112)
	at java.util.EnumSet.range(EnumSet.java:364)
	at androidx.test.internal.runner.junit3.AndroidTestSuite$3.run(Unknown Source:16)
	at androidx.test.runner.MonitoringInstrumentation$5.run(SourceFile:0)
	at android.os.Handler.handleCallback(Handler.java:959)
	at android.os.Handler.dispatchMessage(Handler.java:100)
	at android.os.Looper.loopOnce(Looper.java:232)
	at android.os.Looper.loop(Looper.java:317)
	at android.app.ActivityThread.main(ActivityThread.java:8705)
	at java.lang.reflect.Method.invoke(Native Method)
	at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:580)
	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:886)
Caused by: java.lang.NoSuchMethodException: androidx.test.runner.lifecycle.Stage.values []
	at java.lang.Class.getMethod(Class.java:2950)
	at java.lang.Class.getDeclaredMethod(Class.java:2929)
	at java.lang.Enum.enumValues(Enum.java:310)
	... 19 more

INSTRUMENTATION_STATUS: test=initializationError
INSTRUMENTATION_STATUS_CODE: -2
INSTRUMENTATION_RESULT: shortMsg=Process crashed.
INSTRUMENTATION_CODE: 0

```

When running the aquery (`bazelisk aquery '//src/test:benchmark_app' > /tmp/build_aquery.txt`) there is no `com.squareup_*` jars in input of action `action 'R8 Optimizing, Desugaring, and Dexing //src/test:benchmark_app'`.

My first guess was that the jars that were marked as `runtime` deps are not passed to the r8 and thus are not bundled to the resulting APK, but the 
`com.squareup.wire:wire-runtime` is a `compile` dependency of benchmark-common, see pom.xml here: https://mvnrepository.com/artifact/androidx.benchmark/benchmark-common/1.3.4.