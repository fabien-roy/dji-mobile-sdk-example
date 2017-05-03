package net.info420.fabien.dronetravailpratique.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
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

// TODO : Documenter PrincipaleActivity

public class PrincipaleActivity extends AppCompatActivity implements DJIBaseProduct.DJIVersionCallback {

  public static final String TAG = PrincipaleActivity.class.getName();

  private TextView mTextProduct;
  private TextView mTextModelAvailable;
  private TextView mTextConnectionStatus;
  private Button mBtnOpen;
  private Button mBtnOpenAnyway; // TODO : Enlever ceci
  private Button mBtnRefresh;

  private DJIBaseProduct mProduct;
  private DJIAircraft mAircraft;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      ActivityCompat.requestPermissions(this,
                                        new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE,
                                                      Manifest.permission.INTERNET,               Manifest.permission.ACCESS_WIFI_STATE,
                                                      Manifest.permission.WAKE_LOCK,              Manifest.permission.ACCESS_COARSE_LOCATION,
                                                      Manifest.permission.ACCESS_NETWORK_STATE,   Manifest.permission.ACCESS_FINE_LOCATION,
                                                      Manifest.permission.CHANGE_WIFI_STATE,      Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                                                      Manifest.permission.READ_EXTERNAL_STORAGE,  Manifest.permission.SYSTEM_ALERT_WINDOW,
                                                      Manifest.permission.READ_PHONE_STATE },
                                        1);
    }

    initUI();
    refreshSDKRelativeUI();
  }

  private void initUI() {
    setContentView(R.layout.activity_principale);

    mTextModelAvailable   = (TextView) findViewById(R.id.tv_principale_modele_accessible);
    mTextProduct          = (TextView) findViewById(R.id.tv_principale_produit_info);
    mTextConnectionStatus = (TextView) findViewById(R.id.tv_principale_status_connexion);
    mBtnOpen              = (Button)   findViewById(R.id.btn_principale_ouvrir);
    mBtnOpenAnyway        = (Button)   findViewById(R.id.btn_principale_ouvrir_pareil);
    mBtnRefresh           = (Button)   findViewById(R.id.btn_principale_rafraichir);

    mBtnOpen.setEnabled(false);

    mBtnOpen.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // On change d'application
        startActivity(new Intent(getApplicationContext(), ObjectifsActivity.class));
      }
    });

    mBtnOpenAnyway.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // On change d'application
        startActivity(new Intent(getApplicationContext(), ObjectifsActivity.class));
      }
    });

    mBtnRefresh.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        refreshSDKRelativeUI();
      }
    });
  }

  private void updateVersion() {
    String version = null;
    if(mProduct != null) {
      version = mProduct.getFirmwarePackageVersion();
    }

    if(version == null) {
      mTextModelAvailable.setText("N/A");
    } else {
      mTextModelAvailable.setText(version);
    }
  }

  @Override
  public void onProductVersionChange(String oldVersion, String newVersion) {
    updateVersion();
  }


  // Revérifier
  protected BroadcastReceiver mReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      Log.d(TAG, "BroadcastReceiver : onReceive()");
      refreshSDKRelativeUI();
    }
  };

  // Ajuste les limitations de vol
  protected boolean setFlightLimitation() {

    boolean isSafe = true;

    mAircraft = DroneApplication.getAircraftInstance();

    // Altitude maximale
    mAircraft.getFlightController().getFlightLimitation().setMaxFlightHeight(DroneApplication.MAX_HAUTEUR_VOL, new DJICommonCallbacks.DJICompletionCallback () {
        @Override
        public void onResult(DJIError djiError) {
          if (djiError != null) {
            Log.e(TAG, String.format("Erreur de limitation : %s", djiError.getDescription()));
          }

          // TODO : isSafe ne marche pas
          // isSafe = false;
        }
      });

    // Altiture de retour à la maison
    mAircraft.getFlightController().setGoHomeAltitude(DroneApplication.MAX_GO_HOME_ALTITUDE, new DJICommonCallbacks.DJICompletionCallback () {
      @Override
      public void onResult(DJIError djiError) {
        if (djiError != null) {
          Log.e(TAG, String.format("Erreur de limitation : %s", djiError.getDescription()));
        }

        // isSafe = false;
      }
    });

    // TODO : Inclinaise maximale

    // TODO : Vitesse de déplacement maximale

    return isSafe;
  }


  // Vérifie si le drone est connecté et active l'interface necéssaire
  private void refreshSDKRelativeUI() {

    mProduct = DroneApplication.getProductInstance();

    Log.d(TAG, "mProduct: " + (mProduct == null? "null" : "unnull") );

    if (null != mProduct && mProduct.isConnected()) {

      if (setFlightLimitation()) {

        mBtnOpen.setEnabled(true);

        mTextConnectionStatus.setText("Statut : " + (mProduct instanceof DJIAircraft ? "Aéronef DJI" : "Engin DJI") + " connecté");
        mProduct.setDJIVersionCallback(this);
        updateVersion();

        if (null != mProduct.getModel()) {
          mTextProduct.setText(mProduct.getModel().getDisplayName());
        } else {
          mTextProduct.setText(R.string.produit_information);
        }

        DroneApplication.getAircraftInstance().getFlightController();

      } else {

        Log.d(TAG, "Problème avec la sécurité du drone!");
      }

    } else {

      mBtnOpen.setEnabled(false);
      // mBtnOpen.setEnabled(true);

      mTextProduct.setText(R.string.produit_information);
      mTextConnectionStatus.setText(R.string.connexion_perdue);
    }
  }
}