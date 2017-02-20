package net.info420.fabien.dronetravailpratique.objectives;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.common.ApplicationDrone;

import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIVirtualStickFlightControlData;
import dji.common.util.DJICommonCallbacks;
import dji.common.util.DJICommonCallbacks.DJICompletionCallback;

/**
 * Created by fabien on 17-02-20.
 */

public class ActivityObj1Step2 extends AppCompatActivity {

  public static final String TAG = ActivityObj1Step2.class.getName();

  private Button mBtnStart;
  private Button mBtnStop;
  private Button mBtnForward;
  private Button mBtnBack;
  private Button mBtnTurnRight;
  private Button mBtnTurnLeft;

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

    mBtnStart = (Button) findViewById(R.id.btn_obj1_step2_start);
    mBtnStop = (Button) findViewById(R.id.btn_obj1_step2_stop);
    mBtnForward = (Button) findViewById(R.id.btn_obj1_step2_go_forward);
    mBtnBack = (Button) findViewById(R.id.btn_obj1_step2_go_back);
    mBtnTurnRight = (Button) findViewById(R.id.btn_obj1_step2_turn_right);
    mBtnTurnLeft = (Button) findViewById(R.id.btn_obj1_step2_turn_left);

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

    mBtnForward.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(TAG, "mBtnForward : onClick()");
        goForward();
      }
    });

    mBtnBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(TAG, "mBtnBack : onClick()");
        goBack();
      }
    });

    mBtnTurnLeft.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(TAG, "mBtnTurnLeft : onClick()");
        turnLeft();
      }
    });

    mBtnTurnRight.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(TAG, "mBtnTurnRight : onClick()");
        turnRight();
      }
    });
  }

  private void start() {
    // TAKE OFF
    ApplicationDrone.getAircraftInstance().getFlightController().takeOff(
      new DJICommonCallbacks.DJICompletionCallback () {
        @Override
        public void onResult(DJIError djiError) {
          Log.e(TAG, "Takeoff error : " + djiError.getDescription());
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
          Log.e(TAG, "landing error : " + djiError.getDescription());
        }
      }
    );
  }

  private void goForward() {

    if (ApplicationDrone.isFlightControllerAvailable()) {
      ApplicationDrone.getAircraftInstance().
        getFlightController().sendVirtualStickFlightControlData(
        new DJIVirtualStickFlightControlData(
          1, 0, 0, 1 // pitch roll yaw throttle
        ), new DJICompletionCallback() {
          @Override
          public void onResult(DJIError djiError) {

          }
        }
      );
    }
  }

  private void goBack() {

    if (ApplicationDrone.isFlightControllerAvailable()) {
      ApplicationDrone.getAircraftInstance().
        getFlightController().sendVirtualStickFlightControlData(
        new DJIVirtualStickFlightControlData(
          -1, 0, 0, 1 // pitch roll yaw throttle
        ), new DJICompletionCallback() {
          @Override
          public void onResult(DJIError djiError) {

          }
        }
      );
    }
  }

  private void turnLeft() {

    if (ApplicationDrone.isFlightControllerAvailable()) {
      ApplicationDrone.getAircraftInstance().
        getFlightController().sendVirtualStickFlightControlData(
        new DJIVirtualStickFlightControlData(
          0, 0, -1, 0 // pitch roll yaw throttle
        ), new DJICompletionCallback() {
          @Override
          public void onResult(DJIError djiError) {

          }
        }
      );
    }
  }

  private void turnRight() {

    if (ApplicationDrone.isFlightControllerAvailable()) {
      ApplicationDrone.getAircraftInstance().
        getFlightController().sendVirtualStickFlightControlData(
        new DJIVirtualStickFlightControlData(
          0, 0, 1, 0 // pitch roll yaw throttle
        ), new DJICompletionCallback() {
          @Override
          public void onResult(DJIError djiError) {

          }
        }
      );
    }
  }
}