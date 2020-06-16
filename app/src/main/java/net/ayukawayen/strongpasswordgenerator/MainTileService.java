package net.ayukawayen.strongpasswordgenerator;

import android.content.Context;
import android.content.SharedPreferences;
import android.service.quicksettings.TileService;

public class MainTileService extends TileService {
    SharedPreferences pref;

    public MainTileService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        pref = getSharedPreferences(getString(R.string.prefKey), Context.MODE_PRIVATE);
    }

    @Override
    public void onClick() {
        super.onClick();

        this.showDialog(new MainDialog(this, pref));
    }
}