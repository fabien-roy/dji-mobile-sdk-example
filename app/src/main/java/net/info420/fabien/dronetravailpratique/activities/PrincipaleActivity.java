package net.info420.fabien.dronetravailpratique.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.info420.fabien.dronetravailpratique.R;
import net.info420.fabien.dronetravailpratique.application.DroneApplication;

import dji.common.error.DJIError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.products.DJIAircraft;

/**
 * {@link android.app.Activity} d'entrée à l'{@link android.app.Application}
 *
 * @author  Fabien Roy
 * @version 1.0
 * @since   ?
 */
public class PrincipaleActivity extends AppCompatActivity implements DJIBaseProduct.DJIVersionCallback {
  public static final String TAG = PrincipaleActivity.class.getName();

  private TextView tvProduitInfo;
  private TextView tvModeleAccessible;
  private TextView tvStatusConnexion;
  private Button   btnOuvrir;

  private DJIBaseProduct product;

  /**
   * Exécuté à la création de l'{@link android.app.Activity}
   *
   * <ul>
   *   <li>Demande les permissions à l'usager</li>
   *   <li>Initialise l'interface</li>
   *   <li>Refraîchit l'interface relatif au SDK</li>
   * </ul>
   *
   * @param savedInstanceState {@link Bundle}
   *
   * @see ActivityCompat#requestPermissions(Activity, String[], int)
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      ActivityCompat.requestPermissions(this,
                                        new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                      Manifest.permission.VIBRATE,
                                                      Manifest.permission.INTERNET,
                                                      Manifest.permission.ACCESS_WIFI_STATE,
                                                      Manifest.permission.WAKE_LOCK,
                                                      Manifest.permission.ACCESS_COARSE_LOCATION,
                                                      Manifest.permission.ACCESS_NETWORK_STATE,
                                                      Manifest.permission.ACCESS_FINE_LOCATION,
                                                      Manifest.permission.CHANGE_WIFI_STATE,
                                                      Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                                                      Manifest.permission.READ_EXTERNAL_STORAGE,
                                                      Manifest.permission.SYSTEM_ALERT_WINDOW,
                                                      Manifest.permission.READ_PHONE_STATE },
                                        1);
    }

    initUI();
    rafrachirUI();
  }

  /**
   * Inialise l'interface
   *
   * <ul>
   *   <li>Ajoute le bon {@link android.text.Layout}</li>
   *   <li>Instancie les {@link View}</li>
   *   <li>Met les Listeners</li>
   *   <li>Désactive btnOuvrir</li>
   * </ul>
   *
   * @see ObjectifsActivity
   * @see #rafrachirUI()
   */
  private void initUI() {
    setContentView(R.layout.activity_principale);

    tvModeleAccessible  = (TextView) findViewById(R.id.tv_principale_modele_accessible);
    tvProduitInfo       = (TextView) findViewById(R.id.tv_principale_produit_info);
    tvStatusConnexion   = (TextView) findViewById(R.id.tv_principale_status_connexion);
    btnOuvrir           = (Button)   findViewById(R.id.btn_principale_ouvrir);

    btnOuvrir.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // On change d'application
        startActivity(new Intent(getApplicationContext(), ObjectifsActivity.class));
      }
    });

    findViewById(R.id.btn_principale_ouvrir_pareil).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // On change d'application
        startActivity(new Intent(getApplicationContext(), ObjectifsActivity.class));
      }
    });

    findViewById(R.id.btn_principale_rafraichir).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        rafrachirUI();
      }
    });

    btnOuvrir.setEnabled(false);
  }

  /**
   * Vérifie si le drone est connecté et active l'interface necéssaire
   *
   * <ul>
   *   <li>Vérifie si le drone est connecté. Si oui :
   *   <ul>
   *     <li>Ajuste les limitations de vols</li>
   *     <li>Ajoute le callback au produit</li>
   *     <li>Active le bouton pour accèder au reste de l'{@link android.app.Application}</li>
   *     <li>Remplit les textes des informations du drone</li>
   *   </ul></li>
   *   <li>Sinon :
   *   <ul>
   *     <li>Désactive le bouton pour accèder au reste de l'{@link android.app.Application}</li>
   *     <li>Remplit les textes des informations du drone</li>
   *   </ul></li>
   * </ul>
   *
   * @see #ajusterLimitesVol()
   * @see dji.sdk.base.DJIBaseProduct.DJIVersionCallback
   * @see #mettreAJourVersion()
   */
  private void rafrachirUI() {
    product = DroneApplication.getProductInstance();

    if (null != product && product.isConnected()) {
      ajusterLimitesVol();
      product.setDJIVersionCallback(this);

      btnOuvrir.setEnabled(true);
      tvStatusConnexion.setText("Statut : " + (product instanceof DJIAircraft ? "Aéronef DJI" : "Engin DJI") + " connecté");
      mettreAJourVersion();

      // On vérifie si le nom du modèle du drone est présent
      if (null != product.getModel()) {
        tvProduitInfo.setText(product.getModel().getDisplayName());
      } else {
        tvProduitInfo.setText(R.string.produit_information);
      }
    } else {
      btnOuvrir.setEnabled(false);

      tvProduitInfo.setText(R.string.produit_information);
      tvStatusConnexion.setText(R.string.connexion_perdue);
    }
  }

  /**
   * Met à jour le texte de la version du SDK
   *
   * <ul>
   *   <li>Va chercher la version du produit</li>
   *   <li>Affiche la version du produit</li>
   * </ul>
   *
   * @see DJIBaseProduct#getFirmwarePackageVersion()
   */
  private void mettreAJourVersion() {
    String version = null;

    if(product != null) {
      version = product.getFirmwarePackageVersion();
    }

    tvModeleAccessible.setText(version == null ? "N/A" : version);
  }

  /**
   * Exécuté lorsque la version du {@link DJIBaseProduct} change
   *
   * @param vieilleVersion  String de la vieille version du {@link DJIBaseProduct}
   * @param nouvelleVersion String de la nouvelle version du {@link DJIBaseProduct}
   *
   * @see DJIBaseProduct
   * @see #mettreAJourVersion()
   */
  @Override
  public void onProductVersionChange(String vieilleVersion, String nouvelleVersion) {
    mettreAJourVersion();
  }

  /**
   * Ajuste les limitations du vol
   *
   * <ul>
   *   <li>Ajoute une altitude maximale</li>
   *   <li>Ajuste une altitude de retour à la maison</li>
   *   <li>Ajuste l'inclinaison maximale</li>
   *   <li>Ajuste la vitesse de déplacement maximale</li>
   * </ul>
   *
   * @see dji.sdk.flightcontroller.DJIFlightLimitation#setMaxFlightHeight(float, DJICommonCallbacks.DJICompletionCallback)
   * @see dji.sdk.flightcontroller.DJIFlightController#setGoHomeAltitude(float, DJICommonCallbacks.DJICompletionCallback)
   */
  protected void ajusterLimitesVol() {
    DroneApplication.getAircraftInstance().getFlightController().getFlightLimitation().setMaxFlightHeight(DroneApplication.MAX_HAUTEUR_VOL, new DJICommonCallbacks.DJICompletionCallback () {
        @Override
        public void onResult(DJIError djiError) {
          if (djiError != null) {
            Log.e(TAG, String.format("Erreur de limitation : %s", djiError.getDescription()));
          }
        }
      });

    DroneApplication.getAircraftInstance().getFlightController().setGoHomeAltitude(DroneApplication.MAX_GO_HOME_ALTITUDE, new DJICommonCallbacks.DJICompletionCallback () {
      @Override
      public void onResult(DJIError djiError) {
        if (djiError != null) {
          Log.e(TAG, String.format("Erreur de limitation : %s", djiError.getDescription()));
        }
      }
    });

    // TODO : Inclinaise maximale

    // TODO : Vitesse de déplacement maximale
  }
}