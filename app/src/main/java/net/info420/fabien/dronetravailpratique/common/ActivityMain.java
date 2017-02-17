package net.info420.fabien.dronetravailpratique.common;

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

import dji.sdk.base.DJIBaseProduct;
import dji.sdk.products.DJIAircraft;

public class ActivityMain extends AppCompatActivity implements DJIBaseProduct.DJIVersionCallback {

  public static final String TAG = ActivityMain.class.getName();

  private TextView mTextProduct;
  private TextView mTextModelAvailable;
  private TextView mTextConnectionStatus;
  private Button mBtnOpen;

  private DJIBaseProduct mProduct;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(TAG, "onCreate()");

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      ActivityCompat.requestPermissions(this,
        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE,
          Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE,
          Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_COARSE_LOCATION,
          Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
          Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
          Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SYSTEM_ALERT_WINDOW,
          Manifest.permission.READ_PHONE_STATE,
        }
        , 1);
    }

    initUI();

  }

  private void initUI() {
    Log.d(TAG, "initUI()");

    setContentView(R.layout.activity_main);

    mTextModelAvailable = (TextView) findViewById(R.id.text_model_available);
    mTextProduct = (TextView) findViewById(R.id.text_product_info);
    mTextConnectionStatus = (TextView) findViewById(R.id.text_connection_status);
    mBtnOpen = (Button) findViewById(R.id.btn_open);

    mBtnOpen.setEnabled(true);

    mBtnOpen.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(TAG, "mBtnOpen : onClick()");

        // On change d'application
        startActivity(new Intent(getApplicationContext(), ActivityObjectives.class));
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


  // Vérifie si le drone est connecté et active l'interface necéssaire
  private void refreshSDKRelativeUI() {
    mProduct = ApplicationDrone.getProductInstance();

    Log.d(TAG, "mProduct: " + (mProduct == null? "null" : "unnull") );

    if (null != mProduct && mProduct.isConnected()) {
      mBtnOpen.setEnabled(true);

      mTextConnectionStatus.setText("Statut : " + (mProduct instanceof DJIAircraft ? "Aéronef DJI" : "Engin DJI") + " connecté");
      mProduct.setDJIVersionCallback(this);
      updateVersion();

      if (null != mProduct.getModel()) {
        mTextProduct.setText(mProduct.getModel().getDisplayName());
      } else {
        mTextProduct.setText(R.string.product_information);
      }
    } else {
      mBtnOpen.setEnabled(true);

      mTextProduct.setText(R.string.product_information);
      mTextConnectionStatus.setText(R.string.connection_loose);
    }
  }
}