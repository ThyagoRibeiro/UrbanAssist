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

public class SearchActivity extends SpeechActivity {

    EditText pointOfInterest_editText;
    ListView results_listView;
    ArrayList<Thing> thingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_search);
        initializeComponents();

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void mountInitialSpeech() {

        initialSpeech = getResources().getString(R.string.afterSignalInformation);
        initialSpeech += getResources().getString(R.string.poiNameCommand);
        initialSpeech += getResources().getString(R.string.cancelCommand);
    }

    @Override
    protected void initializeComponents() {

        pointOfInterest_editText = findViewById(R.id.pointOfInterest_editText);

        pointOfInterest_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ThingController.searchThing(pointOfInterest_editText.getText().toString(), results_listView, SearchActivity.this, thingList);
            }
        });

        String hasOrigin = getIntent().getExtras().getString("hasText");
        if (!hasOrigin.equals(""))
            pointOfInterest_editText.setText(hasOrigin);

        results_listView = findViewById(R.id.results_listView);
        results_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Thing selectedThing = thingList.get(i);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("pointOfInterest", selectedThing);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        ThingController.searchThing(pointOfInterest_editText.getText().toString(), results_listView, SearchActivity.this, thingList);
    }

    protected void recognizeVoiceCommand(ArrayList<String> matches) {
        super.recognizeVoiceCommand(matches);

        if (false) {

            pointOfInterest_editText.performClick();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
