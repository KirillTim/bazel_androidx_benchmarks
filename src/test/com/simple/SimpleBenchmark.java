package com.simple;

import android.util.Log;
import androidx.benchmark.BenchmarkState;
import androidx.benchmark.junit4.BenchmarkRule;
import androidx.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SimpleBenchmark {
  @Rule public BenchmarkRule benchmarkRule = new BenchmarkRule();

  @Test
  public void runBenchmark() {
    Log.d("SimpleBenchmark", "runBenchmark");
    BenchmarkState state = benchmarkRule.getState();
    while (state.keepRunning()) {
      Log.d("LogBenchmark", "the cost of writing this log method will be measured");
    }
  }
}
