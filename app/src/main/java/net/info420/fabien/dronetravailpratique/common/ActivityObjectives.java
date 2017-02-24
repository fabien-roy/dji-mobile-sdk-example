package net.info420.fabien.dronetravailpratique.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.objectives.ActivityObj1Step1;
import net.info420.fabien.dronetravailpratique.objectives.ActivityObj1Step2;

/**
 * Created by fabien on 17-02-15.
 */

public class ActivityObjectives extends Activity {

  public static final String TAG = ActivityObjectives.class.getName();

  private Button mBtnObj1Step1;
  private Button mBtnObj1Step2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initUI();

    Log.d(TAG, "onCreate()");
  }

  private void initUI(){
    setContentView(R.layout.activity_objectives);

    mBtnObj1Step1 = (Button) findViewById(R.id.btn_obj1_step1);
    mBtnObj1Step2 = (Button) findViewById(R.id.btn_obj1_step2);

    mBtnObj1Step1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), ActivityObj1Step1.class));
      }
    });

    mBtnObj1Step2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), ActivityObj1Step2.class));
      }
    });
  }
}