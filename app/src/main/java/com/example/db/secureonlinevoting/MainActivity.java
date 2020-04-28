package com.example.db.secureonlinevoting;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.google.common.hash.Hashing;
import android.widget.EditText;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String SCAN_FAILED = "eRROR 404";
    public static final int SCAN_REQUEST = 0;
    public static final String EXTRA_MESSAGEE = "com.example.myfirstapp.MESSAGE2";
    String ScanContents="";
    String name="T";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Send button */

    public void sendMessage(View view) {
        // Do something in response to button

        Intent intent = new Intent(this, DisplayMessageActivity.class);

        String message = ScanContents;
        final String hashed = Hashing.sha256()
                .hashString(message, StandardCharsets.UTF_8)
                .toString();

        intent.putExtra(EXTRA_MESSAGE, hashed);
        startActivity(intent);
    }
    public void QrCodeScanner(View scan){
        /**Call zxing app and get input*/
        Intent scanIntent = new Intent("com.google.zxing.client.android.SCAN");
        scanIntent.putExtra("SCAN_MODE", "QR_CODE_MODE");//for Qr code, its "QR_CODE_MODE" instead of "PRODUCT_MODE"
        scanIntent.putExtra("SAVE_HISTORY", false);//this stops saving ur barcode in barcode scanner app's history
        // Verify it resolves
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(scanIntent, 0);
        boolean isIntentSafe = activities.size() > 0;
        if (isIntentSafe) {
            startActivityForResult(scanIntent, SCAN_REQUEST);
        }
        else {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);
        }


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent scanIntent) {
        // Check which request we're responding to
        if (requestCode == SCAN_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                ScanContents = scanIntent.getStringExtra("SCAN_RESULT"); //this is the result

            }
            //Handle Scan error
            else {
                ScanContents = "ERR/404";
            }

        }
    }
}
