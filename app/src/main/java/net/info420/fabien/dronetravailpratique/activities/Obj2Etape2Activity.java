package net.info420.fabien.dronetravailpratique.activities;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.application.DroneApplication;

import dji.common.camera.CameraSystemState;
import dji.common.camera.DJICameraSettingsDef;
import dji.common.error.DJIError;
import dji.common.product.Model;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.camera.DJICamera;
import dji.sdk.codec.DJICodecManager;

import static net.info420.fabien.dronetravailpratique.R.id.tv_obj2_etape2_timer;
import static net.info420.fabien.dronetravailpratique.application.DroneApplication.getCameraInstance;

// TODO : Documenter Obj2Etape2Activity

/**
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-04-28
 *
 * @see DJICamera
 * @see dji.sdk.camera.DJICamera.CameraReceivedVideoDataCallback
 *
 * {@link <a href="https://github.com/dji-sdk/Mobile-SDK-Android/blob/master/Sample%20Code/app/src/main/java/com/dji/sdk/sample/demo/camera/FetchMediaView.java">
 *   Aller chercher le media de la caméra (vidéo/photo)</a>}
 * {@link <a href="https://github.com/dji-sdk/Mobile-SDK-Android/blob/master/Sample%20Code/app/src/main/java/com/dji/sdk/sample/demo/camera/ShootSinglePhotoView.java">
 *   Mode SHOOT_PHOTO</a>}
 * {@link <a href="https://developer.dji.com/mobile-sdk/documentation/android-tutorials/FPVDemo.html">
 *   Tutoriel pour aller chercher les images de la caméra</a>}
 * {@link <a href="https://developer.dji.com/mobile-sdk/documentation/introduction/component-guide-camera.html">
 *   Informations sur la caméra</a>}
 */
public class Obj2Etape2Activity extends AppCompatActivity implements TextureView.SurfaceTextureListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener{
  public static final String TAG = Obj1Etape2Activity.class.getName();

  // protected VideoFeeder.VideoDataCallback mReceivedVideoDataCallBack = null;

  TextView tvTimer;
  TextureView tvVideo;

