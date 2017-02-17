package net.info420.fabien.dronetravailpratique.objectives;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.info420.fabien.dronetravailpratique.R;

public class ActivityObj1Step1 extends AppCompatActivity {

  public static final String TAG = ActivityObj1Step1.class.getName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG, "onCreate()");

    initUI();
  }

  private void initUI(){
    setContentView(R.layout.activity_obj1_step1);
  }
}
