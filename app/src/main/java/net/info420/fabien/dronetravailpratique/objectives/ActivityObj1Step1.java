package net.info420.fabien.dronetravailpratique.objectives;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.common.ApplicationDrone;

import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;

public class ActivityObj1Step1 extends AppCompatActivity {

  public static final String TAG = ActivityObj1Step1.class.getName();

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
    land();
  }

  private void initUI(){
    setContentView(R.layout.activity_obj1_step1);

    mBtnStart = (Button) findViewById(R.id.btn_obj1_step1_start);
    mBtnStop = (Button) findViewById(R.id.btn_obj1_step1_stop);

    mBtnStart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(TAG, "mBtnStart : onClick()");
        start();
      }
    });

    mBtnStop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(TAG, "mBtnStop : onClick()");
        land();
      }
    });
  }

  private void start() {
    // TAKE OFF
    ApplicationDrone.getAircraftInstance().getFlightController().takeOff(
      new DJICommonCallbacks.DJICompletionCallback () {
        @Override
        public void onResult(DJIError djiError) {
          // Log.e(TAG, "Takeoff error : " + djiError.getDescription());
          // Toast.makeText(ActivityObj1Step1.this, "Takeoff error : " + djiError.getDescription(), Toast.LENGTH_LONG).show();
        }
      }
    );
  }

  private void land() {
    // LANDING
    ApplicationDrone.getAircraftInstance().getFlightController().autoLanding(
      new DJICommonCallbacks.DJICompletionCallback () {
        @Override
        public void onResult(DJIError djiError) {
          // Log.e(TAG, "Landing error : " + djiError.getDescription());
          // Toast.makeText(ActivityObj1Step1.this, "Landing error : " + djiError.getDescription(), Toast.LENGTH_LONG).show();
        }
      }
    );
  }
}