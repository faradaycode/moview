package com.movie.mandiri.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.movie.mandiri.App;
import com.movie.mandiri.R;
import com.movie.mandiri.utils.MyHelpers;

import java.util.Map;

public class SplashActivity extends AppCompatActivity {
    private Context context;

    private final String[] permits = new String[]{
            Manifest.permission.INTERNET
    };

    //launcher
    private final ActivityResultLauncher<String[]> PermissionMulti = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                int denied = 0;

                for (Map.Entry<String, Boolean> par : result.entrySet()) {
                    if (!par.getValue()) {
                        denied += 1;
                    }
                }

                if (denied == 0) {
                    LaunchApp();
                } else {
                    //pakai alert dialog untuk minta ulang
                    dialogDenied();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        ((App) getApplicationContext()).getAppComponent(this).inject(this);

        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (MyHelpers.getCheckPermissions(context, permits) > 0) {
                PermissionMulti.launch(permits);
            } else {
                LaunchApp();
            }
        } else {
            // Pre-Marshmallow
            LaunchApp();
        }
    }

    private void dialogDenied() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                .setMessage("Beberapa izin akses yang diperlukan aplikasi ditolak, " +
                        "lakukan permintaan izin ulang?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        PermissionMulti.launch(permits);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finishAffinity();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    public void LaunchApp() {
        //room call
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (isNetworkConnected(SplashActivity.this)) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                                .setMessage("No Connection Available")
                                .setCancelable(false)
                                .setPositiveButton("Ok", (dialog, id) -> {
                                    dialog.dismiss();
                                    finish();
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }
                }
            }
        };
        timerThread.start();
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkCapabilities capabilities = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            }
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
//                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR");
                    return true;
                } else //                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET");
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
//                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI");
                        return true;
                    } else return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
            }
        }

        return false;
    }
}
