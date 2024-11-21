package com.example.dementia.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dementia.R;

public class PillSelection extends AppCompatActivity {

    private ImageView pill1Img, pill2Img, pill3Img, pill4Img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_selection);

        // Find ImageViews
        pill1Img = findViewById(R.id.pill1Img);
        pill2Img = findViewById(R.id.pill2Img);
        pill3Img = findViewById(R.id.pill3Img);
        pill4Img = findViewById(R.id.pill4Img);

        // Set the images for each ImageView (assuming images are already set in XML)
        pill1Img.setImageResource(R.drawable.pill1);
        pill2Img.setImageResource(R.drawable.pill2);
        pill3Img.setImageResource(R.drawable.pill3);
        pill4Img.setImageResource(R.drawable.pill4);

        // Set onClickListeners to return the selected image name
        pill1Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnSelectedPill("pill1");
            }
        });

        pill2Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnSelectedPill("pill2");
            }
        });

        pill3Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnSelectedPill("pill3");
            }
        });

        pill4Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnSelectedPill("pill4");
            }
        });
    }

    // Method to return the selected pill name
    private void returnSelectedPill(String selectedPill) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selectedPill", selectedPill);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
