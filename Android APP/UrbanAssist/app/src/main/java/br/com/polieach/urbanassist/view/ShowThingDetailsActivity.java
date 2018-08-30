package br.com.polieach.urbanassist.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.com.polieach.urbanassist.R;
import br.com.polieach.urbanassist.model.Locality;
import br.com.polieach.urbanassist.model.Thing;

public class ShowThingDetailsActivity extends AppCompatActivity {

    MainActivity mainActivity;
    Thing thing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_thing_details);

        TextView nameText = findViewById(R.id.thing_name);
        TextView descriptionText = findViewById(R.id.thing_description);
        TextView classText = findViewById(R.id.thing_class);
        TextView localityText = findViewById(R.id.thing_locality);
        TextView alertText = findViewById(R.id.thing_alert);
        TextView displayText = findViewById(R.id.thing_display);
        TextView messageText = findViewById(R.id.thing_message);
        TextView situationText = findViewById(R.id.thing_situation);
        TextView responsibleNameText = findViewById(R.id.thing_responsible_name);
        TextView responsiblePhoneText = findViewById(R.id.thing_responsible_phone);
        TextView responsibleEmailText = findViewById(R.id.thing_responsible_email);

        thing = (Thing) getIntent().getSerializableExtra("thing");

        Button callButton = findViewById(R.id.thing_details_call_button);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button guideButton = findViewById(R.id.thing_details_guide_button);
        guideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.setDestinationThing(thing);
                finish();
            }
        });

        nameText.setText("Nome: " + thing.getName().getText());
        descriptionText.setText("Descrição: " + thing.getDescription().getText());
        classText.setText("Classe: " + thing.getConcept().getText());
        localityText.setText("Localização: " + thing.getLocality().getName().getText());
        alertText.setText("Alerta: " + thing.getAlert().getText());
        displayText.setText("Display: " + thing.getDisplay().getText());
        messageText.setText("Mensagem: " + thing.getMessage().getText());
        situationText.setText("Situação: " + thing.getSituation().getText());
        responsibleNameText.setText("Nome do responsável: " + thing.getResponsible().getName().getText());
        responsiblePhoneText.setText("Telefone do responsável: " + thing.getResponsible().getPhone().getText());
        responsibleEmailText.setText("E-mail do responsável: " + thing.getResponsible().getEmail().getText());

    }

}
