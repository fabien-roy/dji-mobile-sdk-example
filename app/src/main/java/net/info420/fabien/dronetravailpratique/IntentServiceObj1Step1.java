package net.info420.fabien.dronetravailpratique;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class IntentServiceObj1Step1 extends IntentService {
  public final String TAG = "IntentServiceObj1Step1";

  public IntentServiceObj1Step1() {
    super("IntentServiceObj1Step1");
    Log.d(TAG, "onCreate()");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent != null) {

    }
  }
}
