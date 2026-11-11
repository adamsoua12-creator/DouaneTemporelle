
package com.example.douanetemporelle;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // رقم بطاقة الطالب
    public static final String MATRICULE = "4581";

    // إنشاء مفاتيح Intent باستعمال رقم بطاقة الطالب
    public static String construireCle(String matricule, String nomCle) {
        String quatreChiffres = matricule.substring(matricule.length() - 4);
        return quatreChiffres + "_" + nomCle;
    }

    EditText edtNom, edtNaissance, edtDestination;
    Spinner spnMotif;
    Button btnInspecter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ربط العناصر مع الواجهة
        edtNom = findViewById(R.id.edt_nom_as);
        edtNaissance = findViewById(R.id.edt_naissance_as);
        edtDestination = findViewById(R.id.edt_destination_as);
        spnMotif = findViewById(R.id.spn_motif_as);
        btnInspecter = findViewById(R.id.btn_inspecter_as);

        // إعداد قائمة أسباب السفر
        String[] motifs = {
                "Tourisme",
                "Modification du passé",
                "Fuite"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                motifs
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spnMotif.setAdapter(adapter);

        // عند الضغط على زر الفحص
        btnInspecter.setOnClickListener(v -> {

            String nom = edtNom.getText().toString().trim();
            String naissance = edtNaissance.getText().toString().trim();
            String destination = edtDestination.getText().toString().trim();
            String motif = spnMotif.getSelectedItem().toString();

            // التحقق من المعلومات
            if (nom.isEmpty() || naissance.isEmpty() || destination.isEmpty()) {
                Toast.makeText(this,
                        "Veuillez remplir tous les champs",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            int anneeNaissance;
            int anneeDestination;

            try {
                anneeNaissance = Integer.parseInt(naissance);
                anneeDestination = Integer.parseInt(destination);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Année invalide", Toast.LENGTH_SHORT).show();
                return;
            }

            // التحقق من صحة السنوات
            if (anneeNaissance <= 0 || anneeNaissance > 9999 ||
                    anneeDestination <= 0 || anneeDestination > 9999) {

                Toast.makeText(this,
                        "Les années doivent être comprises entre 1 et 9999",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // حساب العمر الزمني
            int ageTemporel = anneeDestination - anneeNaissance;

            // الانتقال إلى شاشة التحقق
            Intent intent = new Intent(MainActivity.this,
                    VerificationActivity.class);

            // إرسال المعلومات باستعمال المفاتيح الديناميكية
            intent.putExtra(construireCle(MATRICULE, "NOM_VOYAGEUR"), nom);
            intent.putExtra(construireCle(MATRICULE, "ANNEE_NAISSANCE"), anneeNaissance);
            intent.putExtra(construireCle(MATRICULE, "ANNEE_DESTINATION"), anneeDestination);
            intent.putExtra(construireCle(MATRICULE, "MOTIF"), motif);
            intent.putExtra(construireCle(MATRICULE, "AGE_TEMPOREL"), ageTemporel);

            startActivity(intent);
        });
    }
}
