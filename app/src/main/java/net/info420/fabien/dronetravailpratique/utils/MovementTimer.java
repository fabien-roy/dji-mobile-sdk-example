package net.info420.fabien.dronetravailpratique.utils;

import android.os.CountDownTimer;
import android.util.Log;

import net.info420.fabien.dronetravailpratique.common.ApplicationDrone;

import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIVirtualStickFlightControlData;
import dji.common.util.DJICommonCallbacks;

/**
 * Created by fabien on 17-03-22.
 */

class MovementTimer extends CountDownTimer {
  private final static String TAG = MovementTimer.class.getName();

  private float mPitch = 0;
  private float mRoll = 0;
  private float mYaw = 0;
  private float mThrottle = 0;
  private MovementTimer mNextMovementTimer;

  public MovementTimer(long millisInFuture, long countDownInterval, float pitch, float roll, float yaw, float throttle) {
    super(millisInFuture, countDownInterval);
    mPitch = pitch;
    mRoll = roll;
    mYaw = yaw;
    mThrottle = throttle;
    mNextMovementTimer = null;

    Log.d(TAG, String.format("MovementTimer(), with %s pitch %s roll %s yaw %s throttle", pitch, roll, yaw, throttle));
  }

  public MovementTimer(long millisInFuture, long countDownInterval, float pitch, float roll, float yaw, float throttle, MovementTimer nextMovementTimer) {
    super(millisInFuture, countDownInterval);
    mPitch = pitch;
    mRoll = roll;
    mYaw = yaw;
    mThrottle = throttle;
    mNextMovementTimer = nextMovementTimer;

    Log.d(TAG, String.format("MovementTimer(), with %s pitch %s roll %s yaw %s throttle", pitch, roll, yaw, throttle));
  }

  @Override
  public void onTick(long l) {
    Log.d(TAG, "onTick()");
    if (ApplicationDrone.isFlightControllerAvailable()) {
      Log.d(TAG, String.format("Tentative de mouvement avec pitch %s roll %s yaw %s throttle %s", mPitch, mRoll, mYaw, mThrottle));

      ApplicationDrone.getAircraftInstance().getFlightController().sendVirtualStickFlightControlData(
        new DJIVirtualStickFlightControlData(
          mPitch, mRoll, mYaw, mThrottle
        ), new DJICommonCallbacks.DJICompletionCallback() {
          @Override
          public void onResult(DJIError djiError) {
            if (djiError != null) {
              Log.d(TAG, String.format("Erreur de mouvement avec pitch %s roll %s yaw %s throttle %s : %s", mPitch, mRoll, mYaw, mThrottle, djiError.getDescription()));
            } else {
              Log.d(TAG, String.format("Mouvement avec pitch %s roll %s yaw %s throttle %s", mPitch, mRoll, mYaw, mThrottle));
            }
          }
        }
      );
    } else {
      Log.d(TAG, "flightController is NOT available");
    }
  }

  @Override
  public void onFinish() {
    Log.d(TAG, "onFinish()");

    if (mNextMovementTimer != null) {
      mNextMovementTimer.start();
    } else {
      if (ApplicationDrone.isFlightControllerAvailable()) {
        ApplicationDrone.getAircraftInstance().getFlightController().sendVirtualStickFlightControlData(
          new DJIVirtualStickFlightControlData(
            0, 0, 0, 0
          ), new DJICommonCallbacks.DJICompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
              if (djiError != null) {
                Log.e(TAG, String.format("Erreur de mouvement à zéro : %s", djiError.getDescription()));
              } else {
                Log.d(TAG, "Mouvement à zéro");
              }
            }
          }
        );
      }else {
        Log.d(TAG, "flightController is NOT available");
      }
    }
  }
}