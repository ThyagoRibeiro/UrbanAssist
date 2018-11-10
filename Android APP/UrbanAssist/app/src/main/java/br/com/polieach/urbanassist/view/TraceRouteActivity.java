package br.com.polieach.urbanassist.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    ArrayList<Thing> thingList = new ArrayList<>();
    Intent intent;

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

        intent = getIntent();

        origin_editText = findViewById(R.id.origin_text);
        origin_editText.setText(intent.getStringExtra("origin"));
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

        destination_editText = findViewById(R.id.destination_text);
        destination_editText.setText(intent.getStringExtra("destination"));
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

                Intent resultIntent = new Intent();
                resultIntent.putExtra("choice", selectedThing);
                setResult(RESULT_OK, resultIntent);
                finish();

            }
        });

        if (intent.getExtras().getInt("requestCode") == 0)
            origin_editText.requestFocus();
        else
            destination_editText.requestFocus();

    }

    @Override
    protected void recognizeVoiceCommand(ArrayList<String> matches) {
        super.recognizeVoiceCommand(matches);

        if (origin_editText.hasFocus() || destination_editText.hasFocus()) {

            if (matches.contains(getResources().getString(R.string.cancelCommand).replace("; ", ""))) {

                origin_editText.setFocusable(false);
                destination_editText.setFocusable(false);
                results_listView.setAdapter(null);

                String speech = getResources().getString(R.string.traceRouteActivity);
                speech += getResources().getString(R.string.afterSignalOptionsInformation);
                speech += getResources().getString(R.string.selectOriginCommand);
                speech += getResources().getString(R.string.selectDestinationCommand);
                speech += getResources().getString(R.string.preferencesCommand);
                speech += getResources().getString(R.string.returnCommand);

                speechAndWaitCommand(speech);
            } else {
                //setText
                EditText editTextInFocus = (origin_editText.hasFocus() ? origin_editText : destination_editText);
                editTextInFocus.setText(matches.get(0));
            }

        } else {

            String speech = getResources().getString(R.string.afterSignalInformation);

            // select origin
            if (matches.contains(getResources().getString(R.string.selectOriginCommand).replace("; ", ""))) {
                origin_editText.requestFocus();
                speech += getResources().getString(R.string.originNameCommand);

            } else if (matches.contains(getResources().getString(R.string.selectDestinationCommand).replace("; ", ""))) {
                // select destination
                destination_editText.requestFocus();
                speech += getResources().getString(R.string.destinationNameCommand);
            }

            speech += getResources().getString(R.string.cancelCommand);
            speechAndWaitCommand(speech);

        }
    }
}
