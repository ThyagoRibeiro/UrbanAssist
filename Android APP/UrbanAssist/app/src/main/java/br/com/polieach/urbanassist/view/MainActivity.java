package br.com.polieach.urbanassist.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import br.com.polieach.urbanassist.R;
import br.com.polieach.urbanassist.controller.DirectionController;
import br.com.polieach.urbanassist.controller.ThingController;
import br.com.polieach.urbanassist.model.Edge;
import br.com.polieach.urbanassist.model.Thing;
import br.com.polieach.urbanassist.model.User;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final int NEIGHBOURS_DEPTH = 2;
    private static SharedPreferences preferences;
    private User user = new User();
    private EditText originText, destinationText;
    private TextView[] details = new TextView[11];

    private static GraphView graphView;
    private FloatingActionButton qrcodeFab, centralizeFab, routeFab;
    ListView resultList;

    private Thing thingOrigin, thingDestination;
    private ArrayList<Thing> thingList;

    boolean discoveringNeighbours = true, isOrigin, fromList;

    private static final int MAIN_VIEW = 0, FIND_ROUTE_VIEW = 1, GUIDE_ROUTE_VIEW = 2;
    private static final int DETAILS_NAME = 0, DETAILS_DESCRIPTION = 1, DETAILS_CLASS = 2, DETAILS_LOCALITY = 3, DETAILS_ALERT = 4, DETAILS_DISPLAY = 5, DETAILS_MESSAGE = 6, DETAILS_SITUATION = 7, DETAILS_RESPONSIBLE_NAME = 8, DETAILS_RESPONSIBLE_PHONE = 9, DETAILS_RESPONSIBLE_EMAIL = 10;
    private LinearLayout coordinatesLayout;

    private Stack<Integer> viewStack = new Stack<>();
    private int currentView = MAIN_VIEW;

    private boolean discoverThing = true;

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
    private static float degreesToGo = 0f;
    private static int metersToGo;
    private static TextView coordinatesText;

    private String coordinates = "", newCoordinates;
    private TextToSpeech tts;
    private int result;
    private boolean informingRoute = false;

    private static ArrayList<Edge> tempEdgeList;

    private static final String mainCommands = "Tela principal. Para exibir mais informações de um ponto fale procurar ponto. Para ler um código fale ler código. Para traçar a rota entre dois pontos fale traçar rota. Para configurar o aplicativo fale configurar.";
    private static final String routeCommands = "Tela de rota. Para selecionar ponto de origem fale selecionar origem. Para selecionar ponto de destino fale selecionar destino. Para configurar o aplicativo fale configurar.";

    private SpeechRecognizer speechRecognizer;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        user.setIdUser(1);

        originText = findViewById(R.id.origin_text);
        originText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (!fromList) {
                    isOrigin = true;
                    ThingController.searchThing(originText.getText().toString(), MainActivity.this);
                } else {
                    fromList = false;
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        destinationText = findViewById(R.id.destination_text);
        destinationText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (!fromList) {
                    isOrigin = false;
                    ThingController.searchThing(destinationText.getText().toString(), MainActivity.this);
                } else {
                    fromList = false;
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        coordinatesLayout = findViewById(R.id.coordinates_layout);

        coordinatesText = findViewById(R.id.coordinates_text);
        mPointer = findViewById(R.id.pointer);

        resultList = findViewById(R.id.result_list);
        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                fromList = true;

                if (isOrigin) {
                    thingOrigin = thingList.get(i);
                    originText.setText(thingList.get(i).getName().getText());
                } else {
                    thingDestination = thingList.get(i);
                    destinationText.setText(thingList.get(i).getName().getText());
                }

                resultList.setAdapter(null);
                resultList.setVisibility(View.GONE);

                getEdges();
            }
        });

        graphView = findViewById(R.id.graph_view);
        graphView.setMainActivity(this);

        qrcodeFab = findViewById(R.id.fab_qrcode);
        qrcodeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//                integrator.setOrientationLocked(false);
