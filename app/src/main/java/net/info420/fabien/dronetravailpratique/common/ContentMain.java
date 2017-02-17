package net.info420.fabien.dronetravailpratique.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.info420.fabien.dronetravailpratique.R;

import dji.sdk.base.DJIBaseProduct;
import dji.sdk.products.DJIAircraft;
import dji.thirdparty.eventbus.EventBus;

/**
 * Created by fabien on 17-02-14.
 */

public class ContentMain extends RelativeLayout implements DJIBaseProduct.DJIVersionCallback {

  public static final String TAG = ContentMain.class.getName();

  private TextView mTextProduct;
  private TextView mTextModelAvailable;
  private TextView mTextConnectionStatus;
  private Button mBtnOpen;

  private DJIBaseProduct mProduct;

  public ContentMain(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    initUI();
  }

  private void initUI() {
    Log.d(TAG, "initUI()");

    mTextModelAvailable = (TextView) findViewById(R.id.text_model_available);
    mTextProduct = (TextView) findViewById(R.id.text_product_info);
    mTextConnectionStatus = (TextView) findViewById(R.id.text_connection_status);
    mBtnOpen = (Button) findViewById(R.id.btn_open);

    mBtnOpen.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.d(TAG, "onClick : mBtnOpen");
        EventBus.getDefault().post(new SetViewWrapper(R.layout.content_objectives, R.string.activity_objectives, getContext()));
      }
    });
  }

  @Override
  protected void onAttachedToWindow() {
    Log.d(TAG, "onAttachedToWindow()");
    refreshSDKRelativeUI();
    IntentFilter filter = new IntentFilter();
    filter.addAction(ApplicationDrone.FLAG_CONNECTION_CHANGE);
    getContext().registerReceiver(mReceiver, filter);
    super.onAttachedToWindow();
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
    Toast.makeText(getContext(), "mProduct: " + (mProduct == null? "null" : "unnull"), Toast.LENGTH_LONG).show();
    Log.d(TAG, "mProduct: " + (mProduct == null? "null" : "unnull") );
    if (null != mProduct && mProduct.isConnected()) {
      mBtnOpen.setEnabled(true);

      String str = mProduct instanceof DJIAircraft ? "DJIAircraft" : "DJIHandHeld";
      mTextConnectionStatus.setText("Statut : " + str + " connecté");
      mProduct.setDJIVersionCallback(this);
      updateVersion();

      if (null != mProduct.getModel()) {
        mTextProduct.setText("" + mProduct.getModel().getDisplayName());
      } else {
        mTextProduct.setText(R.string.product_information);
      }
    } else {
      mBtnOpen.setEnabled(false);

      mTextProduct.setText(R.string.product_information);
      mTextConnectionStatus.setText(R.string.connection_loose);
    }
  }
}
