package br.com.polieach.urbanassist.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.polieach.urbanassist.R;
import br.com.polieach.urbanassist.model.Thing;

public class POIDetailsActivity extends SpeechActivity {

    ListView results_listView;
    Thing pointOfInterest;
    FloatingActionButton call_fab, sendEmail_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_poi_details);
        initializeComponents();

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void mountInitialSpeech() {

        initialSpeech = getResources().getString(R.string.poiDetailsActivity);
        initialSpeech += getResources().getString(R.string.afterSignalOptionsInformation);
        initialSpeech += getResources().getString(R.string.preferencesCommand);
        initialSpeech += getResources().getString(R.string.returnCommand);
    }

    @Override
    protected void initializeComponents() {

        pointOfInterest = (Thing) getIntent().getExtras().getSerializable("pointOfInterest");
        results_listView = findViewById(R.id.results_listView);
        fillPOIDetailsList();
    }

    protected void recognizeVoiceCommand(ArrayList<String> matches) {
        super.recognizeVoiceCommand(matches);

        if (false) {

            // call responsible
            if (false) {
                call_fab.performClick();
            } else if (false) {
                sendEmail_fab.performClick();
            }
        }
    }


    private void fillPOIDetailsList() {

        String name = getResources().getString(R.string.name);
        String description = getResources().getString(R.string.description);
        String locality = getResources().getString(R.string.locality);
        String concept = getResources().getString(R.string.concept);
        String situation = getResources().getString(R.string.situation);
        String alert = getResources().getString(R.string.alert);
        String display = getResources().getString(R.string.display);
        String message = getResources().getString(R.string.message);
        String responsibleName = getResources().getString(R.string.responsibleName);
        String responsibleEmail = getResources().getString(R.string.responsibleEmail);
        String responsiblePhone = getResources().getString(R.string.responsiblePhone);

        List<RowData> rowData = new ArrayList<>();

        rowData.add(new RowData(name, pointOfInterest.getName().getText()));
        rowData.add(new RowData(description, pointOfInterest.getDescription().getText()));
        rowData.add(new RowData(locality, pointOfInterest.getLocality().getName().getText()));
        rowData.add(new RowData(concept, pointOfInterest.getConcept().getText()));
        rowData.add(new RowData(situation, pointOfInterest.getSituation().getText()));
        rowData.add(new RowData(alert, pointOfInterest.getAlert().getText()));
        rowData.add(new RowData(display, pointOfInterest.getDisplay().getText()));
        rowData.add(new RowData(message, pointOfInterest.getMessage().getText()));
        rowData.add(new RowData(responsibleName, pointOfInterest.getResponsible().getName().getText()));
        rowData.add(new RowData(responsibleEmail, pointOfInterest.getResponsible().getEmail().getText()));
        rowData.add(new RowData(responsiblePhone, pointOfInterest.getResponsible().getPhone().getText()));

        final ListAdapter adapter = new ListAdapter(this, rowData);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                results_listView.setAdapter(adapter);
            }
        });
    }
}
