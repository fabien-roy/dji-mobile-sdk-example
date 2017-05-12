package net.info420.fabien.dronetravailpratique.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import net.info420.fabien.dronetravailpratique.R;

/**
 * {@link Activity} où tous les Objectifs et leurs étapes sont listés
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   17-02-15
 */
public class ObjectifsActivity extends Activity {
  public static final String TAG = ObjectifsActivity.class.getName();

  /**
   * Exécuté à la création de l'{@link android.app.Activity}
   *
   * <ul>
   *   <li>Initialise l'interface</li>
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
   * Inialise l'interface
   *
   * <ul>
   *   <li>Ajoute le bon {@link android.text.Layout}</li>
   *   <li>Met les Listeners</li>
   * </ul>
   */
  private void initUI(){
    setContentView(R.layout.activity_objectifs);

    findViewById(R.id.btn_obj1_etape1).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), Obj1Etape1Activity.class));
      }
    });

    findViewById(R.id.btn_obj1_etape2).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), Obj1Etape2Activity.class));
      }
    });

    findViewById(R.id.btn_obj1_etape3).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), Obj1Etape3Activity.class));
      }
    });

    findViewById(R.id.btn_obj2_etape1).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), Obj2Etape1Activity.class));
      }
    });

    findViewById(R.id.btn_obj2_etape2).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), Obj2Etape2Activity.class));
      }
    });

    findViewById(R.id.btn_obj2_etape3).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), Obj2Etape3Activity.class));
      }
    });

    findViewById(R.id.btn_obj3_etape1).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // TODO : Démarer Obj3Etape1Activity
        startActivity(new Intent(getApplicationContext(), Obj2Etape3Activity.class));
      }
    });
  }
}