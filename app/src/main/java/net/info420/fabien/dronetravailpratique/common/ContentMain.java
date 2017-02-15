package net.info420.fabien.dronetravailpratique.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.info420.fabien.dronetravailpratique.R;

import dji.sdk.base.DJIBaseProduct;
import dji.sdk.products.DJIAircraft;

/**
 * Created by fabien on 17-02-14.
 */

public class ContentMain extends RelativeLayout {

  public static final String TAG = ContentMain.class.getName();

  private TextView mTextConnectionStatus;
  private TextView mTextProduct;
  private TextView mTextModelAvailable;
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

    mTextConnectionStatus = (TextView) findViewById(R.id.text_connection_status);
    mTextModelAvailable = (TextView) findViewById(R.id.text_model_available);
    mTextProduct = (TextView) findViewById(R.id.text_product_info);
    mBtnOpen = (Button) findViewById(R.id.btn_open);
  }

  // Vérifie si le drone est connecté et active l'interface necéssaire
  private void refreshSDKRelativeUI() {
    mProduct = ApplicationDrone.getProductInstance();
    Log.d(TAG, "mProduct: " + (mProduct == null? "null" : "unnull") );
    if (null != mProduct && mProduct.isConnected()) {
      mBtnOpen.setEnabled(true);

      String str = mProduct instanceof DJIAircraft ? "DJIAircraft" : "DJIHandHeld";
      mTextConnectionStatus.setText("Status: " + str + " connected");
    } else {
      mBtnOpen.setEnabled(false);

      mTextConnectionStatus.setText(R.string.connection_loose);
    }
  }
}
