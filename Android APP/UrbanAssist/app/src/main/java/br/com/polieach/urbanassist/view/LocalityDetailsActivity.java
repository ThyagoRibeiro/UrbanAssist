package br.com.polieach.urbanassist.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.polieach.urbanassist.R;
import br.com.polieach.urbanassist.model.Locality;

public class LocalityDetailsActivity extends SpeechActivity {

    ListView results_listView;
    Locality locality;
    FloatingActionButton call_fab, sendEmail_fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_locality_details);
        initializeComponents();

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initializeComponents() {

        locality = (Locality) getIntent().getExtras().getSerializable("locality");
        results_listView = findViewById(R.id.results_listView);
        fillPOIDetailsList();
    }

    @Override
    protected void mountInitialSpeech() {

        initialSpeech = getResources().getString(R.string.localityDetailsActivity);
        initialSpeech += getResources().getString(R.string.afterSignalOptionsInformation);
        initialSpeech += getResources().getString(R.string.preferencesCommand);
        initialSpeech += getResources().getString(R.string.returnCommand);
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
        String responsibleName = getResources().getString(R.string.responsibleName);
        String responsibleEmail = getResources().getString(R.string.responsibleEmail);
        String responsiblePhone = getResources().getString(R.string.responsiblePhone);

        List<RowData> rowData = new ArrayList<>();

        rowData.add(new RowData(name, locality.getName().getText()));
        rowData.add(new RowData(description, locality.getDescription().getText()));
        rowData.add(new RowData(responsibleName, locality.getResponsible().getName().getText()));
        rowData.add(new RowData(responsibleEmail, locality.getResponsible().getEmail().getText()));
        rowData.add(new RowData(responsiblePhone, locality.getResponsible().getPhone().getText()));

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