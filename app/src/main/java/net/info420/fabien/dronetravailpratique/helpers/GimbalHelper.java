package net.info420.fabien.dronetravailpratique.helpers;

// TODO : Documenter GimbalHelper

import android.util.Log;

import net.info420.fabien.dronetravailpratique.application.DroneApplication;

import dji.common.error.DJIError;
import dji.common.gimbal.DJIGimbalAngleRotation;
import dji.common.gimbal.DJIGimbalRotateAngleMode;
import dji.common.gimbal.DJIGimbalRotateDirection;
import dji.common.gimbal.DJIGimbalWorkMode;
import dji.common.util.DJICommonCallbacks;

/**
 * {@link GimbalHelper}
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-05-05
 */
public class GimbalHelper {
  private static final String TAG = GimbalHelper.class.getName();

  public GimbalHelper() {
    initGimbal();
  }

  /**
   * Arrange le Gimbal afin de pouvoir le bouger
   */
  private void initGimbal() {
    // FreeMode permet de jouer avec le pitch, le roll et le yaw
    DroneApplication.getGimbalInstance().setGimbalWorkMode(DJIGimbalWorkMode.FreeMode, new DJICommonCallbacks.DJICompletionCallback() {
      @Override
      public void onResult(DJIError djiError) {
        if (djiError == null) {
          Log.d(TAG, "Set Gimbal Work Mode success");
        } else {
          Log.d(TAG, "Set Gimbal Work Mode failed" + djiError);
        }
      }
    });
  }

  /**
   * Reçoit trois paramètres {@link DJIGimbalAngleRotation} et bouge le {@link dji.sdk.gimbal.DJIGimbal}
   *
   * @param pitch {@link DJIGimbalAngleRotation} de pitch
   * @param roll  {@link DJIGimbalAngleRotation} de roll
   * @param yaw   {@link DJIGimbalAngleRotation} de yaw
   *
   * @see DJIGimbalAngleRotation
   * @see dji.sdk.gimbal.DJIGimbal
   */
   public void bougerGimbal(DJIGimbalAngleRotation pitch, DJIGimbalAngleRotation roll, DJIGimbalAngleRotation yaw) {
    // RelativeAngle permet d'ajouter l'angle à l'angle actuel du Gimbal
    // AbsoluteAngle permettrait d'ajuster l'angle avec le devant du drone
    DroneApplication.getGimbalInstance().rotateGimbalByAngle(DJIGimbalRotateAngleMode.RelativeAngle, pitch, roll, yaw, new DJICommonCallbacks.DJICompletionCallback() {
      @Override
      public void onResult(DJIError djiError) {
        if (djiError == null) {
          Log.d(TAG, "Rotate Gimbal Success");
        } else {
          Log.d(TAG, "Rotate Gimbal Fail" + djiError);
        }
      }
    });
  }

  /**
   * Appeler bougerGimbal en construisant des {@link DJIGimbalAngleRotation} fait des int reçus
   *
   * @param pitch int permettant de construire le {@link DJIGimbalAngleRotation} de pitch
   * @param roll  int permettant de construire le {@link DJIGimbalAngleRotation} de roll
   * @param yaw   int permettant de construire le {@link DJIGimbalAngleRotation} de yaw
   *
   * @see DJIGimbalAngleRotation
   * @see dji.sdk.gimbal.DJIGimbal
   */
  public void bougerGimbal(int pitch, int roll, int yaw) {
    DJIGimbalAngleRotation angleRotationPitch = new DJIGimbalAngleRotation(true, pitch, DJIGimbalRotateDirection.Clockwise);
    DJIGimbalAngleRotation angleRotationRoll  = new DJIGimbalAngleRotation(true, roll,  DJIGimbalRotateDirection.Clockwise);
    DJIGimbalAngleRotation angleRotationYaw   = new DJIGimbalAngleRotation(true, yaw,   DJIGimbalRotateDirection.Clockwise);

    bougerGimbal(angleRotationPitch, angleRotationRoll, angleRotationYaw);
  }
}
