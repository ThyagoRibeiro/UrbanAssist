package br.com.polieach.urbanassist.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.polieach.urbanassist.R;
import br.com.polieach.urbanassist.controller.ThingController;
import br.com.polieach.urbanassist.model.Thing;

public class TraceRouteActivity extends SpeechActivity {

    boolean fromOrigin = true;
    EditText origin_editText, destination_editText;
    ListView results_listView;
    Thing origin, destination;
    ArrayList<Thing> thingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_trace_route);
        initializeComponents();

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void mountInitialSpeech() {

        initialSpeech = getResources().getString(R.string.traceRouteActivity);
        initialSpeech += getResources().getString(R.string.afterSignalOptionsInformation);
        initialSpeech += getResources().getString(R.string.selectOriginCommand);
        initialSpeech += getResources().getString(R.string.selectDestinationCommand);
        initialSpeech += getResources().getString(R.string.preferencesCommand);
        initialSpeech += getResources().getString(R.string.returnCommand);
    }

    @Override
    protected void initializeComponents() {

        origin_editText = findViewById(R.id.origin_editText);

        String hasOrigin = getIntent().getExtras().getString("hasOrigin");
        if (!hasOrigin.equals(""))
            origin_editText.setText(hasOrigin);

        origin_editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                fromOrigin = true;
                ThingController.searchThing(origin_editText.getText().toString(), results_listView, TraceRouteActivity.this, thingList);

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        destination_editText = findViewById(R.id.destination_editText);
        destination_editText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                fromOrigin = false;
                ThingController.searchThing(destination_editText.getText().toString(), results_listView, TraceRouteActivity.this, thingList);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        results_listView = findViewById(R.id.results_listView);
        results_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Thing selectedThing = thingList.get(i);

                if (fromOrigin) {
                    origin = selectedThing;
                    origin_editText.setText(selectedThing.getName().getText());
                } else {
                    destination = selectedThing;
                    destination_editText.setText(selectedThing.getName().getText());
                }

                if (origin != null && destination != null) {
                    Intent intent = new Intent(TraceRouteActivity.this, DirectionsActivity.class);
                    intent.putExtra("origin", origin);
                    intent.putExtra("destination", destination);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }

                results_listView.setAdapter(null);

            }
        });
    }

    @Override
    protected void recognizeVoiceCommand(ArrayList<String> matches) {
        super.recognizeVoiceCommand(matches);

        if (origin_editText.hasFocus() || destination_editText.hasFocus()) {

            // cancel
            if (false) {

                origin_editText.setFocusable(false);
                destination_editText.setFocusable(false);
                results_listView.setAdapter(null);

            } else {

                //setText
                EditText editTextInFocus = (origin_editText.hasFocus() ? origin_editText : destination_editText);
                editTextInFocus.setText("");

            }

        } else {

            String speech = getResources().getString(R.string.afterSignalInformation);

            // select origin
            if (false) {
                origin_editText.performClick();
                speech += (R.string.originNameCommand);

            } else if (false) {
                // select destination
                destination_editText.performClick();
                speech += (R.string.destinationNameCommand);
            }

            speech += (R.string.cancelCommand);

            speechAndWaitCommand(speech);

        }
    }
}
