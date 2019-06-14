package com.aptoide.walletpermissionssample;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPermissions = (Button)findViewById(R.id.btnPermissions);
        btnPermissions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("appcoins://wallet/permissions/1"));
                    intent.putExtra("PERMISSION_NAME_KEY", "WALLET_ADDRESS");
                    startActivityForResult(intent, 123);
                } catch (ActivityNotFoundException anfe) {
                    //If no AppCoins wallet found display popup to install it
                    showWalletInstallDialog(v.getContext(),
                            getString(R.string.install_wallet_from_iab));
                }
            }
        });
    }
    private static void showWalletInstallDialog(Context context, String message) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.wallet_missing)
                .setMessage(message)
                .setPositiveButton(R.string.install, (dialog, which) -> gotoStore(context))
                .setNegativeButton(R.string.skip, (DialogInterface dialog, int which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private static void gotoStore(Context activity) {
        String appPackageName = "com.appcoins.wallet";

        //First try getting the AppCoins wallet from Aptoide
        try {
            activity.startActivity(
                    new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            //If it fails get it from the play store
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