//                integrator.setPrompt("Aponte a câmera para o QRCode");
//                tts("Aponte a câmera para o QRCode");
//                integrator.setCameraId(0);
//                integrator.initiateScan();

                if (tempEdgeList.size() > 0)
                    updateNextDirection(tempEdgeList.remove(0));
            }
        });

        centralizeFab = findViewById(R.id.fab_centralize);
        centralizeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                graphView.centralize();
            }
        });

        routeFab = findViewById(R.id.fab_route);
        routeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFindRouteView();
            }
        });

        details[DETAILS_NAME] = findViewById(R.id.details_name);
        details[DETAILS_DESCRIPTION] = findViewById(R.id.details_description);
        details[DETAILS_CLASS] = findViewById(R.id.details_class);
        details[DETAILS_LOCALITY] = findViewById(R.id.details_locality);
        details[DETAILS_ALERT] = findViewById(R.id.details_alert);
        details[DETAILS_DISPLAY] = findViewById(R.id.details_display);
        details[DETAILS_MESSAGE] = findViewById(R.id.details_message);
        details[DETAILS_SITUATION] = findViewById(R.id.details_situation);
        details[DETAILS_RESPONSIBLE_NAME] = findViewById(R.id.details_responsible_name);
        details[DETAILS_RESPONSIBLE_PHONE] = findViewById(R.id.details_responsible_phone);
        details[DETAILS_RESPONSIBLE_EMAIL] = findViewById(R.id.details_responsible_email);

        initializeTextToSpeech();
        initializeSpeechRecognizer();
    }

    private void initializeSpeechRecognizer() {

        if (SpeechRecognizer.isRecognitionAvailable(MainActivity.this)) {

            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                    Log.d("stt", "onReadyForSpeech");
                }

                @Override
                public void onBeginningOfSpeech() {

                    Log.d("stt", "onBeginningOfSpeech");
                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                    Log.d("stt", "onEndOfSpeech");
                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle bundle) {

                    Log.d("stt", "a");
                    ArrayList<String> results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    processSpeechResult(results.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void processSpeechResult(String result) {

        Log.d("stt", result);
    }

    private void initializeTextToSpeech() {

        tts = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == tts.SUCCESS) {
                    result = tts.setLanguage(new Locale("pt", "POR"));
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(getApplicationContext(), "Dispositivo não suporta idioma.", Toast.LENGTH_SHORT).show();
                        Log.d("tts", "Dispositivo não suporta idioma.");
                    } else {
                        showMainView();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Erro ao inicializar tts.", Toast.LENGTH_SHORT).show();
                    Log.d("tts", "Erro ao inicializar tts.");
                }
            }
        });
    }

    public void tts(String text) {

        Log.d("tts", text);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void floatingActionButtonVisibility(FloatingActionButton fab, boolean show) {
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        FloatingActionButton.Behavior behavior =
                (FloatingActionButton.Behavior) params.getBehavior();

        if (!show) {
            if (behavior != null) {
                behavior.setAutoHideEnabled(false);
            }

            fab.hide();
        } else {
            if (behavior != null) {
                behavior.setAutoHideEnabled(true);
            }

            fab.show();
        }

    }

    public void setDestinationThing(Thing thingDestination) {
        this.thingDestination = thingDestination;
        this.destinationText.setText(thingDestination.getName().getText());
    }

    private void getEdges() {

        if (discoveringNeighbours && thingOrigin != null) {
            ThingController.discoverNeighbours(thingOrigin, NEIGHBOURS_DEPTH, MainActivity.this);
            hideKeyboard();
            return;
        }

        if (thingOrigin != null && thingDestination != null) {
            User user = new User();
            user.setIdUser(1);

            ThingController.guideToThing(thingOrigin, thingDestination, user, MainActivity.this);

            hideKeyboard();
            showGuideRouteView();
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputManager =
                (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showDirections(final ArrayList<Edge> edgeList) {

        tempEdgeList = edgeList;
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

        reachedTheGoal();
    }

    public void fillList(ArrayList<Thing> thingList) {

        this.thingList = thingList;

        List<RowData> rowData = new ArrayList<>();
        for (Thing thing : thingList) {
            RowData data = new RowData();
            data.setTitle(thing.getName().getText());
            data.setSubtitle(thing.getLocality().getName().getText());
            rowData.add(data);
        }

        final ListAdapter adapter = new ListAdapter(this, rowData);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                resultList.setAdapter(adapter);
                graphView.setVisibility(View.GONE);
                resultList.setVisibility(View.VISIBLE);
            }
        });
    }

    public void showDetailsView(final Thing thing) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                originText.setVisibility(View.GONE);
                destinationText.setVisibility(View.GONE);
                graphView.setVisibility(View.GONE);
                resultList.setVisibility(View.GONE);
                floatingActionButtonVisibility(qrcodeFab, false);

                for (TextView detailTextView : details) {
                    detailTextView.setVisibility(View.VISIBLE);
                }

                details[DETAILS_NAME].setText(thing.getName().getText());
                details[DETAILS_DESCRIPTION].setText(thing.getDescription().getText());
                details[DETAILS_CLASS].setText(thing.getConcept().getText());
                details[DETAILS_LOCALITY].setText(thing.getLocality().getName().getText());
                details[DETAILS_ALERT].setText(thing.getAlert().getText());
                details[DETAILS_DISPLAY].setText(thing.getDisplay().getText());
                details[DETAILS_MESSAGE].setText(thing.getMessage().getText());
                details[DETAILS_SITUATION].setText(thing.getSituation().getText());
                details[DETAILS_RESPONSIBLE_NAME].setText(thing.getResponsible().getName().getText());
                details[DETAILS_RESPONSIBLE_PHONE].setText(thing.getResponsible().getPhone().getText());
                details[DETAILS_RESPONSIBLE_EMAIL].setText(thing.getResponsible().getEmail().getText());
            }
        });
    }

    private void showMainView() {

        currentView = MAIN_VIEW;
        viewStack.push(currentView);

        originText.setVisibility(View.VISIBLE);
        destinationText.setVisibility(View.GONE);
        graphView.setVisibility(View.VISIBLE);
        resultList.setVisibility(View.GONE);
        floatingActionButtonVisibility(qrcodeFab, true);
        floatingActionButtonVisibility(centralizeFab, true);
        floatingActionButtonVisibility(routeFab, true);
        discoveringNeighbours = true;
        discoverThing = true;
        coordinatesLayout.setVisibility(View.GONE);

        getEdges();

        setDetailsGone();
        informingRoute = false;

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        speechRecognizer.startListening(intent);

        Log.d("stt", "" + speechRecognizer.isRecognitionAvailable(this));

        tts(mainCommands);
    }

    private void showFindRouteView() {

        currentView = FIND_ROUTE_VIEW;
        viewStack.push(currentView);

        originText.setVisibility(View.VISIBLE);
        originText.setHint("Origem");
        destinationText.setVisibility(View.VISIBLE);
        graphView.setVisibility(View.GONE);
        resultList.setVisibility(View.VISIBLE);
        floatingActionButtonVisibility(qrcodeFab, false);
        floatingActionButtonVisibility(centralizeFab, false);
        floatingActionButtonVisibility(routeFab, false);
        discoveringNeighbours = false;
        coordinatesLayout.setVisibility(View.GONE);

        setDetailsGone();
        informingRoute = false;

        tts(routeCommands);
    }

    private void showGuideRouteView() {

        currentView = GUIDE_ROUTE_VIEW;
        viewStack.push(currentView);

        originText.setVisibility(View.VISIBLE);
        destinationText.setVisibility(View.VISIBLE);
        graphView.setVisibility(View.VISIBLE);
        resultList.setVisibility(View.GONE);
        floatingActionButtonVisibility(qrcodeFab, true);
        floatingActionButtonVisibility(centralizeFab, true);
        floatingActionButtonVisibility(routeFab, false);
        discoverThing = false;
        coordinatesLayout.setVisibility(View.VISIBLE);

        setDetailsGone();
        informingRoute = true;
    }

    private void setDetailsGone() {
        for (TextView detailTextView : details) {
            detailTextView.setText("");
            detailTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

        int i = viewStack.pop();
        Log.d("msg", "" + i);
        switch (i) {
            case 0:
                super.onBackPressed();
                break;
            case 1:
            case 2:
                showMainView();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("msg", "" + resultCode);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            Log.d("msg", "leuQrCode");
            if (discoverThing) {
                Log.d("msg", "discoverThing");
                if (result.getContents() != null) {
                    ThingController.discoverThingFromID(Integer.parseInt(result.getContents()), MainActivity.this);
                }
            } else {
                DirectionController.showDirectionFromQRCode(Integer.parseInt(result.getContents()), user, MainActivity.this);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static void reachedTheGoal() {
        coordinatesText.setText("Você chegou ao objetivo!");
    }

    public void updateNextDirection(Edge nextEdge) {
        degreesToGo = nextEdge.getDegree();
        metersToGo = nextEdge.getDistance();
        graphView.updateNextDirection(nextEdge);
    }

    private void invertOriginDestination() {
        Thing thingTemp = thingOrigin;
        thingOrigin = thingDestination;
        thingDestination = thingTemp;

        originText.setText(thingOrigin.getName().getText());
        destinationText.setText(thingDestination.getName().getText());

        getEdges();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent preferencesIntent = new Intent(this, SettingsActivity.class);
            startActivity(preferencesIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static SharedPreferences getPreferences() {
        return preferences;
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

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (informingRoute) {
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
                    Log.d("edasd", "ue");
                    coordinates = newCoordinates;
                    coordinatesText.setText(coordinates);
                    tts(coordinates);
                }
            }
        }
    }

    private String discoverDirection(float degreesToGo, float actualDegree) {

        float degree = (degreesToGo - actualDegree) - 30;

        if (degree < 10 || degree > 350)
            return "Caminhe " + metersToGo + " metros";
        if (degree < 45)
            return "Vire levemente para a direita";
        if (degree < 90)
            return "Vire para a direita";
        if (degree < 270)
            return "Dê meia volta";
        if (degree < 315)
            return "Vire para a esquerda";
        return "Vire levemente para a esquerda";
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }
}