package net.info420.fabien.dronetravailpratique.helpers;

// TODO : Documenter CameraHelper

import android.util.Log;

import net.info420.fabien.dronetravailpratique.application.DroneApplication;

import dji.sdk.camera.DJICamera;
import dji.common.camera.DJICameraSettingsDef;
import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;

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
   * Vérifie l'instance de la {@link DJICamera}
   * Démarre une photo shoot avec mode une seule photo (Single)
   * Affiche un log en fonction du succès de l'opération
   *
   * @see DJICamera
   */
  public void capturer() {
    if (DroneApplication.getCameraInstance() != null) {
      DroneApplication.getCameraInstance().startShootPhoto(DJICameraSettingsDef.CameraShootPhotoMode.Single, new DJICommonCallbacks.DJICompletionCallback() {
        @Override
        public void onResult(DJIError djiError) {
          if (djiError == null) {
            Log.d(TAG, "Succès de la capture de photo");
          } else {
            Log.d(TAG, djiError.getDescription());
          }
        }
      });
    }
  }

  /**
   * Démarre l'enregistrement vidéo du {@link DJICamera}
   *
   * Vérifie l'instance de la {@link DJICamera}
   * Démarre l'enregistrement vidéo du {@link DJICamera}
   * Affiche un log en fonction du succès de l'opération
   */
  public void demarrerEnregistrement(){
    if (DroneApplication.getCameraInstance() != null) {
      DroneApplication.getCameraInstance().startRecordVideo(new DJICommonCallbacks.DJICompletionCallback(){
        @Override
        public void onResult(DJIError djiError) {
          if (djiError == null) {
            Log.d(TAG, "Succès du démarrage de l'enregistrement");
          } else {
            Log.d(TAG, djiError.getDescription());
          }
        }
      });
    }
  }

  /**
   * Arrête l'enregistrement vidéo du {@link DJICamera}
   *
   * Vérifie l'instance de la {@link DJICamera}
   * Arrête l'enregistrement vidéo du {@link DJICamera}
   * Affiche un log en fonction du succès de l'opération
   */
  public void arreterEnregistrement(){
    if (DroneApplication.getCameraInstance() != null) {
      DroneApplication.getCameraInstance().stopRecordVideo(new DJICommonCallbacks.DJICompletionCallback(){
        @Override
        public void onResult(DJIError djiError) {
          if (djiError == null) {
            Log.d(TAG, "Succès de l'arrêt de l'enregistrement");
          } else {
            Log.d(TAG, djiError.getDescription());
          }
        }
      });
    }
  }

  /**
   * Change le mode de la {@link DJICamera}
   *
   * Vérifie l'instance de la {@link DJICamera}
   * Change le mode de la {@link DJICamera}
   * Affiche un log en fonction du succès de l'opération
   *
   * @param cameraMode  {@link DJICameraSettingsDef} à mettre sur la {@link DJICamera}
   *
   * @see DJICamera
   * @see DJICameraSettingsDef
   */
  public void switchCameraMode(DJICameraSettingsDef.CameraMode cameraMode){
    if (DroneApplication.getCameraInstance()!= null) {
      DroneApplication.getCameraInstance().setCameraMode(cameraMode, new DJICommonCallbacks.DJICompletionCallback() {
        @Override
        public void onResult(DJIError djiError) {
          if (djiError == null) {
            Log.d(TAG, "Succès du changement de mode de caméra");
          } else {
            Log.d(TAG, djiError.getDescription());
          }
        }
      });
    }
  }
}
