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
import net.info420.fabien.dronetravailpratique.helpers.CameraHelper;

import dji.common.camera.CameraSystemState;
import dji.common.camera.DJICameraSettingsDef;
import dji.common.product.Model;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.camera.DJICamera;
import dji.sdk.codec.DJICodecManager;

import static net.info420.fabien.dronetravailpratique.R.id.tv_obj2_etape2_timer;
import static net.info420.fabien.dronetravailpratique.application.DroneApplication.getCameraInstance;

/**
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-04-28
 *
 * @see DJICamera
 * @see dji.sdk.camera.DJICamera.CameraReceivedVideoDataCallback
 *
 * @see <a href="https://github.com/dji-sdk/Mobile-SDK-Android/blob/master/Sample%20Code/app/src/main/java/com/dji/sdk/sample/demo/camera/FetchMediaView.java"
 *      target="_blank">
 *      Source : Aller chercher le media de la caméra (vidéo/photo)</a>
 * @see <a href="https://github.com/dji-sdk/Mobile-SDK-Android/blob/master/Sample%20Code/app/src/main/java/com/dji/sdk/sample/demo/camera/ShootSinglePhotoView.java"
 *      target="_blank">
 *      Source : Mode SHOOT_PHOTO</a>
 * @see <a href="https://developer.dji.com/mobile-sdk/documentation/android-tutorials/FPVDemo.html"
 *      target="_blank">
 *      Source : Tutoriel pour aller chercher les images de la caméra</a>
 * @see <a href="https://developer.dji.com/mobile-sdk/documentation/introduction/component-guide-camera.html"
 *      target="_blank">
 *      Source : Informations sur la caméra</a>
 */
public class Obj2Etape2Activity extends AppCompatActivity implements TextureView.SurfaceTextureListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener{
  public static final String TAG = Obj1Etape2Activity.class.getName();

  TextView tvTimer;
  TextureView tvVideo;

  protected DJICamera.CameraReceivedVideoDataCallback mReceivedVideoDataCallBack = null;
  protected DJICodecManager mCodecManager;

  /**
   * Exécuté à la création de l'{@link android.app.Activity}
   *
   * <ul>
   *   <li>Appelle {@link #initUI()}</li>
   *   <li>Instancie le callback d</li>
   * </ul>
   *
   * @param savedInstanceState {@link Bundle}
   *
   * @see #initUI()
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initUI();
    initCallback();
    initTvTimer();
  }

  /**
   * Inialise l'interface
   *
   * <ul>
   *   <li>Ajoute le bon {@link android.text.Layout}</li>
   *   <li>Instancie les composants</li>
   *   <li>Met le Listener de texture au tvVideo</li>
   *   <li>Met les Listeners</li>
   *   <li>Cache tvTimer</li>
   * </ul>
   *
   * <p>Les seuls composants instanciés sont tvTimer et tvVideo, puisque ce sont les seuls dont
   * on a besoin dans les autres méthodes de l'{@link android.app.Activity}</p>
   *
   * @see android.view.TextureView.SurfaceTextureListener
   * @see #onSurfaceTextureUpdated(SurfaceTexture)
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

    // Listener spécial pour un ToggleButton
    ((ToggleButton) findViewById(R.id.btn_obj2_etape2_enregistrer)).setOnCheckedChangeListener(this);

    tvTimer.setVisibility(View.INVISIBLE);
  }

  /**
   * Instancie le {@link dji.sdk.camera.DJICamera.CameraReceivedVideoDataCallback}
   *
   * <ul>
   *   <li>Instancie mReceivedVideoDataCallBack</li>
   *   <li>Lorsque du data est reçu, vérifie si le {@link DJICodecManager} n'est pas null</li>
   *   <li>S'il ne l'est pas, décode la vidéo</li>
   * </ul>
   *
   * @see dji.sdk.camera.DJICamera.CameraReceivedVideoDataCallback
   * @see dji.sdk.camera.DJICamera.CameraReceivedVideoDataCallback#onResult(byte[], int)
   * @see DJICodecManager
   * @see DJICodecManager#sendDataToDecoder(byte[], int)
   */
  private void initCallback() {
    mReceivedVideoDataCallBack = new DJICamera.CameraReceivedVideoDataCallback() {
      @Override
      public void onResult(byte[] videoBuffer, int size) {
        if(mCodecManager != null){
          mCodecManager.sendDataToDecoder(videoBuffer, size);
        }
      }
    };
  }

