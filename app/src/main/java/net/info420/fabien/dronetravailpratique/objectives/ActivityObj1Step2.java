package net.info420.fabien.dronetravailpratique.objectives;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.common.ApplicationDrone;

import java.util.Timer;
import java.util.TimerTask;

import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIVirtualStickFlightControlData;
import dji.common.flightcontroller.DJIVirtualStickRollPitchControlMode;
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

  private Timer movementTimer;
  private MovementTimerTask movementTimerTask;

  private float mPitch;
  private float mRoll;
  private float mYaw;
  private float mThrottle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG, "onCreate()");

    initUI();

    // Activation du mode de contrôle par Virtual Stick
    ApplicationDrone.getAircraftInstance().getFlightController().enableVirtualStickControlMode(
      new DJICompletionCallback() {
        @Override
        public void onResult(DJIError djiError) {
          if (djiError != null) {
            Log.e(TAG, "Erreur d'activation du mode de contrôle par Virtual Stick : " + djiError.getDescription());
          }
        }
      }
    );

    // Mise en place du mode de vélocité pour le roll et le pitch
    ApplicationDrone.getAircraftInstance().getFlightController().
      setRollPitchControlMode(
        DJIVirtualStickRollPitchControlMode.Velocity
      );
  }

  @Override
  protected void onDestroy() {

    if (null != movementTimer) {
      movementTimerTask.cancel();
      movementTimerTask = null;
      movementTimer.cancel();
      movementTimer.purge();
      movementTimer = null;
    }

    land();

    // Désactivation du mode de controle par Virtual Stick
    ApplicationDrone.getAircraftInstance().getFlightController().disableVirtualStickControlMode(
      new DJICompletionCallback() {
        @Override
        public void onResult(DJIError djiError) {
          if (djiError != null) {
            Log.e(TAG, "Erreur de désactivation du mode de contrôle par Virtual Stick : " + djiError.getDescription());
          }
        }
      }
    );
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

  private float[] goForward() { float[] array = { -10,   0,   0,   0}; return array; }

  private float[] goBack()    { float[] array = {  10,   0,   0,   0}; return array; }

  private float[] goLeft()    { float[] array = {   0,  10,   0,   0}; return array; }

  private float[] goRight()   { float[] array = {   0, -10,   0,   0}; return array; }

  private float[] turnLeft()  { float[] array = {   0,   0,  35,   0}; return array; }

  private float[] turnRight() { float[] array = {   0,   0, -35,   0}; return array; }


  private void start() {
    // TAKE OFF
    ApplicationDrone.getAircraftInstance().getFlightController().takeOff(
      new DJICommonCallbacks.DJICompletionCallback () {
        @Override
        public void onResult(DJIError djiError) {
          if (djiError != null) {
            Log.e(TAG, "Erreur de décollage : " + djiError.getDescription());
          }
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
          if (djiError != null) {
            Log.e(TAG, "Erreur d'atterissage : " + djiError.getDescription());
          }
        }
      }
    );
  }

  private void move(float[] pitchRollYawThrottle) {
    mPitch = pitchRollYawThrottle[0];
    mRoll = pitchRollYawThrottle[1];
    mYaw = pitchRollYawThrottle[2];
    mThrottle = pitchRollYawThrottle[3];

    if (null == movementTimer) {
      movementTimerTask = new MovementTimerTask();
      movementTimer = new Timer();
      movementTimer.schedule(movementTimerTask, 0, 200);
    }
  }

  class MovementTimerTask extends TimerTask {

    @Override
    public void run() {

      // On vérifie si le flightController est O.K.
      if (ApplicationDrone.isFlightControllerAvailable()) {

        ApplicationDrone.getAircraftInstance().getFlightController().sendVirtualStickFlightControlData(
          new DJIVirtualStickFlightControlData(
            mPitch, mRoll, mYaw, mThrottle
          ), new DJICompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
              if (djiError != null) {
                Log.e(TAG, String.format("Erreur de mouvement avec pitch s% roll %s yaw %s throttle %s : ", mPitch, mRoll, mYaw, mThrottle) + djiError.getDescription());
              }
            }
          }
        );
      }
    }
  }
}