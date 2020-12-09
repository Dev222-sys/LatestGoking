package com.gokings;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.kaopiz.kprogresshud.KProgressHUD;

public class util {
    KProgressHUD pDialog;


    public static void blackiteamstatusbar(Activity activity, int color)
{
    activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

    activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity,color));//getStatusBarColor(ContextCompat.getColor(activity,color)) ;
}

    public static void showtoast(Activity activity,String msg)
    {
        View view=activity.getLayoutInflater().inflate(R.layout.layout_toast,null);
        TextView text = (TextView) view.findViewById(R.id.toast);
        text.setText(msg);

        Toast toast=new Toast(activity);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

}
