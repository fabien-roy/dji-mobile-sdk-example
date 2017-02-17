package net.info420.fabien.dronetravailpratique.common;

import android.Manifest;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import net.info420.fabien.dronetravailpratique.R;

import java.util.Stack;

import dji.sdk.base.DJIBaseProduct;
import dji.thirdparty.eventbus.EventBus;

public class ActivityMain extends AppCompatActivity {

  public static final String TAG = ActivityMain.class.getName();

  private FrameLayout mContentFrameLayout;

  private ObjectAnimator mPushInAnimator;
  private ObjectAnimator mPushOutAnimator;
  private ObjectAnimator mPopInAnimator;
  private LayoutTransition mPopOutTransition;

  private Stack<SetViewWrapper> mStack;

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

    setContentView(R.layout.activity_main);

    mContentFrameLayout = (FrameLayout) findViewById(R.id.framelayout_content);

    initParams();
    EventBus.getDefault().register(this);

    IntentFilter filter = new IntentFilter();
    filter.addAction(ApplicationDrone.FLAG_CONNECTION_CHANGE);
    registerReceiver(mReceiver, filter);

  }

  @Override
  protected void onDestroy() {
    EventBus.getDefault().unregister(this);
    unregisterReceiver(mReceiver);
    super.onDestroy();
  }

  private void initParams() {
    mStack = new Stack<SetViewWrapper>();
    View view = mContentFrameLayout.getChildAt(0);
    mStack.push(new SetViewWrapper(view, R.string.activity_objectives));
  }

  private void pushView(SetViewWrapper wrapper) {
    if (mStack.size() <= 0) return;

    mContentFrameLayout.setLayoutTransition(null);

    int titleId = wrapper.getTitleId();
    View showView = wrapper.getView();

    int preTitleId = mStack.peek().getTitleId();
    View preView = mStack.peek().getView();

    mStack.push(wrapper);

    mContentFrameLayout.addView(showView);

    mPushOutAnimator.setTarget(preView);
    mPushOutAnimator.start();

    mPushInAnimator.setTarget(showView);
    mPushInAnimator.setFloatValues(mContentFrameLayout.getWidth(), 0);
    mPushInAnimator.start();

    refreshTitle();
  }

  protected BroadcastReceiver mReceiver = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {
      refreshTitle();
    }
  };

  private void refreshTitle() {
    if(mStack.size() > 1) {
      SetViewWrapper wrapper = mStack.peek();
      setTitle(wrapper.getTitleId());
    } else if(mStack.size() == 1) {
      DJIBaseProduct product = ApplicationDrone.getProductInstance();
      if(product != null && product.getModel() != null) {
        setTitle(product.getModel().getDisplayName());
      } else {
        setTitle(R.string.app_name);
      }
    }
  }

  private void popView() {

    if (mStack.size() <= 1) {
      finish();
      return;
    }

    SetViewWrapper removeWrapper = mStack.pop();

    View showView = mStack.peek().getView();
    View removeView = removeWrapper.getView();

    int titleId = mStack.peek().getTitleId();
    int preTitleId = 0;
    if (mStack.size() > 1) {
      preTitleId = mStack.get(mStack.size() - 2).getTitleId();
    }

    mContentFrameLayout.setLayoutTransition(mPopOutTransition);
    mContentFrameLayout.removeView(removeView);

    mPopInAnimator.setTarget(showView);
    mPopInAnimator.start();

    refreshTitle();

  }

  @Override
  public void onBackPressed() {
    if (mStack.size() > 1) {
      popView();
    } else {
      super.onBackPressed();
    }
  }

  public void onEventMainThread(SetViewWrapper wrapper) {
    pushView(wrapper);
  }

  public void onEventMainThread(SetViewWrapper.Remove wrapper) {

    if (mStack.peek().getView() == wrapper.getView()) {
      popView();
    }

  }

  @Override
  public void onConfigurationChanged(Configuration newConfig){
    Log.d(TAG, "onConfigurationChanged()");
    super.onConfigurationChanged(newConfig);
  }
}