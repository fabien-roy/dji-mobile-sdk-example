package net.info420.fabien.dronetravailpratique.utils;

import android.util.Log;

import net.info420.fabien.dronetravailpratique.common.ApplicationDrone;

import dji.common.error.DJIError;
import dji.common.flightcontroller.DJIVirtualStickFlightCoordinateSystem;
import dji.common.flightcontroller.DJIVirtualStickRollPitchControlMode;
import dji.common.flightcontroller.DJIVirtualStickVerticalControlMode;
import dji.common.flightcontroller.DJIVirtualStickYawControlMode;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.flightcontroller.DJIFlightController;

/**
 * Created by fabien on 17-03-22.
 */

public class DroneMover {
  private static final String TAG = DroneMover.class.getName();

  public static final int CLOCKWISE             = 1;
  public static final int COUNTER_CLOCKWISE     = -1;
  public static final int QUARTER_CIRCLE        = 90;
  public static final int HALF_CIRCLE           = 180;
  public static final int THREE_QUARTER_CIRCLE  = 270;
  public static final int FULL_CIRCLE           = 360;

  private DJIFlightController flightController;
  private MovementTimer mMovementTimer;

  public void DroneMover() {
    initFlightController();
  }

  private void initFlightController() {
    flightController = ApplicationDrone.getAircraftInstance().getFlightController();
  }

  public void startMotors() {
    // Il est nécéssaire de démarrer les moteurs. Ceci permet de "tester" le mouvement.
    ApplicationDrone.getAircraftInstance().getFlightController().turnOnMotors(
      new DJICommonCallbacks.DJICompletionCallback () {
        @Override
        public void onResult(DJIError djiError) {
          if (djiError != null) {
            Log.e(TAG, String.format("Erreur de démarrage des moteurs : %s", djiError.getDescription()));
          }
        }
      }
    );
  }

  public void takeOff() {
    // TAKE OFF
    ApplicationDrone.getAircraftInstance().getFlightController().takeOff(
      new DJICommonCallbacks.DJICompletionCallback () {
        @Override
        public void onResult(DJIError djiError) {
          if (djiError != null) {
            Log.e(TAG, String.format("Erreur de décollage : %s", djiError.getDescription()));
          }
        }
      }
    );
  }

  public void land() {
    if (null != mMovementTimer) {
      mMovementTimer.cancel();
      mMovementTimer = null;
    }

    // LANDING
    ApplicationDrone.getAircraftInstance().getFlightController().autoLanding(
      new DJICommonCallbacks.DJICompletionCallback() {
        @Override
        public void onResult(DJIError djiError) {
          if (djiError != null) {
            Log.e(TAG, String.format("Erreur d'atterissage : %s",djiError.getDescription()));
          }
        }
      }
    );
  }

  public void move(MovementTimer movementTimer) {
    if (null != mMovementTimer) {
      Log.d(TAG, "mMovementTimer is not null : cancelling");
      mMovementTimer.cancel();
      mMovementTimer = null;
    }

    if (null == mMovementTimer) {
      mMovementTimer = movementTimer;
      mMovementTimer.start();
    }
  }

  public MovementTimer getMovementTimer(float[] pitchRollYawThrottle) {
    Log.d(TAG, String.format("Creating MovementTimer with pitch %s roll %s yaw %s throttle %s", pitchRollYawThrottle[0], pitchRollYawThrottle[1], pitchRollYawThrottle[2], pitchRollYawThrottle[3]));
    return new MovementTimer(1000, 100, pitchRollYawThrottle[0], pitchRollYawThrottle[1], pitchRollYawThrottle[2], pitchRollYawThrottle[3]);
  }

  public MovementTimer getMovementTimer(float pitch, float roll, float yaw, float throttle) {
    Log.d(TAG, String.format("Creating MovementTimer with pitch %s roll %s yaw %s throttle %s", pitch, roll, yaw, throttle));
    return new MovementTimer(1000, 100, pitch, roll, yaw, throttle);
  }

  public MovementTimer getMovementTimer(float pitch, float roll, float yaw, float throttle, MovementTimer nextMovementTimer) {
    Log.d(TAG, String.format("Creating MovementTimer with pitch %s roll %s yaw %s throttle %s", pitch, roll, yaw, throttle));
    return new MovementTimer(1000, 100, pitch, roll, yaw, throttle, nextMovementTimer);
  }

  public MovementTimer getMovementTimer(float pitch, float roll, float yaw, float throttle, int duration, int frequency) {
    Log.d(TAG, String.format("Creating MovementTimer with pitch %s roll %s yaw %s throttle %s", pitch, roll, yaw, throttle));
    return new MovementTimer(duration, frequency, pitch, roll, yaw, throttle);
  }

  public MovementTimer getMovementTimer(float pitch, float roll, float yaw, float throttle, int duration, int frequency, MovementTimer nextMovementTimer) {
    Log.d(TAG, String.format("Creating MovementTimer with pitch %s roll %s yaw %s throttle %s", pitch, roll, yaw, throttle));
    return new MovementTimer(duration, frequency, pitch, roll, yaw, throttle, nextMovementTimer);
  }

  public void enableVirtualStickMode() {
    // Source : https://developer.dji.com/mobile-sdk/documentation/introduction/flightController_concepts.html
    // Mode de base du drone

    // Ceci permet de se servir de coordonées x, y, z qui n'utilisent pas le nord magnétique
    ApplicationDrone.getAircraftInstance().getFlightController().setHorizontalCoordinateSystem(DJIVirtualStickFlightCoordinateSystem.Body);

    // Mise en place du mode de vélocité pour le roll et le pitch (m/s)
    ApplicationDrone.getAircraftInstance().getFlightController().setRollPitchControlMode(DJIVirtualStickRollPitchControlMode.Velocity);

    // Mise en place du mode de vélocité pour le Throttle (m/s)
    ApplicationDrone.getAircraftInstance().getFlightController().setVerticalControlMode(DJIVirtualStickVerticalControlMode.Velocity);

    // Mise en place du mode de vélocité angulaire pour le Yaw (°/s)
    ApplicationDrone.getAircraftInstance().getFlightController().setYawControlMode(DJIVirtualStickYawControlMode.AngularVelocity);

    // Activation du mode de contrôle par Virtual Stick
    ApplicationDrone.getAircraftInstance().getFlightController().enableVirtualStickControlMode(
      new DJICommonCallbacks.DJICompletionCallback() {
        @Override
        public void onResult(DJIError djiError) {
          if (djiError != null) {
            Log.e(TAG, String.format("Erreur d'activation du mode de contrôle par Virtual Stick : %s", djiError.getDescription()));
          }
        }
      }
    );
  }

  public void disableVirtualStickMode() {
    // Désactivation du mode de controle par Virtual Stick
    ApplicationDrone.getAircraftInstance().getFlightController().disableVirtualStickControlMode(
      new DJICommonCallbacks.DJICompletionCallback() {
        @Override
        public void onResult(DJIError djiError) {
          if (djiError != null) {
            Log.e(TAG, String.format("Erreur de désactivation du mode de contrôle par Virtual Stick : %s" + djiError.getDescription()));
          }
        }
      }
    );
  }
}
