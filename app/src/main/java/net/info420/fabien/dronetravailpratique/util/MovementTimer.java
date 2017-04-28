package net.info420.fabien.dronetravailpratique.util;

import android.os.CountDownTimer;
import android.util.Log;

import net.info420.fabien.dronetravailpratique.application.ApplicationDrone;

import java.util.List;

import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIVirtualStickFlightControlData;
import dji.common.util.DJICommonCallbacks;

/**
 * Created by fabien on 17-03-22.
 */

public class MovementTimer extends CountDownTimer {
  private final static String TAG = MovementTimer.class.getName();

  private float mPitch = 0;
  private float mRoll = 0;
  private float mYaw = 0;
  private float mThrottle = 0;
  private List<MovementTimer> mNextMovementTimers = null;

  private String mName; // TODO : Temporaire, pour les tests

  public MovementTimer(String name, long millisInFuture, long countDownInterval, float pitch, float roll, float yaw, float throttle) {
    super(millisInFuture, countDownInterval);
    mPitch    = pitch;
    mRoll     = roll;
    mYaw      = yaw;
    mThrottle = throttle;
    mName     = name;

    Log.d(TAG, String.format("MovementTimer %s : create %s pitch %s roll %s yaw %s throttle : %s %s", mName, pitch, roll, yaw, throttle, millisInFuture, countDownInterval));
  }

  public void setNextMovementTimers(List<MovementTimer> nextMovementTimers) {
    mNextMovementTimers = nextMovementTimers;
  }

  @Override
  public void onTick(long l) {
    Log.d(TAG, String.format("MovementTimer %s : onTick", mName));
    if (ApplicationDrone.isFlightControllerAvailable()) {
      Log.d(TAG, String.format("MovementTimer %s : Tentative de mouvement avec pitch %s roll %s yaw %s throttle %s", mName, mPitch, mRoll, mYaw, mThrottle));

      ApplicationDrone.getAircraftInstance().getFlightController().sendVirtualStickFlightControlData(
        new DJIVirtualStickFlightControlData(
          mPitch, mRoll, mYaw, mThrottle
        ), new DJICommonCallbacks.DJICompletionCallback() {
          @Override
          public void onResult(DJIError djiError) {
            if (djiError != null) {
              Log.d(TAG, String.format("MovementTimer %s : Erreur de mouvement avec pitch %s roll %s yaw %s throttle %s : %s", mName, mPitch, mRoll, mYaw, mThrottle, djiError.getDescription()));
            } else {
              Log.d(TAG, String.format("MovementTimer %s : Mouvement avec pitch %s roll %s yaw %s throttle %s", mName, mPitch, mRoll, mYaw, mThrottle));
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
    Log.d(TAG, String.format("MovementTimer %s : onFinish", mName));

    // Si la List de MovementTimer n'est pas null (le timer actuel n'est pas le dernier d'une liste)
    if ((mNextMovementTimers != null) && (mNextMovementTimers.size() != 0)) {
      Log.d(TAG, String.format("MovementTimer %s : mNextMovementTimers != null", mName));
      // Prochain timer
      MovementTimer nextMovementTimer = mNextMovementTimers.get(0);

      // S'il reste plus qu'un prochain timer (si la liste est size = 1, alors la liste doit être null)
      if (mNextMovementTimers.size() > 1) {
        Log.d(TAG, String.format("MovementTimer %s : pas le dernier", mName));
        // Si le prochain timer n'est pas le dernier, alors on lui envoie la List de MovementTimer, moins lui-même
        nextMovementTimer.setNextMovementTimers(mNextMovementTimers.subList(1, (mNextMovementTimers.size() - 1)));
      } else {
        Log.d(TAG, String.format("MovementTimer %s : CECI NE DOIT JAMAIS ARRIVER dernier", mName));
      }

      // Sinon, sa liste est null

      // On débute le timer
      nextMovementTimer.start();
    } else {
      Log.d(TAG, String.format("MovementTimer %s : dernier lol wut", mName));
      // Sinon, on arrête tout.
      if (ApplicationDrone.isFlightControllerAvailable()) {
        ApplicationDrone.getAircraftInstance().getFlightController().sendVirtualStickFlightControlData(
          new DJIVirtualStickFlightControlData(
            0, 0, 0, 0
          ), new DJICommonCallbacks.DJICompletionCallback() {
            @Override
            public void onResult(DJIError djiError) {
              if (djiError != null) {
                Log.e(TAG, String.format("MovementTimer %s :Erreur de mouvement à zéro : %s", mName, djiError.getDescription()));
              } else {
                Log.d(TAG, String.format("MovementTimer %s : Mouvement à zéro", mName));
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