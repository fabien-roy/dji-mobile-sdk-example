package net.info420.fabien.dronetravailpratique.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.application.DroneApplication;

import dji.common.error.DJIError;
import dji.common.gimbal.DJIGimbalAngleRotation;
import dji.common.gimbal.DJIGimbalRotateAngleMode;
import dji.common.gimbal.DJIGimbalRotateDirection;
import dji.common.gimbal.DJIGimbalWorkMode;
import dji.common.util.DJICommonCallbacks;

/**
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-04-28
 *
 * {@link <a href="https://github.com/dji-sdk/Mobile-SDK-Android/blob/master/Sample%20Code/app/src/main/java/com/dji/sdk/sample/demo/gimbal/GimbalCapabilityView.java">
 *   Bouger le Gimbal</a>}
 * {@link <a href="https://developer.dji.com/mobile-sdk/documentation/introduction/component-guide-gimbal.html">
 *   Informations sur le gimbal</a>}
 */
public class Obj2Etape1Activity extends AppCompatActivity {
  public static final String TAG = Obj2Etape1Activity.class.getName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setupGimbal();

    initUI();
  }

  @Override
  protected void onDestroy(){
    super.onDestroy();

    DroneApplication.getDroneMover().land();
  }

  private void initUI(){
    setContentView(R.layout.activity_obj2_etape1);

    findViewById(R.id.btn_obj2_etape1_connecter).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setupGimbal();
      }
    });

    findViewById(R.id.btn_obj2_etape1_pitch_moins).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bougerGimbal(15, 0, 0);
      }
    });

    findViewById(R.id.btn_obj2_etape1_pitch_plus).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bougerGimbal(-15, 0, 0);
      }
    });

    findViewById(R.id.btn_obj2_etape1_roll_moins).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bougerGimbal(0, 15, 0);
      }
    });

    findViewById(R.id.btn_obj2_etape1_roll_plus).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bougerGimbal(0, -15, 0);
      }
    });

    findViewById(R.id.btn_obj2_etape1_yaw_moins).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bougerGimbal(0, 0, 15);
      }
    });

    findViewById(R.id.btn_obj2_etape1_yaw_plus).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        bougerGimbal(0, 0, -15);
      }
    });
  }

  /**
   * Arrange le Gimbal afin de pouvoir le bouger
   */
  private void setupGimbal() {
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
  private void bougerGimbal(DJIGimbalAngleRotation pitch, DJIGimbalAngleRotation roll, DJIGimbalAngleRotation yaw) {
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
  private void bougerGimbal(int pitch, int roll, int yaw) {
    DJIGimbalAngleRotation angleRotationPitch = new DJIGimbalAngleRotation(true, pitch, DJIGimbalRotateDirection.Clockwise);
    DJIGimbalAngleRotation angleRotationRoll  = new DJIGimbalAngleRotation(true, roll,  DJIGimbalRotateDirection.Clockwise);
    DJIGimbalAngleRotation angleRotationYaw   = new DJIGimbalAngleRotation(true, yaw,   DJIGimbalRotateDirection.Clockwise);

    bougerGimbal(angleRotationPitch, angleRotationRoll, angleRotationYaw);
  }
}
