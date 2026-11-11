package com.example.douanetemporelle;

import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class VerificationActivity extends AppCompatActivity {

    TextView txtNom, txtAge, txtMotif;
    Button btnApprouver, btnRefuser;

    // التحقق إذا كان العدد أوليا
    private boolean estPremier(int nombre) {
        if (nombre < 2) return false;

        for (int i = 2; i <= Math.sqrt(nombre); i++) {
            if (nombre % i == 0) {
                return false;
            }
        }
        return true;
    }

    // إرسال نتيجة المراقبة
    private void envoyerResultat(boolean verdict, String nom) {
        Intent intent = new Intent(
                VerificationActivity.this,
                ResultActivity.class
        );

        intent.putExtra(
                MainActivity.construireCle(MainActivity.MATRICULE, "VERDICT"),
                verdict
        );

        intent.putExtra(
                MainActivity.construireCle(MainActivity.MATRICULE, "NOM_VOYAGEUR"),
                nom
        );

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        // ربط العناصر بالواجهة
        txtNom = findViewById(R.id.txt_nom_verif_as);
        txtAge = findViewById(R.id.txt_age_verif_as);
        txtMotif = findViewById(R.id.txt_motif_verif_as);

        btnApprouver = findViewById(R.id.btn_approuver_as);
        btnRefuser = findViewById(R.id.btn_refuser_as);

        // استقبال معلومات المسافر
        Intent intent = getIntent();

        String nom = intent.getStringExtra(
                MainActivity.construireCle(MainActivity.MATRICULE, "NOM_VOYAGEUR")
        );

        int anneeNaissance = intent.getIntExtra(
                MainActivity.construireCle(MainActivity.MATRICULE, "ANNEE_NAISSANCE"),
                0
        );

        int anneeDestination = intent.getIntExtra(
                MainActivity.construireCle(MainActivity.MATRICULE, "ANNEE_DESTINATION"),
                0
        );

        String motif = intent.getStringExtra(
                MainActivity.construireCle(MainActivity.MATRICULE, "MOTIF")
        );

        int ageTemporel = anneeDestination - anneeNaissance;

        // عرض معلومات المسافر
        txtNom.setText("Nom : " + nom);
        txtAge.setText("Âge temporel : " + ageTemporel + " ans");
        txtMotif.setText("Motif : " + motif);

        // منع الموافقة في حالة تعديل الماضي والفرق عدد أولي
        int difference = Math.abs(anneeDestination - anneeNaissance);

        if ("Modification du passé".equals(motif)
                && estPremier(difference)) {
            btnApprouver.setEnabled(false);
        }

        // عند الموافقة
        btnApprouver.setOnClickListener(v -> {
            envoyerResultat(true, nom);
        });

        // عند الرفض
        btnRefuser.setOnClickListener(v -> {
            envoyerResultat(false, nom);
        });
    }
}