  /**
   * Affiche correctement le {@link android.widget.TextView} du timer
   *
   * <ul>
   *   <li>Vérifie l'instance de la {@link DJICamera}</li>
   *   <li>Ajoute un {@link dji.sdk.camera.DJICamera.CameraUpdatedSystemStateCallback} à la
   *   {@link DJICamera}</li>
   *   <li>Va chercher les données du temps d'enregistrement</li>
   *   <li>Démarre un {@link Thread} pour le temps de la vidéo</li>
   * </ul>
   *
   * @see DJICamera#setDJICameraUpdatedSystemStateCallback(DJICamera.CameraUpdatedSystemStateCallback)
   * @see dji.sdk.camera.DJICamera.CameraUpdatedSystemStateCallback
   * @see dji.sdk.camera.DJICamera.CameraUpdatedSystemStateCallback#onResult(CameraSystemState)
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
    if (getCameraInstance() != null) {
      // Reset du callback
      getCameraInstance().setDJICameraReceivedVideoDataCallback(cameraReceivedVideoDataCallback);
    }
  }

  /**
   * Initialise la vidéo
   *
   * <ul>
   *   <li>Vérifie que le {@link DJIBaseProduct} est connecté</li>
   *   <li>Place le bon Listener sur la {@link TextureView}</li>
   *   <li>Place le bon {@link DJICamera.CameraReceivedVideoDataCallback} sur l'instance de la
   *   {@link DJICamera}</li>
   * </ul>
   *
   * @see DJIBaseProduct
   * @see DJICamera
   * @see dji.sdk.camera.DJICamera.CameraReceivedVideoDataCallback
   */
  private void initVideo() {
    DJIBaseProduct product = DroneApplication.getProductInstance();

    if (product == null || !product.isConnected()) {
      Log.e(TAG, "Le drone est déconnecté!");
    } else {
      if (null != tvVideo) tvVideo.setSurfaceTextureListener(this);

      if (!product.getModel().equals(Model.UnknownAircraft)) {
        setCameraCallback(mReceivedVideoDataCallBack);
      }
    }
  }

  /**
   * Lorsque le {@link DJIBaseProduct} change, on appelle {@link #initVideo()}
   *
   * @see #initVideo()
   */
  protected void onProductChange() {
    initVideo();
  }

  /**
   * Lorsqu'on revient sur l'{@link android.app.Activity}, appelle {@link #onProductChange()}
   *
   * @see #onProductChange()
   */
  @Override
  public void onResume() {
    super.onResume();

    onProductChange();
  }

  /**
   * S'assure que le {@link DJICodecManager} est initilisalisé
   *
   * @param surface La {@link SurfaceTexture}, en occurance, tvVideo
   * @param width   La largeur du {@link SurfaceTexture}
   * @param height  La hauteur du {@link SurfaceTexture}
   *
   * @see SurfaceTexture
   * @see android.view.TextureView.SurfaceTextureListener#onSurfaceTextureAvailable(SurfaceTexture, int, int)
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
   * @see android.view.TextureView.SurfaceTextureListener#onSurfaceTextureSizeChanged(SurfaceTexture, int, int)
   */
  @Override
  public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) { }

  /**
   * S'assure que le {@link DJICodecManager} n'est plus initilisalisé
   *
   * @param surface La {@link SurfaceTexture}, en occurance, tvVideo
   *
   * @see SurfaceTexture
   * @see android.view.TextureView.SurfaceTextureListener#onSurfaceTextureDestroyed(SurfaceTexture)
   * @see DJICodecManager#cleanSurface()
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
   * @see android.view.TextureView.SurfaceTextureListener#onSurfaceTextureUpdated(SurfaceTexture)
   */
  @Override
  public void onSurfaceTextureUpdated(SurfaceTexture surface) { }

  /**
   * Exécuté lorsqu'un {@link View} est cliqué dans l'{@link android.app.Activity}
   *
   * @param view  Le {@link View} qui a été cliqué
   *
   * @see net.info420.fabien.dronetravailpratique.helpers.GimbalHelper#bougerGimbal(int, int, int)
   * @see CameraHelper#capturer()
   * @see CameraHelper#demarrerEnregistrement()
   * @see CameraHelper#switchCameraMode(DJICameraSettingsDef.CameraMode)
   */
  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_obj2_etape2_pitch_moins:
        DroneApplication.getGimbalHelper().bougerGimbal(-15, 0, 0);
        break;
      case R.id.btn_obj2_etape2_pitch_plus:
        DroneApplication.getGimbalHelper().bougerGimbal(15, 0, 0);
        break;
      case R.id.btn_obj2_etape2_yaw_moins:
        DroneApplication.getGimbalHelper().bougerGimbal(0, 0, -15);
        break;
      case R.id.btn_obj2_etape2_yaw_plus:
        DroneApplication.getGimbalHelper().bougerGimbal(0, 0, 15);
        break;
      case R.id.btn_obj2_etape2_capturer:
        DroneApplication.getCameraHelper().capturer();
        break;
      case R.id.btn_obj2_etape2_enregistrer:
        DroneApplication.getCameraHelper().demarrerEnregistrement();
        break;
      case R.id.btn_obj2_etape2_mode_photo:
        DroneApplication.getCameraHelper().switchCameraMode(DJICameraSettingsDef.CameraMode.ShootPhoto);
        break;
      case R.id.btn_obj2_etape2_mode_video:
        DroneApplication.getCameraHelper().switchCameraMode(DJICameraSettingsDef.CameraMode.RecordVideo);
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
   * @see CameraHelper#demarrerEnregistrement()
   * @see CameraHelper#arreterEnregistrement()
   */
  @Override
  public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
    if (isChecked) {
      tvTimer.setVisibility(View.VISIBLE);
      DroneApplication.getCameraHelper().demarrerEnregistrement();
    } else {
      tvTimer.setVisibility(View.INVISIBLE);
      DroneApplication.getCameraHelper().arreterEnregistrement();
    }
  }
}
