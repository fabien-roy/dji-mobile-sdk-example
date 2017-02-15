package net.info420.fabien.dronetravailpratique.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.info420.fabien.dronetravailpratique.R;

import dji.sdk.base.DJIBaseProduct;
import dji.sdk.products.DJIAircraft;
import dji.thirdparty.eventbus.EventBus;

/**
 * Created by fabien on 17-02-14.
 */

public class ContentMain extends RelativeLayout {

  public static final String TAG = ContentMain.class.getName();

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
