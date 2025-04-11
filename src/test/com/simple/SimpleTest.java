package com.simple;

import android.util.Log;
import androidx.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SimpleTest {

  @Test
  public void runTest() {
    Log.d("SimpleTest", "runTest");
    if (this.getClass() != null) {
      throw new IllegalStateException("run test!");
    }
  }
}
