package net.ayukawayen.strongpasswordgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Dialog dialog = new MainDialog(this, getSharedPreferences(getString(R.string.prefKey), Context.MODE_PRIVATE));
        dialog.setOnDismissListener(this);
        dialog.show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        this.finish();
    }
}