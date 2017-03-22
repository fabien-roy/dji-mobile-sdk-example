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

  private Float mPitch;
  private Float mRoll;
  private Float mYaw;
  private Float mThrottle;

  public MovementTimer(long millisInFuture, long countDownInterval, float pitch, float roll, float yaw, float throttle) {
    super(millisInFuture, countDownInterval);
    mPitch = pitch;
    mRoll = roll;
    mYaw = yaw;
    mThrottle = throttle;
  }

  @Override
  public void onTick(long l) {
    if (ApplicationDrone.isFlightControllerAvailable()) {

      ApplicationDrone.getAircraftInstance().getFlightController().sendVirtualStickFlightControlData(
        new DJIVirtualStickFlightControlData(
          mPitch, mRoll, mYaw, mThrottle
        ), new DJICommonCallbacks.DJICompletionCallback() {
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
        ), new DJICommonCallbacks.DJICompletionCallback() {
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

      mPitch = null;
      mRoll = null;
      mYaw = null;
      mThrottle = null;
    }
  }
}