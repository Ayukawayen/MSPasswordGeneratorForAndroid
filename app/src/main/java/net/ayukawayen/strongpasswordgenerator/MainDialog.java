package net.ayukawayen.strongpasswordgenerator;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class MainDialog extends Dialog implements TextWatcher, View.OnClickListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {
    SharedPreferences pref;

    int len = 16;
    int flag = 0xf;

    String base64Codec = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789._";
    byte[] passwordBytes = new byte[]{};

    public MainDialog(@NonNull Context context, SharedPreferences pref) {
        super(context, R.style.DialogTheme);

        this.pref = pref;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog);

        ((EditText)findViewById(R.id.editTextMasterKey)).addTextChangedListener(this);
        ((EditText)findViewById(R.id.editTextSiteKey)).addTextChangedListener(this);

        ((SeekBar)findViewById(R.id.seekBarLength)).setOnSeekBarChangeListener(this);

        ((CheckBox)findViewById(R.id.checkBoxNumber)).setOnCheckedChangeListener(this);
        ((CheckBox)findViewById(R.id.checkBoxLowercase)).setOnCheckedChangeListener(this);
        ((CheckBox)findViewById(R.id.checkBoxUppercase)).setOnCheckedChangeListener(this);
        ((CheckBox)findViewById(R.id.checkBoxSymbol)).setOnCheckedChangeListener(this);


        findViewById(R.id.textViewPassword).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        EditText editTextFocus = ((EditText)findViewById(R.id.editTextMasterKey));

        String savedMKey = pref.getString("masterKey", "");
        if(savedMKey.length() > 0) {
            ((EditText)findViewById(R.id.editTextMasterKey)).setText(savedMKey);
            editTextFocus = ((EditText)findViewById(R.id.editTextSiteKey));
        }

        editTextFocus.requestFocus();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    void refresh() {
        String password = PasswordGenerator.baseConvert64(passwordBytes, base64Codec);
        ((TextView)findViewById(R.id.textViewPassword)).setText(password.substring(0, Math.min(password.length(), len)));
    }

    @Override
    public void onClick(View v) {
        if(v != findViewById(R.id.textViewPassword)) return;

        String mKey = ((EditText)findViewById(R.id.editTextMasterKey)).getText().toString();
        String sKey = ((EditText)findViewById(R.id.editTextSiteKey)).getText().toString();

        pref.edit().putString("masterKey", mKey)
                .putInt(sKey+"_len", len)
                .putInt(sKey+"_flag", flag)
                .apply();

        ClipboardManager clipboardManager = (ClipboardManager) this.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Password", ((TextView)findViewById(R.id.textViewPassword)).getText().toString());
        clipboardManager.setPrimaryClip(clip);

        Toast.makeText(getContext(), R.string.toastTextCopied, Toast.LENGTH_LONG).show();

        dismiss();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String sKey = ((EditText)findViewById(R.id.editTextSiteKey)).getText().toString();
        if(sKey.length() <= 0) {
            passwordBytes = new byte[]{};
            refresh();
            return;
        }

        String mKey = ((EditText)findViewById(R.id.editTextMasterKey)).getText().toString();

        len = pref.getInt(sKey+"_len", 16);
        ((SeekBar)findViewById(R.id.seekBarLength)).setProgress(len);

        flag = pref.getInt(sKey+"_flag", 0xf);
        ((CheckBox)findViewById(R.id.checkBoxNumber)).setChecked((flag&1) > 0);
        ((CheckBox)findViewById(R.id.checkBoxLowercase)).setChecked((flag&2) > 0);
        ((CheckBox)findViewById(R.id.checkBoxUppercase)).setChecked((flag&4) > 0);
        ((CheckBox)findViewById(R.id.checkBoxSymbol)).setChecked((flag&8) > 0);

        passwordBytes = PasswordGenerator.gen(mKey, sKey);
        refresh();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        len = progress;
        ((TextView)findViewById(R.id.textViewLengthHint)).setText("Length: "+len);

        refresh();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    static String[] base64Codecs = new String[]{
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789._",
            "0123456789012345678901234501234567890123456789012345012345678900",
            "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijaa",
            "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz012345678900",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJAA",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ012345678900",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzabcdefghijaa",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz012345678900",
            "._._._._._._._._._._._._._._._._._._._._._._._._._._._._._._._._",
            "01234567890123456789012345012345678901234567890123450123456789._",
            "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghij._",
            "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz0123456789._",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJ._",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789._",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyzabcdefghij._",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789._",
    };
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        flag = 0
                | (((CheckBox)findViewById(R.id.checkBoxNumber)).isChecked() ? 1 : 0)
                | (((CheckBox)findViewById(R.id.checkBoxLowercase)).isChecked() ? 2 : 0)
                | (((CheckBox)findViewById(R.id.checkBoxUppercase)).isChecked() ? 4 : 0)
                | (((CheckBox)findViewById(R.id.checkBoxSymbol)).isChecked() ? 8 : 0)
        ;

        base64Codec = base64Codecs[flag & 0xf];

        refresh();
    }
}
