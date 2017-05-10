package net.info420.fabien.dronetravailpratique.helpers;

import android.util.Log;

import net.info420.fabien.dronetravailpratique.application.DroneApplication;

import dji.common.camera.DJICameraSettingsDef;
import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.camera.DJICamera;

/**
 * {@link CameraHelper}
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-05-09
 */
public class CameraHelper {
  private static final String TAG = CameraHelper.class.getName();

  public CameraHelper() { }

  /**
   * Capture une photo
   *
   * <ul>
   *   <li>Vérifie l'instance de la {@link DJICamera}</li>
   *   <li>Démarre une photo shoot avec mode une seule photo (Single)</li>
   *   <li>Affiche un log en fonction du succès de l'opération</li>
   * </ul>
   *
   * @see DJICamera
   * @see DJICamera#startShootPhoto(DJICameraSettingsDef.CameraShootPhotoMode, DJICommonCallbacks.DJICompletionCallback)
   */
  public void capturer() {
    if (DroneApplication.getCameraInstance() != null) {
      DroneApplication.getCameraInstance().startShootPhoto(DJICameraSettingsDef.CameraShootPhotoMode.Single, new DJICommonCallbacks.DJICompletionCallback() {
        @Override
        public void onResult(DJIError djiError) {
          Log.d(TAG, djiError == null ? "Succès de la capture de photo" : djiError.getDescription());
        }
      });
    }
  }

  /**
   * Démarre l'enregistrement vidéo du {@link DJICamera}
   *
   * <ul>
   *   <li>Vérifie l'instance de la {@link DJICamera}</li>
   *   <li>Démarre l'enregistrement vidéo du {@link DJICamera}</li>
   *   <li>Affiche un log en fonction du succès de l'opération</li>
   * </ul>
   *
   * @see DJICamera
   * @see DJICamera#startRecordVideo(DJICommonCallbacks.DJICompletionCallback)
   */
  public void demarrerEnregistrement(){
    if (DroneApplication.getCameraInstance() != null) {
      DroneApplication.getCameraInstance().startRecordVideo(new DJICommonCallbacks.DJICompletionCallback(){
        @Override
        public void onResult(DJIError djiError) {
          Log.d(TAG, djiError == null ? "Succès du démarrage de l'enregistrement" : djiError.getDescription());
        }
      });
    }
  }

  /**
   * Arrête l'enregistrement vidéo du {@link DJICamera}
   *
   * <ul>
   *   <li>Vérifie l'instance de la {@link DJICamera}</li>
   *   <li>Arrête l'enregistrement vidéo du {@link DJICamera}</li>
   *   <li>Affiche un log en fonction du succès de l'opération</li>
   * </ul>
   *
   * @see DJICamera
   * @see DJICamera#stopRecordVideo(DJICommonCallbacks.DJICompletionCallback)
   */
  public void arreterEnregistrement(){
    if (DroneApplication.getCameraInstance() != null) {
      DroneApplication.getCameraInstance().stopRecordVideo(new DJICommonCallbacks.DJICompletionCallback(){
        @Override
        public void onResult(DJIError djiError) {
          Log.d(TAG, djiError == null ? "Succès de l'arrêt de l'enregistrement" : djiError.getDescription());
        }
      });
    }
  }

  /**
   * Change le mode de la {@link DJICamera}
   *
   * <ul>
   *   <li>Vérifie l'instance de la {@link DJICamera}</li>
   *   <li>Change le mode de la {@link DJICamera}</li>
   *   <li>Affiche un log en fonction du succès de l'opération</li>
   * </ul>
   *
   * @param cameraMode  {@link DJICameraSettingsDef} à mettre sur la {@link DJICamera}
   *
   * @see DJICamera
   * @see DJICamera#setCameraMode(DJICameraSettingsDef.CameraMode, DJICommonCallbacks.DJICompletionCallback)
   * @see DJICameraSettingsDef
   */
  public void switchCameraMode(DJICameraSettingsDef.CameraMode cameraMode){
    if (DroneApplication.getCameraInstance()!= null) {
      DroneApplication.getCameraInstance().setCameraMode(cameraMode, new DJICommonCallbacks.DJICompletionCallback() {
        @Override
        public void onResult(DJIError djiError) {
          Log.d(TAG, djiError == null ? "Succès du changement de mode de caméra" : djiError.getDescription());
        }
      });
    }
  }
}
