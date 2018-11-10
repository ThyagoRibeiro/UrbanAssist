package br.com.polieach.urbanassist.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Locale;

import br.com.polieach.urbanassist.R;
import br.com.polieach.urbanassist.controller.ThingController;

public class SpeechActivity extends AppCompatActivity {

    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    protected WifiManager wifiManager;

    protected TextToSpeech tts;
    protected String initialSpeech;

    protected boolean calledByAnotherActivity = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeSpeechRecognizer();
        initializeTextToSpeech();
        mountInitialSpeech();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                speechAndWaitCommand(initialSpeech);
            }
        }, 1000);
    }

    private void askPermissions() {
        ActivityCompat.requestPermissions(SpeechActivity.this,
                new String[]{Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO},
                1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    private void initializeTextToSpeech() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if (status == TextToSpeech.SUCCESS) {
                    int result = tts.setLanguage(new Locale("pt", "POR"));
                    if (result == TextToSpeech.LANG_MISSING_DATA ||
                            result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("error", "This Language is not supported");
                    }
                } else
                    Log.e("error", "Initilization Failed!");
            }
        });
    }

    protected void initializeComponents() {

        Log.d("json", "iniciou componentes");
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        askPermissions();
        verifyWifi();

    }

    private void verifyWifi() {
        if (wifiManager.isWifiEnabled() == false) {
            Toast.makeText(getApplicationContext(), "Ativando wifi...", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }
    }

    protected void mountInitialSpeech() {
    }

    protected void speechAndWaitCommand(String text) {

        Thread t = new Thread();
        speech(text);
        while (tts.isSpeaking()) {
        }
        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
    }

    protected void speech(String text) {

        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void initializeSpeechRecognizer() {

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR");
        mSpeechRecognizerIntent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{});

        SpeechRecognitionListener listener = new SpeechRecognitionListener(this);
        mSpeechRecognizer.setRecognitionListener(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSpeechRecognizer.destroy();
        tts.stop();
        tts.shutdown();
    }

    protected void recognizeVoiceCommand(ArrayList<String> matches) {

        for (String match : matches) {
            Log.d("VoiceRecognition", match);
        }

        if (matches.contains(getResources().getString(R.string.preferencesCommand).replace("; ", ""))) {
            Intent preferencesIntent = new Intent(this, SettingsActivity.class);
            startActivity(preferencesIntent);
        }

        if (calledByAnotherActivity && matches.contains(getResources().getString(R.string.returnCommand).replace("; ", ""))) {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.qrcode_reader) {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            startActivityForResult(intent, 0);
        }

        if (id == R.id.action_settings) {
            Intent preferencesIntent = new Intent(this, SettingsActivity.class);
            startActivity(preferencesIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        Log.d("json", "onActivityResult requestCode " + requestCode);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                int idThing = Integer.parseInt(intent.getStringExtra("SCAN_RESULT"));
                Log.d("json", "onActivityResult idThing " + idThing);
                ThingController.saveWifiData(wifiManager.getScanResults(), idThing);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    protected class SpeechRecognitionListener implements RecognitionListener {

        SpeechActivity speechRecognizerActivity;

        public SpeechRecognitionListener(SpeechActivity speechRecognitionActivity) {
            this.speechRecognizerActivity = speechRecognitionActivity;
        }

        @Override
        public void onBeginningOfSpeech() {
            //Log.d(TAG, "onBeginingOfSpeech");
        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {
            //Log.d(TAG, "onEndOfSpeech");
        }

        @Override
        public void onError(int error) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onReadyForSpeech(Bundle params) {
            Log.d("VoiceRecognition", "onReadyForSpeech");
        }

        @Override
        public void onResults(Bundle results) {

            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            recognizeVoiceCommand(matches);
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }
    }


}
