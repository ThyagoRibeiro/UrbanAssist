package br.com.polieach.urbanassist.view;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import br.com.polieach.urbanassist.R;
import br.com.polieach.urbanassist.controller.ThingController;
import br.com.polieach.urbanassist.model.Edge;
import br.com.polieach.urbanassist.model.Thing;
import br.com.polieach.urbanassist.model.User;

public class DirectionsActivity extends SpeechActivity implements SensorEventListener {

    private static GraphView graphView;
    private static float degreesToGo = 0.f;
    private static int metersToGo;
    private static TextView coordinatesText;
    FloatingActionButton qrcode_fab, centralize_fab;
    private EditText origin_editText, destination_editText;
    private Thing origin, destination;
    private User user;
    private ImageView mPointer;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private String coordinates = "", newCoordinates;

    public static void showDirections(final ArrayList<Edge> edgeList) {

        degreesToGo = edgeList.get(0).getDegree();
        metersToGo = edgeList.get(0).getDistance();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                graphView.updateEdgeList(edgeList, edgeList.get(0).getOrigin(), edgeList.get(3), edgeList.get(edgeList.size() - 1).getDestination());
                graphView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initializeCompassSensor();
    }

    @Override
    protected void mountInitialSpeech() {

        initialSpeech = getResources().getString(R.string.directionsActivity);
        initialSpeech += getResources().getString(R.string.afterSignalOptionsInformation);
        initialSpeech += getResources().getString(R.string.preferencesCommand);
        initialSpeech += getResources().getString(R.string.returnCommand);
    }

    private void initializeCompassSensor() {

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void initializeComponents() {

        setContentView(R.layout.activity_directions);

        origin_editText = findViewById(R.id.origin_text);
        origin = (Thing) getIntent().getExtras().getSerializable("origin");
        origin_editText.setText(origin.getName().getText());

        destination_editText = findViewById(R.id.destination_text);
        destination = (Thing) getIntent().getExtras().getSerializable("destination");
        destination_editText.setText(destination.getName().getText());

        coordinatesText = findViewById(R.id.coordinates_text);
        mPointer = findViewById(R.id.pointer);

        graphView = findViewById(R.id.graphView);

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
                IntentIntegrator integrator = new IntentIntegrator(DirectionsActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setOrientationLocked(false);

                String pointToQRCode = getResources().getString(R.string.pointToQRCode);
                speech(pointToQRCode);
                integrator.setCameraId(0);
                integrator.initiateScan();
            }
        });

        user = new User();
        user.setIdUser(0);
        ThingController.guideToThing(origin, destination, user, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            Log.d("qrCode", result.getContents());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void recognizeVoiceCommand(ArrayList<String> matches) {
        super.recognizeVoiceCommand(matches);

        if (false) {

            // qrcode
            if (false) {
                qrcode_fab.performClick();
            } else if (false) {
                centralize_fab.performClick();
            }
        }
    }

    private String discoverDirection(float degreesToGo, float actualDegree) {

        float degree = (degreesToGo - actualDegree) - 30;

        if (degree < 10 || degree > 350)
            return "Caminhe " + metersToGo + " metros";
        if (degree < 45)
            return "Vire levemente para a esquerda";
        if (degree < 90)
            return "Vire para a esquerda";
        if (degree < 270)
            return "Dê meia volta";
        if (degree < 315)
            return "Vire para a direita";
        return "Vire levemente para a direita";
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

//        if (informingRoute) {
        if (event.sensor == mAccelerometer) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor == mMagnetometer) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);
            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;

            RotateAnimation ra = new RotateAnimation(
                    -azimuthInDegress,
                    degreesToGo,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            ra.setDuration(250);
            ra.setFillAfter(true);

            mPointer.startAnimation(ra);

            newCoordinates = discoverDirection(degreesToGo, -azimuthInDegress);

            if (!coordinates.equals(newCoordinates)) {
                coordinates = newCoordinates;
                coordinatesText.setText(coordinates);
                speech(coordinates);
            }
//            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
        mSensorManager.unregisterListener(this, mMagnetometer);
    }
}
