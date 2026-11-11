
package com.example.douanetemporelle;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView txtResultat;
    Button btnNouveau;
    LinearLayout layoutResultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // ربط العناصر بالواجهة
        txtResultat = findViewById(R.id.txt_resultat_as);
        btnNouveau = findViewById(R.id.btn_nouveau_as);
        layoutResultat = findViewById(R.id.layout_resultat_as);

        // استقبال نتيجة المراقبة
        Intent intent = getIntent();

        boolean verdict = intent.getBooleanExtra(
                MainActivity.construireCle(MainActivity.MATRICULE, "VERDICT"),
                false
        );

        String nom = intent.getStringExtra(
                MainActivity.construireCle(MainActivity.MATRICULE, "NOM_VOYAGEUR")
        );

        if (nom == null) {
            nom = "Voyageur";
        }

        // عرض النتيجة وتغيير لون الشاشة
        if (verdict) {
            layoutResultat.setBackgroundColor(Color.rgb(22, 138, 85));
            txtResultat.setText("Bon voyage " + nom + " !");
        } else {
            layoutResultat.setBackgroundColor(Color.rgb(192, 57, 43));
            txtResultat.setText(nom + " est en état d'arrestation temporelle");
        }

        // العودة إلى المسافر الجديد وإغلاق الشاشات السابقة
        btnNouveau.setOnClickListener(v -> {
            Intent retour = new Intent(ResultActivity.this, MainActivity.class);
            retour.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(retour);
            finish();
        });
    }
}
