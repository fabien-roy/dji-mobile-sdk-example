package net.info420.fabien.dronetravailpratique.objectives;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.common.ApplicationDrone;

import java.util.TimerTask;

import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIVirtualStickFlightControlData;
import dji.common.flightcontroller.DJIVirtualStickFlightCoordinateSystem;
import dji.common.flightcontroller.DJIVirtualStickRollPitchControlMode;
import dji.common.flightcontroller.DJIVirtualStickVerticalControlMode;
import dji.common.flightcontroller.DJIVirtualStickYawControlMode;
import dji.common.util.DJICommonCallbacks;
import dji.common.util.DJICommonCallbacks.DJICompletionCallback;

/**
 * Created by fabien on 17-02-20.
 */

public class ActivityObj1Step2 extends AppCompatActivity {

  public static final String TAG = ActivityObj1Step2.class.getName();

  private Button mBtnStartMotors;
  private Button mBtnGoForward;
  private Button mBtnGoBack;
  private Button mBtnGoLeft;
  private Button mBtnGoRight;
  private Button mBtnTurnRight;
  private Button mBtnTurnLeft;

  private CountDownTimer movementTimer;
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

    // Source : https://developer.dji.com/mobile-sdk/documentation/introduction/flightController_concepts.html
    // Mode de base du drone

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

    // Mise en place du mode de vélocité pour le roll et le pitch (m/s)
    ApplicationDrone.getAircraftInstance().getFlightController().setRollPitchControlMode(DJIVirtualStickRollPitchControlMode.Velocity);

    // Mise en place du mode de vélocité pour le Throttle (m/s)
    ApplicationDrone.getAircraftInstance().getFlightController().setVerticalControlMode(DJIVirtualStickVerticalControlMode.Velocity);

    // Mise en place du mode de vélocité angulaire pour le Yaw (°/s)
    ApplicationDrone.getAircraftInstance().getFlightController().setYawControlMode(DJIVirtualStickYawControlMode.AngularVelocity);

    // Ceci permet de se servir de coordonées x, y, z qui n'utilisent pas le nord magnétique
    ApplicationDrone.getAircraftInstance().getFlightController().setHorizontalCoordinateSystem(DJIVirtualStickFlightCoordinateSystem.Body);
  }

  @Override
  protected void onDestroy() {

    if (null != movementTimer) {
      // movementTimerTask.cancel();
      // movementTimerTask = null;
      movementTimer.cancel();
      // movementTimer.purge();
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

    mBtnStartMotors = (Button) findViewById(R.id.btn_obj1_step2_start_motors);
    mBtnGoForward = (Button) findViewById(R.id.btn_obj1_step2_go_forward);
    mBtnGoBack = (Button) findViewById(R.id.btn_obj1_step2_go_back);
    mBtnGoLeft = (Button) findViewById(R.id.btn_obj1_step2_go_left);
    mBtnGoRight = (Button) findViewById(R.id.btn_obj1_step2_go_right);
    mBtnTurnRight = (Button) findViewById(R.id.btn_obj1_step2_turn_right);
    mBtnTurnLeft = (Button) findViewById(R.id.btn_obj1_step2_turn_left);

    mBtnStartMotors.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startMotors();
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

  // Ceci a été fait avec un tableau sur le site de DJI : https://developer.dji.com/mobile-sdk/documentation/introduction/component-guide-flightController.html (Roll Pitch Control Mode)
  // Important à savoir, on se sert du mode Velocity et Body

  // Rappelez vous, c'est en m/s (et °/s pour le Yaw). Mon timer dure en fonction.

  //                                                p    r    y    t

  private float[] goForward() { float[] array = {   0,   1,   0,   0}; return array; }

  private float[] goBack()    { float[] array = {   0,  -1,   0,   0}; return array; }

  private float[] goLeft()    { float[] array = {  -1,   0,   0,   0}; return array; }

  private float[] goRight()   { float[] array = {   1,   0,   0,   0}; return array; }

  private float[] turnLeft()  { float[] array = {   0,   0,  90,   0}; return array; }

  private float[] turnRight() { float[] array = {   0,   0, -90,   0}; return array; }


  private void startMotors() {
    // Il est nécéssaire de démarrer les moteurs. Ceci permet de "tester" le mouvement.
    ApplicationDrone.getAircraftInstance().getFlightController().turnOnMotors(
      new DJICommonCallbacks.DJICompletionCallback () {
        @Override
        public void onResult(DJIError djiError) {
          if (djiError != null) {
            Log.e(TAG, "Erreur de démarrage des moteurs : " + djiError.getDescription());
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
    if (null != movementTimer) {
      movementTimer.cancel();
    }

    mPitch = pitchRollYawThrottle[0];
    mRoll = pitchRollYawThrottle[1];
    mYaw = pitchRollYawThrottle[2];
    mThrottle = pitchRollYawThrottle[3];

    if (null == movementTimer) {
      // movementTimerTask = new MovementTimerTask();
      movementTimer = new MovementTimer(100, 200);
      // movementTimer.schedule(movementTimerTask, 100, 200);
      movementTimer.start();
    }
  }

  class MovementTimer extends CountDownTimer {

    public MovementTimer(long millisInFuture, long countDownInterval) {
      super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long l) {
      if (ApplicationDrone.isFlightControllerAvailable()) {

        ApplicationDrone.getAircraftInstance().getFlightController().sendVirtualStickFlightControlData(
          new DJIVirtualStickFlightControlData(
            mPitch, mRoll, mYaw, mThrottle
          ), new DJICompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
              if (djiError != null) {
                Log.e(TAG, "Erreur de mouvement avec pitch " + mPitch + " roll " + mRoll + " yaw " + mYaw + " throttle " + mThrottle + " : " + djiError.getDescription());
              } else {
                Log.d(TAG, "Mouvement avec pitch " + mPitch + " roll " + mRoll + " yaw " + mYaw + " throttle " + mThrottle);
              }
            }
          }
        );
      }
    }

    @Override
    public void onFinish() {
      if (ApplicationDrone.isFlightControllerAvailable()) {

        ApplicationDrone.getAircraftInstance().getFlightController().sendVirtualStickFlightControlData(
          new DJIVirtualStickFlightControlData(
            0, 0, 0, 0
          ), new DJICompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
              if (djiError != null) {
                Log.e(TAG, "Erreur de mouvement à zéro : " + djiError.getDescription());
              } else {
                Log.d(TAG, "Mouvement à zéro");
              }
            }
          }
        );
      }
    }
  }

  class MovementTimerTask extends TimerTask {
    // Solution pour arrêter le timer après 1 seconde
    // Source : http://stackoverflow.com/questions/15894731/how-to-stop-the-timer-after-certain-time

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
                Log.e(TAG, "Erreur de mouvement avec pitch " + mPitch + " roll " + mRoll + " yaw " + mYaw + " throttle " + mThrottle + " : " + djiError.getDescription());
              } else {
                Log.d(TAG, "Mouvement avec pitch " + mPitch + " roll " + mRoll + " yaw " + mYaw + " throttle " + mThrottle);
              }
            }
          }
        );
      }
    }
  }
}