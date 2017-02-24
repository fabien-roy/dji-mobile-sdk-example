package net.info420.fabien.dronetravailpratique.objectives;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.common.ApplicationDrone;

import java.util.Timer;
import java.util.TimerTask;

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
  private Button mBtnGoForward;
  private Button mBtnGoBack;
  private Button mBtnGoLeft;
  private Button mBtnGoRight;
  private Button mBtnTurnRight;
  private Button mBtnTurnLeft;

  private Timer mSendVirtualStickDataTimer;
  private SendVirtualStickDataTask mSendVirtualStickDataTask;

  private float mPitch;
  private float mRoll;
  private float mYaw;
  private float mThrottle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG, "onCreate()");

    initUI();
  }

  @Override
  protected void onDestroy() {

    if (null != mSendVirtualStickDataTimer) {
      mSendVirtualStickDataTask.cancel();
      mSendVirtualStickDataTask = null;
      mSendVirtualStickDataTimer.cancel();
      mSendVirtualStickDataTimer.purge();
      mSendVirtualStickDataTimer = null;
    }

    land();
  }

  private void initUI(){
    setContentView(R.layout.activity_obj1_step2);

    mBtnStart = (Button) findViewById(R.id.btn_obj1_step2_start);
    mBtnStop = (Button) findViewById(R.id.btn_obj1_step2_stop);
    mBtnGoForward = (Button) findViewById(R.id.btn_obj1_step2_go_forward);
    mBtnGoBack = (Button) findViewById(R.id.btn_obj1_step2_go_back);
    mBtnGoLeft = (Button) findViewById(R.id.btn_obj1_step2_go_left);
    mBtnGoRight = (Button) findViewById(R.id.btn_obj1_step2_go_right);
    mBtnTurnRight = (Button) findViewById(R.id.btn_obj1_step2_turn_right);
    mBtnTurnLeft = (Button) findViewById(R.id.btn_obj1_step2_turn_left);

    mBtnStart.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        start();
      }
    });

    mBtnStop.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        land();
      }
    });

    mBtnGoForward.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        move(goForward());
      }
    });

    mBtnGoBack.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        move(goBack());
      }
    });

    mBtnGoLeft.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        move(goLeft());
      }
    });

    mBtnGoRight.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        move(goRight());
      }
    });

    mBtnTurnLeft.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        move(turnLeft());
      }
    });

    mBtnTurnRight.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        move(turnRight());
      }
    });
  }

  //                                                p    r    y    t

  private float[] goForward() { float[] array = { -35,   0,   0,   0}; return array; }

  private float[] goBack()    { float[] array = {  35,   0,   0,   0}; return array; }

  private float[] goLeft()    { float[] array = {   0,  35,   0,   0}; return array; }

  private float[] goRight()   { float[] array = {   0, -35,   0,   0}; return array; }

  private float[] turnLeft()  { float[] array = {   0,   0,  35,   0}; return array; }

  private float[] turnRight() { float[] array = {   0,   0, -35,   0}; return array; }


  private void start() {
    // TAKE OFF
    ApplicationDrone.getAircraftInstance().getFlightController().takeOff(
      new DJICommonCallbacks.DJICompletionCallback () {
        @Override
        public void onResult(DJIError djiError) {
          Log.e(TAG, "Erreur de décollage : " + djiError.getDescription().toString());
          Toast.makeText(ActivityObj1Step2.this, "Erreur de décollage : " + djiError.getDescription().toString(), Toast.LENGTH_LONG).show();
        }
      }
    );
  }

  private void land() {
    // LANDING
    ApplicationDrone.getAircraftInstance().getFlightController().autoLanding(
      new DJICommonCallbacks.DJICompletionCallback() {
        @Override
        public void onResult(DJIError djiError) {
          Log.e(TAG, "Erreur d'atterissage : " + djiError.getDescription().toString());
          Toast.makeText(ActivityObj1Step2.this, "Erreur d'atterrissage : " + djiError.getDescription().toString(), Toast.LENGTH_LONG).show();
        }
      }
    );
  }

  private void move(float[] pitchRollYawThrottle) {
    mPitch = pitchRollYawThrottle[0];
    mRoll = pitchRollYawThrottle[1];
    mYaw = pitchRollYawThrottle[2];
    mThrottle = pitchRollYawThrottle[3];

    if (null == mSendVirtualStickDataTimer) {
      mSendVirtualStickDataTask = new SendVirtualStickDataTask();
      mSendVirtualStickDataTimer = new Timer();
      mSendVirtualStickDataTimer.schedule(mSendVirtualStickDataTask, 0, 200);
    }
  }

  class SendVirtualStickDataTask extends TimerTask {

    @Override
    public void run() {
      if (ApplicationDrone.isFlightControllerAvailable()) {
        ApplicationDrone.getAircraftInstance().
          getFlightController().sendVirtualStickFlightControlData(
          new DJIVirtualStickFlightControlData(
            mPitch, mRoll, mYaw, mThrottle
          ), new DJICompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
              Log.e(TAG, "Move error : " + djiError.getDescription().toString());
              Toast.makeText(ActivityObj1Step2.this, "Move error : " + djiError.getDescription().toString(), Toast.LENGTH_LONG).show();
            }
          }
        );
      }
    }
  }
}