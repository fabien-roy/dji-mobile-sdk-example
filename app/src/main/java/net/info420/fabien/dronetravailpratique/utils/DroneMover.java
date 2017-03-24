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

  private DJIFlightController flightController;
  private MovementTimer movementTimer;

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
            Log.e(TAG, "Erreur de démarrage des moteurs : " + djiError.getDescription());
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
            Log.e(TAG, "Erreur de décollage : " + djiError.getDescription());
          }
        }
      }
    );
  }

  public void land() {
    if (null != movementTimer) {
      movementTimer.cancel();
      movementTimer = null;
    }

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

  public void move(float[] pitchRollYawThrottle) {
    if (null != movementTimer) {
      movementTimer.cancel();
      movementTimer = null;
    }

    if (null == movementTimer) {
      movementTimer = new MovementTimer(100, 200, pitchRollYawThrottle[0], pitchRollYawThrottle[1], pitchRollYawThrottle[2], pitchRollYawThrottle[3]);
      movementTimer.start();
    }
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
            Log.e(TAG, "Erreur d'activation du mode de contrôle par Virtual Stick : " + djiError.getDescription());
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
            Log.e(TAG, "Erreur de désactivation du mode de contrôle par Virtual Stick : " + djiError.getDescription());
          }
        }
      }
    );
  }
}
