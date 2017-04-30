package net.info420.fabien.dronetravailpratique.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
public class Obj2Etape2Activity extends AppCompatActivity {
  public static final String TAG = Obj1Etape2Activity.class.getName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initUI();
  }

  @Override
  protected void onDestroy(){
    super.onDestroy();

    DroneApplication.getDroneMover().land();
  }

  private void initUI(){
    setContentView(R.layout.activity_obj2_etape2);
  }
}
