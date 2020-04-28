package com.example.db.secureonlinevoting;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class DisplayMessageActivity extends AppCompatActivity {

    public static final String SCAN_FAILED = "eRROR 404";
    public static final int SCAN_REQUEST = 0;
    public static final String EXTRA_MESSAGEE = "com.example.myfirstapp.MESSAGE2";
    String ScanContents="";
    String name="T";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        name = message;
        if (message == null){
            message = "NULL";
        }



        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.textView_display_message);
        textView.setText(getString(R.string.enc).concat(message));
    }
    /** Called to direct user to new page */

    public void displayScanResult(View view) {
        /*//Do something in response to button
        Intent intent = new Intent(this, DisplayResult.class);
        intent.putExtra(EXTRA_MESSAGEE, ".");
        startActivity(intent);*/
        String msg = ScanContents.toString();
        final String hashed = Hashing.sha256()
                .hashString(msg, StandardCharsets.UTF_8)
                .toString();
        String a = name + " " + hashed;

        //Do something in response to button
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("message/rfc822");
        sendIntent.setPackage("com.google.android.gm");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "SecureOnlineVoting@gmail.com" ,"ScanVote@gmail.com", "dbhatta1232@gmail.com"});
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "85rff8e2");
        sendIntent.putExtra(Intent.EXTRA_TEXT,a);
        //sendIntent.putExtra(Intent.EXTRA_TEXT, new String[] {name," ",ScanContents});
        if (sendIntent.resolveActivity(getPackageManager())!=null)
            startActivity(sendIntent);
        else {
            Uri marketUri = Uri.parse("market://details?id=com.google.android.gm");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);
        }

    }
    /** Called when the user taps the OK button */

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
                Context context = getApplicationContext();
                Toast.makeText(context,"Couldn't Scan. Try again.",Toast.LENGTH_SHORT).show();
            }

        }
    }


}
