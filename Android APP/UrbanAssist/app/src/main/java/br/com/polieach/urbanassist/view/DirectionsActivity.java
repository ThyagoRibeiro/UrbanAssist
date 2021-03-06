package br.com.polieach.urbanassist.view;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.polieach.urbanassist.R;
import br.com.polieach.urbanassist.controller.ThingController;
import br.com.polieach.urbanassist.model.Edge;
import br.com.polieach.urbanassist.model.Thing;
import br.com.polieach.urbanassist.model.User;

public class DirectionsActivity extends SpeechActivity implements SensorEventListener {

    private static float degreesToGo = 0.f, currentDegree = 0.0f;
    private static double metersToGo;
    private static String pointToGo;
    private static TextView coordinatesText;
    private EditText origin_editText, destination_editText;
    private Thing origin, destination;
    private User user;
    private static ImageView mPointer;
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
    public static ArrayList<Edge> edgeList;

    public void updateDirections() {

        if (edgeList.size() > 0) {

            Edge edge = edgeList.remove(0);

            degreesToGo = edge.getDegree();
            metersToGo = edge.getDistance();
            pointToGo = edge.getDestination().getName().getText();

            coordinatesText.setVisibility(View.VISIBLE);
            mPointer.setVisibility(View.VISIBLE);
        } else {

            coordinatesText.setText(getResources().getString(R.string.arrived));
            speech(getResources().getString(R.string.arrived));
            mPointer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initializeCompassSensor();
        initializeComponents();
    }

    @Override
    protected void mountInitialSpeech() {

        initialSpeech = getResources().getString(R.string.directionsActivity);
        initialSpeech += getResources().getString(R.string.afterSignalOptionsInformation);
        initialSpeech += getResources().getString(R.string.selectOriginCommand);
        initialSpeech += getResources().getString(R.string.selectDestinationCommand);
        initialSpeech += getResources().getString(R.string.preferencesCommand);
    }

    private void initializeCompassSensor() {

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void initializeComponents() {

        super.initializeComponents();
        setContentView(R.layout.activity_directions);

        origin_editText = findViewById(R.id.origin_text);
        origin_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DirectionsActivity.this, TraceRouteActivity.class);
                intent.putExtra("origin", origin_editText.getText().toString());
                intent.putExtra("destination", destination_editText.getText().toString());
                intent.putExtra("requestCode", 1);
                startActivityForResult(intent, 1);
                overridePendingTransition(0, 0);
            }
        });

        destination_editText = findViewById(R.id.destination_text);
        destination_editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DirectionsActivity.this, TraceRouteActivity.class);
                intent.putExtra("origin", origin_editText.getText().toString());
                intent.putExtra("destination", destination_editText.getText().toString());
                intent.putExtra("requestCode", 2);
                startActivityForResult(intent, 2);
                overridePendingTransition(0, 0);
            }
        });

        coordinatesText = findViewById(R.id.coordinates_text);
        coordinatesText.setVisibility(View.INVISIBLE);

        mPointer = findViewById(R.id.pointer);
        mPointer.setVisibility(View.INVISIBLE);

        mPointer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDirections();
            }
        });

        startLookingForThingWifi();

        user = new User();
        user.setIdUser(0);
    }

    private void startLookingForThingWifi() {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        ThingController.getThingsProbability(wifiManager.getScanResults(), DirectionsActivity.this);
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                origin = (Thing) data.getExtras().getSerializable("choice");
                origin_editText.setText(origin.getName().getText());
            }
        }

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                destination = (Thing) data.getExtras().getSerializable("choice");
                destination_editText.setText(destination.getName().getText());
            }
        }

        if (origin != null && destination != null) {
            ThingController.guideToThing(origin, destination, user, this);
        }
    }

    protected void recognizeVoiceCommand(ArrayList<String> matches) {
        super.recognizeVoiceCommand(matches);


        if (matches.contains(getResources().getString(R.string.selectOriginCommand).replace("; ", ""))) {
            origin_editText.requestFocus();
            speechAndWaitCommand("Diga o ponto de origem");
        } else if (matches.contains(getResources().getString(R.string.selectDestinationCommand).replace("; ", ""))) {
            destination_editText.requestFocus();
            speechAndWaitCommand("Diga o ponto de destino");
        }


    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (mPointer != null && mPointer.getVisibility() != View.INVISIBLE) {
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
                float azimuthInDegress = (float) ((Math.toDegrees(azimuthInRadians) - degreesToGo) + 360) % 360;

                RotateAnimation ra = new RotateAnimation(
                        currentDegree,
                        -azimuthInDegress,
                        Animation.RELATIVE_TO_SELF,
                        0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);

                ra.setDuration(250);
                ra.setFillAfter(true);

                mPointer.startAnimation(ra);

                newCoordinates = discoverDirection(azimuthInDegress);

                if (!coordinates.equals(newCoordinates)) {
                    coordinates = newCoordinates;
                    coordinatesText.setText(coordinates);
                    speech(coordinates);
                }
                currentDegree = -azimuthInDegress;
            }
        }
    }

    private String discoverDirection(float degree) {

        if (degree < 10 || degree > 350)
            return getResources().getString(R.string.walk) + " " + metersToGo + " " + getResources().getString(R.string.meters) + " " + getResources().getString(R.string.until) + " " + pointToGo;
        if (degree < 45)
            return getResources().getString(R.string.turnSlightlyLeft);
        if (degree < 135)
            return getResources().getString(R.string.turnLeft);
        if (degree < 225)
            return getResources().getString(R.string.turnAround);
        if (degree < 315)
            return getResources().getString(R.string.turnRight);
        return getResources().getString(R.string.turnSlightlyRight);
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
