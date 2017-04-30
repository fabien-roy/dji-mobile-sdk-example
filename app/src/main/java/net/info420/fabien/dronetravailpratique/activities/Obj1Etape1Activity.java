package net.info420.fabien.dronetravailpratique.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.application.DroneApplication;

public class Obj1Etape1Activity extends AppCompatActivity {
  public static final String TAG = Obj1Etape1Activity.class.getName();

  private Button mBtnStart;
  private Button mBtnStop;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG, "onCreate()");

    initUI();
  }

  @Override
  protected void onDestroy(){
    super.onDestroy();

    DroneApplication.getDroneMover().land();
  }

  private void initUI(){
    setContentView(R.layout.activity_obj1_etape1);

    mBtnStart = (Button) findViewById(R.id.btn_obj1_step1_start);
    mBtnStop = (Button) findViewById(R.id.btn_obj1_step1_stop);

    mBtnStart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneMover().takeOff();
      }
    });

    mBtnStop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneMover().land();
      }
    });
  }
}