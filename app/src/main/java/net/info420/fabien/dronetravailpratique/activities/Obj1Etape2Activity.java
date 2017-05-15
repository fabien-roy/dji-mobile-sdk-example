package net.info420.fabien.dronetravailpratique.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.application.DroneApplication;
import net.info420.fabien.dronetravailpratique.helpers.DroneHelper;
import net.info420.fabien.dronetravailpratique.util.MouvementTimer;

/**
 * {@link android.app.Activity} pour faire bouger le drone
 *
 * @see DroneHelper
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-02-20
 */
public class Obj1Etape2Activity extends AppCompatActivity {
  public static final String TAG = Obj1Etape2Activity.class.getName();

  /**
   * Exécuté à la création de l'{@link android.app.Activity}
   *
   * <ul>
   *   <li>Appelle {@link #initUI()}</li>
   *   <li>Active le mode VirtualStick du drone</li>
   * </ul>
   *
   * @param savedInstanceState {@link Bundle}
   *
   * @see DroneHelper#setupFlightController()
   * @see #initUI()
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    DroneApplication.getDroneHelper().setupFlightController();

    initUI();
  }

  /**
   * Exécuté à la fermetture de l'{@link android.app.Activity}
   *
   * <ul>
   *   <li>Fait attérir le drone</li>
   *   <li>Désactive le mode VirtualStick du drone</li>
   * </ul>
   *
   * @see DroneHelper#atterir()
   * @see DroneHelper#disableVirtualStickMode()
   */
  @Override
  protected void onDestroy() {
    super.onDestroy();

    DroneApplication.getDroneHelper().atterir();

    DroneApplication.getDroneHelper().disableVirtualStickMode();
  }

  /**
   * Inialise l'interface
   *
   * <ul>
   *   <li>Ajoute le bon {@link android.text.Layout}</li>
   *   <li>Met les Listeners</li>
   * </ul>
   *
   * <p>Les floats que j'envoie au {@link MouvementTimer}
   * sont dans l'ordre pitch, roll, yaw , throttle</p>
   * <p>On se sert actuellement du mode Velocity et Body.</p>
   *
   * @see DroneHelper#decoller()
   * @see DroneHelper#atterir()
   * @see DroneHelper#getMovementTimer(String, Float[], Integer)
   * @see MouvementTimer
   *
   * @see <a href="https://developer.dji.com/mobile-sdk/documentation/introduction/component-guide-flightController.html"
   *      target="_blank">
   *      Source : Roll Pitch Control Mode</a>
   */
  private void initUI(){
    setContentView(R.layout.activity_obj1_etape2);

    findViewById(R.id.btn_obj1_etape2_demarrer_moteurs).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneHelper().demarrerMoteurs();
      }
    });

    findViewById(R.id.btn_obj1_etape2_atterir).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneHelper().atterir();
      }
    });

    findViewById(R.id.btn_obj1_etape2_aller_avant).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneHelper().sendMovementTimer(DroneApplication.getDroneHelper().getMovementTimer(null, new Float[] {0F, 1F, 0F, 0F}, null));
      }
    });

    findViewById(R.id.btn_obj1_etape2_aller_derriere).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneHelper().sendMovementTimer(DroneApplication.getDroneHelper().getMovementTimer(null, new Float[] {0F, -1F, 0F, 0F}, null));
      }
    });

    findViewById(R.id.btn_obj1_etape2_aller_gauche).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneHelper().sendMovementTimer(DroneApplication.getDroneHelper().getMovementTimer(null, new Float[] {-1F, 0F, 0F, 0F}, null));
      }
    });

    findViewById(R.id.btn_obj1_etape2_aller_droite).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneHelper().sendMovementTimer(DroneApplication.getDroneHelper().getMovementTimer(null, new Float[] {1F, 0F, 0F, 0F}, null));
      }
    });

    findViewById(R.id.btn_obj1_etape2_tourner_droite).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneHelper().sendMovementTimer(DroneApplication.getDroneHelper().getMovementTimer(null, new Float[] {0F, 0F, 15F, 0F}, null));
      }
    });

    findViewById(R.id.btn_obj1_etape2_tourner_gauche).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneHelper().sendMovementTimer(DroneApplication.getDroneHelper().getMovementTimer(null, new Float[] {0F, 0F, -15F, 0F}, null));
      }
    });
  }
}