package com.splashandroid;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

public class SplashScreen {
  private static Dialog mSplashDialog;
  private static WeakReference<Activity> mActivity;

  public static void show(final Activity activity, final int themeResId, final boolean fullScreen) {
    if (activity == null)
      return;
    mActivity = new WeakReference<Activity>(activity);
    activity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (!activity.isFinishing()) {
          mSplashDialog = new Dialog(activity, themeResId);
          mSplashDialog.setContentView(R.layout.launch_screen);
          mSplashDialog.setCancelable(false);
          if (fullScreen) {
            setActivityAndroidP(mSplashDialog);
          }
          if (!mSplashDialog.isShowing()) {
            mSplashDialog.show();
          }
        }
      }
    });
  }

  public static void show(final Activity activity, final boolean fullScreen) {
    int resourceId = fullScreen ? R.style.SplashScreen_Fullscreen : R.style.SplashScreen_SplashTheme;

    show(activity, resourceId, fullScreen);
  }

  public static void show(final Activity activity) {
    show(activity, false);
  }

  public static void hide(Activity activity) {
    if (activity == null) {
      if (mActivity == null) {
        return;
      }
      activity = mActivity.get();
    }

    if (activity == null)
      return;

    final Activity _activity = activity;

    _activity.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (mSplashDialog != null && mSplashDialog.isShowing()) {
          boolean isDestroyed = false;

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            isDestroyed = _activity.isDestroyed();
          }

          if (!_activity.isFinishing() && !isDestroyed) {
            mSplashDialog.dismiss();
          }
          mSplashDialog = null;
        }
      }
    });
  }

  private static void setActivityAndroidP(Dialog dialog) {
    if (Build.VERSION.SDK_INT >= 28) {
      if (dialog != null && dialog.getWindow() != null) {
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        dialog.getWindow().setAttributes(lp);
      }
    }
  }
}