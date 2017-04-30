package net.info420.fabien.dronetravailpratique.activities;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.application.DroneApplication;

/**
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-04-28
 *
 * {@link <a href="https://github.com/dji-sdk/Mobile-SDK-Android/blob/master/Sample%20Code/app/src/main/java/com/dji/sdk/sample/demo/camera/FetchMediaView.java">
 *   Aller chercher le media de la caméra (vidéo/photo)</a>}
 * {@link <a href="https://github.com/dji-sdk/Mobile-SDK-Android/blob/master/Sample%20Code/app/src/main/java/com/dji/sdk/sample/demo/camera/ShootSinglePhotoView.java">
 *   Mode SHOOT_PHOTO</a>}
 * {@link <a href="https://developer.dji.com/mobile-sdk/documentation/android-tutorials/index.html">
 *   Tutoriel pour aller chercher les images de la caméra</a>}
 * {@link <a href="https://developer.dji.com/mobile-sdk/documentation/introduction/component-guide-camera.html">
 *   Informations sur la caméra</a>}
 */
public class Obj2Etape2Activity extends AppCompatActivity implements TextureView.SurfaceTextureListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener{
  public static final String TAG = Obj1Etape2Activity.class.getName();

  // TextView tvTimer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initUI();
  }

  /**
   * Par mesure de sécurité, on fait attérir le drone à la fermetture de l'{@link android.app.Activity}
   */
  @Override
  protected void onDestroy(){
    super.onDestroy();

    DroneApplication.getDroneMover().land();
  }

  /**
   * Initialise les composants
   * Mets en place les Listeners
   */
  private void initUI(){
    setContentView(R.layout.activity_obj2_etape2);

    ((TextureView) findViewById(R.id.tv_video)).setSurfaceTextureListener(this);

    findViewById(R.id.btn_pitch_moins).setOnClickListener(this);
    findViewById(R.id.btn_pitch_plus).setOnClickListener(this);
    findViewById(R.id.btn_yaw_moins).setOnClickListener(this);
    findViewById(R.id.btn_yaw_plus).setOnClickListener(this);

    findViewById(R.id.btn_capturer).setOnClickListener(this);
    findViewById(R.id.btn_enregistrer).setOnClickListener(this);
    findViewById(R.id.btn_mode_photo).setOnClickListener(this);
    findViewById(R.id.btn_mode_video).setOnClickListener(this);

    ((ToggleButton) findViewById(R.id.btn_enregistrer)).setOnCheckedChangeListener(this);

    // tvTimer = (TextView) findViewById(R.id.tv_timer);

    findViewById(R.id.tv_timer).setVisibility(View.INVISIBLE);
  }

  @Override
  public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {

  }

  @Override
  public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

  }

  @Override
  public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
    return false;
  }

  @Override
  public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_pitch_moins:
        break;
      case R.id.btn_pitch_plus:
        break;
      case R.id.btn_yaw_moins:
        break;
      case R.id.btn_yaw_plus:
        break;
      case R.id.btn_capturer:
        break;
      case R.id.btn_enregistrer:
        break;
      case R.id.btn_mode_photo:
        break;
      case R.id.btn_mode_video:
        break;
    }
  }

  @Override
  public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
    // TODO : Ce switch-case est-il utile?
    switch (compoundButton.getId()) {
      case R.id.btn_capturer:
        break;
    }
  }
}
