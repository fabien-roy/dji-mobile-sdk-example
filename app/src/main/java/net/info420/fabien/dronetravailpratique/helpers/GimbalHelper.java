package net.info420.fabien.dronetravailpratique.helpers;

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

  /**
   * Appelle {@link #initGimbal()}
   */
  public GimbalHelper() {
    initGimbal();
  }

  /**
   * Arrange le Gimbal afin de pouvoir le bouger
   */
  private void initGimbal() {
    // FreeMode permet de jouer avec le pitch, le roll et le yaw
    setGimbalWorkMode(DJIGimbalWorkMode.FreeMode);
  }

  /**
   * Modifie le mode du {@link dji.sdk.gimbal.DJIGimbal}
   *
   * @param djiGimbalWorkMode Le {@link DJIGimbalWorkMode} du {@link dji.sdk.gimbal.DJIGimbal}
   *
   * @see dji.sdk.gimbal.DJIGimbal#setGimbalWorkMode(DJIGimbalWorkMode, DJICommonCallbacks.DJICompletionCallback)
   *
   * @see <a href="https://developer.dji.com/api-reference/android-api/Components/Gimbal/DJIGimbal.html#djigimbal_workmode_inline"
   *      target="_blank">
   *      Source : Les différents modes du {@link dji.sdk.gimbal.DJIGimbal}</a>
   */
  private void setGimbalWorkMode(DJIGimbalWorkMode djiGimbalWorkMode) {
    DroneApplication.getGimbalInstance().setGimbalWorkMode(djiGimbalWorkMode, new DJICommonCallbacks.DJICompletionCallback() {
      @Override
      public void onResult(DJIError djiError) {
        Log.d(TAG, djiError == null ? "Succès du changement de mode du gimbal" : djiError.getDescription());
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
   private void bougerGimbal(DJIGimbalAngleRotation pitch, DJIGimbalAngleRotation roll, DJIGimbalAngleRotation yaw) {
    // RelativeAngle permet d'ajouter l'angle à l'angle actuel du Gimbal
    // AbsoluteAngle permettrait d'ajuster l'angle avec le devant du drone
    DroneApplication.getGimbalInstance().rotateGimbalByAngle(DJIGimbalRotateAngleMode.RelativeAngle, pitch, roll, yaw, new DJICommonCallbacks.DJICompletionCallback() {
      @Override
      public void onResult(DJIError djiError) {
        Log.d(TAG, djiError == null ? "Succès de la rotation du gimbal" : djiError.getDescription());
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
    bougerGimbal( new DJIGimbalAngleRotation(true, pitch, DJIGimbalRotateDirection.Clockwise),
                  new DJIGimbalAngleRotation(true, roll,  DJIGimbalRotateDirection.Clockwise),
                  new DJIGimbalAngleRotation(true, yaw,   DJIGimbalRotateDirection.Clockwise));
  }

  /**
   * Vise le {@link dji.sdk.camera.DJICamera} vers le sol
   *
   * <ul>
   *   <li>Ajuste le {@link DJIGimbalWorkMode} pour qu'il suive le devant du drone</li>
   *   <li>Vise le {@link dji.sdk.gimbal.DJIGimbal} vers le sol</li>
   * </ul>
   *
   * @see #setGimbalWorkMode(DJIGimbalWorkMode)
   * @see #bougerGimbal(int, int, int)
   *
   * @see <a href="https://developer.dji.com/api-reference/android-api/Components/Gimbal/DJIGimbal.html#djigimbal_workmode_inline"
   *      target="_blank">
   *      Source : Les différents modes du {@link dji.sdk.gimbal.DJIGimbal}</a>
   */
  public void setGroundGimbal() {
    setGimbalWorkMode(DJIGimbalWorkMode.FreeMode);

    bougerGimbal(0, 1000, 0);

    setGimbalWorkMode(DJIGimbalWorkMode.FpvMode);
  }
}
