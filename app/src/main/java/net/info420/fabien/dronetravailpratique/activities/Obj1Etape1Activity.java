package net.info420.fabien.dronetravailpratique.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.application.DroneApplication;
import net.info420.fabien.dronetravailpratique.helpers.DroneHelper;

/**
 * {@link android.app.Activity} pour faire décoller et attérir le drone
 *
 * @see DroneHelper
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   ?
 */
public class Obj1Etape1Activity extends AppCompatActivity {
  public static final String TAG = Obj1Etape1Activity.class.getName();

  /**
   * Exécuté à la création de l'{@link android.app.Activity}
   *
   * <ul>
   *   <li>Appele {@link #initUI()}</li>
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
  }

  /**
   * Exécuté à la fermetture de l'{@link android.app.Activity}
   *
   * <ul>
   *   <li>Fait attérir le drone</li>
   * </ul>
   *
   * @see DroneHelper#atterir()
   */
  @Override
  protected void onDestroy(){
    super.onDestroy();

    DroneApplication.getDroneHelper().atterir();
  }

  /**
   * Inialise l'interface
   *
   * <ul>
   *   <li>Ajoute le bon {@link android.text.Layout}</li>
   *   <li>Met les Listeners</li>
   * </ul>
   *
   * @see DroneHelper#decoller()
   * @see DroneHelper#atterir()
   */
  private void initUI(){
    setContentView(R.layout.activity_obj1_etape1);

    findViewById(R.id.btn_obj1_etape1_decoller).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneHelper().decoller();
      }
    });

    findViewById(R.id.btn_ob1_etape1_arreter).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DroneApplication.getDroneHelper().atterir();
      }
    });
  }
}