  protected DJICamera.CameraReceivedVideoDataCallback mReceivedVideoDataCallBack = null;
  protected DJICodecManager mCodecManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initUI();
    initCallback();
    initTvTimer();
  }

  /**
   * Par mesure de sécurité, on fait attérir le drone à la fermetture de l'{@link android.app.Activity}
   */
  @Override
  protected void onDestroy(){
    super.onDestroy();

    DroneApplication.getDroneBougeur().atterir();
  }

  /**
   * Initialise les composants
   * Mets en place les Listeners
   */
  private void initUI(){
    setContentView(R.layout.activity_obj2_etape2);

    tvTimer = (TextView)    findViewById(tv_obj2_etape2_timer);
    tvVideo = (TextureView) findViewById(R.id.tv_obj2_etape2_video);

    tvVideo.setSurfaceTextureListener(this);

    findViewById(R.id.btn_obj2_etape2_pitch_moins).setOnClickListener(this);
    findViewById(R.id.btn_obj2_etape2_pitch_plus).setOnClickListener(this);
    findViewById(R.id.btn_obj2_etape2_yaw_moins).setOnClickListener(this);
    findViewById(R.id.btn_obj2_etape2_yaw_plus).setOnClickListener(this);

    findViewById(R.id.btn_obj2_etape2_capturer).setOnClickListener(this);
    findViewById(R.id.btn_obj2_etape2_enregistrer).setOnClickListener(this);
    findViewById(R.id.btn_obj2_etape2_mode_photo).setOnClickListener(this);
    findViewById(R.id.btn_obj2_etape2_mode_video).setOnClickListener(this);

    ((ToggleButton) findViewById(R.id.btn_obj2_etape2_enregistrer)).setOnCheckedChangeListener(this);

    tvTimer.setVisibility(View.INVISIBLE);
  }

  /**
   * Initialise la vidéo
   *
   * Vérifie que le {@link DJIBaseProduct} est connecté
   * Place le bon Listener sur la {@link TextureView}
   * Place le bon {@link DJICamera.CameraReceivedVideoDataCallback} sur l'instance de la {@link DJICamera}
   *
   * @see DJIBaseProduct
   * @see DJICamera
   * @see dji.sdk.camera.DJICamera.CameraReceivedVideoDataCallback
   */
  private void initVideo() {
    DJIBaseProduct product = DroneApplication.getProductInstance();

    if (product == null || !product.isConnected()) {
      Log.e(TAG, "Le produit est déconnecté!");
    } else {
      if (null != tvVideo) tvVideo.setSurfaceTextureListener(this);

      if (!product.getModel().equals(Model.UnknownAircraft)) {
        setCameraCallback(mReceivedVideoDataCallBack);
      }
    }
  }

  /**
   * Enlève le {@link dji.sdk.camera.DJICamera.CameraReceivedVideoDataCallback} de la {@link DJICamera}
   */
  private void uninitVideo() {
    setCameraCallback(null);
  }

  /**
   * Instancie le {@link dji.sdk.camera.DJICamera.CameraReceivedVideoDataCallback}
   */
  private void initCallback() {
    mReceivedVideoDataCallBack = new DJICamera.CameraReceivedVideoDataCallback() {
      @Override
      public void onResult(byte[] videoBuffer, int size) {
        if(mCodecManager != null){
          // Décoder la vidéo reçue
          mCodecManager.sendDataToDecoder(videoBuffer, size);
        }else {
          Log.e(TAG, "mCodecManager = null");
        }
      }
    };
  }

  /**
   * Affiche correctement le {@link android.widget.TextView} du timer
   *
   * Vérifie l'instance de la {@link DJICamera}
   * Ajoute un {@link dji.sdk.camera.DJICamera.CameraUpdatedSystemStateCallback} à la {@link DJICamera}
   * Va chercher les données du temps d'enregistrement
   * Démarre un {@link Thread} pour le temps de la vidéo
   *
   * @see dji.sdk.camera.DJICamera.CameraUpdatedSystemStateCallback
   */
  private void initTvTimer() {
    if (DroneApplication.getCameraInstance() != null) {
      DroneApplication.getCameraInstance().setDJICameraUpdatedSystemStateCallback(new DJICamera.CameraUpdatedSystemStateCallback() {
        @Override
        public void onResult(CameraSystemState cameraSystemState) {
          if (null != cameraSystemState) {
            int tempsEnregistrement = cameraSystemState.getCurrentVideoRecordingTimeInSeconds();

            final String tempsString        = String.format("%02d:%02d",
                                                            (tempsEnregistrement % 3600) / 60,
                                                            tempsEnregistrement % 60);
            final boolean isVideoRecording  = cameraSystemState.isRecording();

            // Thread pour le timer
            Obj2Etape2Activity.this.runOnUiThread(new Runnable() {
              @Override
              public void run() {
                tvTimer.setText(tempsString);
                tvTimer.setVisibility((isVideoRecording) ? View.VISIBLE : View.INVISIBLE);
              }
            });
          }
        }
      });
    }
  }

  /**
   * Ajoute un {@link dji.sdk.camera.DJICamera.CameraReceivedVideoDataCallback} à la {@link DJICamera}
   */
  private void setCameraCallback(DJICamera.CameraReceivedVideoDataCallback cameraReceivedVideoDataCallback) {
    if (getCameraInstance()!= null) {
      // Reset du callback
      getCameraInstance().setDJICameraReceivedVideoDataCallback(cameraReceivedVideoDataCallback);
    }
  }

  // TODO : Affiche la photo

  /**
   * Capture une photo
   *
   * Vérifie l'instance de la {@link DJICamera}
   * Démarre une photo shoot avec mode une seule photo (Single)
   * Affiche un log en fonction du succès de l'opération
   *
   * @see DJICamera
   */
  private void capturer() {
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
  private void demarrerEnregistrement(){
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
  private void arreterEnregistrement(){
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
  private void switchCameraMode(DJICameraSettingsDef.CameraMode cameraMode){
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

  protected void onProductChange() {
    initVideo();
  }

  @Override
  public void onResume() {
    super.onResume();

    onProductChange();

    if(tvVideo == null) Log.e(TAG, "tvVideo est null");
  }

  /**
   * S'assure que le {@link DJICodecManager} est initilisalisé
   *
   * @param surface La {@link SurfaceTexture}, en occurance, tvVideo
   * @param width   La largeur du {@link SurfaceTexture}
   * @param height  La hauteur du {@link SurfaceTexture}
   *
   * @see SurfaceTexture
   */
  @Override
  public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
    if (mCodecManager == null) {
      mCodecManager = new DJICodecManager(this, surface, width, height);
    }
  }

  /**
   * Exécuté lorsque la grandeur du {@link SurfaceTexture} change
   *
   * @param surface La {@link SurfaceTexture}, en occurance, tvVideo
   * @param width   La largeur du {@link SurfaceTexture}
   * @param height  La hauteur du {@link SurfaceTexture}
   *
   * @see SurfaceTexture
   */
  @Override
  public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) { }

  /**
   * S'assure que le {@link DJICodecManager} n'est plus initilisalisé
   *
   * @param surface La {@link SurfaceTexture}, en occurance, tvVideo
   *
   * @see SurfaceTexture
   */
  @Override
  public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
    if (mCodecManager != null) {
      mCodecManager.cleanSurface();
      mCodecManager = null;
    }
    return false;
  }

  /**
   * Exécuté lorsque la {@link SurfaceTexture} change
   *
   * @param surface La {@link SurfaceTexture}, en occurance, tvVideo
   *
   * @see SurfaceTexture
   */
  @Override
  public void onSurfaceTextureUpdated(SurfaceTexture surface) { }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_obj2_etape2_pitch_moins:
        break;
      case R.id.btn_obj2_etape2_pitch_plus:
        break;
      case R.id.btn_obj2_etape2_yaw_moins:
        break;
      case R.id.btn_obj2_etape2_yaw_plus:
        break;
      case R.id.btn_obj2_etape2_capturer:
        capturer();
        break;
      case R.id.btn_obj2_etape2_enregistrer:
        break;
      case R.id.btn_obj2_etape2_mode_photo:
        switchCameraMode(DJICameraSettingsDef.CameraMode.ShootPhoto);
        break;
      case R.id.btn_obj2_etape2_mode_video:
        switchCameraMode(DJICameraSettingsDef.CameraMode.RecordVideo);
        break;
    }
  }

  /**
   * Démarre ou arrête la vidéo du {@link DJICamera} en fonction du toggle du {@link ToggleButton}
   *
   * @param compoundButton  {@link ToggleButton}
   * @param isChecked       boolean signifiant le toggle du {@link ToggleButton}
   *
   * @see ToggleButton
   */
  @Override
  public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
    if (isChecked) {
      tvTimer.setVisibility(View.VISIBLE);
      demarrerEnregistrement();
    } else {
      tvTimer.setVisibility(View.INVISIBLE);
      arreterEnregistrement();
    }
  }
}
