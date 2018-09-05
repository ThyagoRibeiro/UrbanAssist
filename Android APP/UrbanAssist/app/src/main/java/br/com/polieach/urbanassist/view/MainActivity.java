package br.com.polieach.urbanassist.view;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import br.com.polieach.urbanassist.R;
import br.com.polieach.urbanassist.model.Thing;

public class MainActivity extends SpeechActivity {

    private static final int QRCODE_ACTIVITY = 0, SEARCH_ACTIVITY = 1;
    private static SharedPreferences preferences;
    private EditText pointOfInterest_editText;
    private GraphView graphView;
    private Thing pointOfInterest;
    private FloatingActionButton route_fab, qrcode_fab, centralize_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);
        initializeComponents();

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        //        switch (requestCode) {
//            case 1: {
//
//                for (int grantRsult : grantResults) {
//
//                }
//
//                return;
//            }
//        }
    }

    @Override
    protected void recognizeVoiceCommand(ArrayList<String> matches) {
        super.recognizeVoiceCommand(matches);

        if (matches.contains(getResources().getString(R.string.traceRouteCommand).replace("; ", ""))) {
            route_fab.performClick();
        } else if (matches.contains(getResources().getString(R.string.qrCodeCommand).replace("; ", ""))) {
            qrcode_fab.performClick();
        } else if (matches.contains(getResources().getString(R.string.selectPOICommand).replace("; ", "")) ||
                matches.contains(getResources().getString(R.string.selectPOISymbolCommand).replace("; ", ""))) {
            pointOfInterest_editText.performClick();
        }
    }

    private void askPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO},
                1);
    }

    @Override
    protected void initializeComponents() {

        calledByAnotherActivity = false;

        pointOfInterest_editText = findViewById(R.id.pointOfInterest_text);
        pointOfInterest_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("hasText", pointOfInterest_editText.getText().toString());
                startActivityForResult(intent, SEARCH_ACTIVITY);
                overridePendingTransition(0, 0);
            }
        });

        graphView = findViewById(R.id.graphView);

        route_fab = findViewById(R.id.fab_route);
        route_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TraceRouteActivity.class);
                intent.putExtra("hasOrigin", pointOfInterest_editText.getText().toString());
                startActivity(intent);
            }
        });

        centralize_fab = findViewById(R.id.fab_centralize);
        centralize_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphView.centralize();
            }
        });

        qrcode_fab = findViewById(R.id.fab_qrcode);
        qrcode_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setOrientationLocked(false);

                String pointToQRCode = getResources().getString(R.string.pointToQRCode);
                speech(pointToQRCode);
                integrator.setCameraId(0);
                integrator.initiateScan();
            }
        });

        askPermissions();
    }

    @Override
    protected void mountInitialSpeech() {

        initialSpeech = getResources().getString(R.string.mainActivity);
        initialSpeech += getResources().getString(R.string.afterSignalOptionsInformation);
        initialSpeech += getResources().getString(R.string.selectPOICommand);
        initialSpeech += getResources().getString(R.string.qrCodeCommand);
        initialSpeech += getResources().getString(R.string.discoverWithWiFiCommand);
        initialSpeech += getResources().getString(R.string.traceRouteCommand);
        initialSpeech += getResources().getString(R.string.preferencesCommand);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (requestCode == SEARCH_ACTIVITY) {

                pointOfInterest = (Thing) data.getExtras().getSerializable("pointOfInterest");
                pointOfInterest_editText.setText(pointOfInterest.getName().getText());
            } else {
                if (result != null) {
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }
}
