package com.example.drplantae;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class RemediesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedies);

        // Get the ListView reference
        ListView remediesListView = findViewById(R.id.remediesListView);

        // Create a list of remedies
        List<String> remedies = new ArrayList<>();
        remedies.add("Tomato_Healthy: No disease detected.");
        remedies.add("Tomato_Mosaic_Virus: Remove infected plants and use resistant varieties.");
        remedies.add("Tomato_Yellow_Leaf_Curl_Virus: Control whiteflies and use resistant varieties.");
        remedies.add("Tomatao_Target_Spot: Rotate crops, remove infected plant debris, and use fungicides.");
        remedies.add("Tomato_Spider_mites_Two-spotted_spidermites: Use insecticidal soap or neem oil.");
        remedies.add("Tomato_Sepatoria_Leaf_Spot: Rotate crops, remove infected plant debris, and use fungicides.");
        remedies.add("Tomato_Leaf_Mold: Improve air circulation, avoid overhead watering, and use fungicides.");
        remedies.add("Tomato_Late_Blight: Use resistant varieties, remove infected plant debris, and apply fungicides.");
        remedies.add("Tomato_Early_Blight: Rotate crops, remove infected plant debris, and use fungicides.");
        remedies.add("Tomato_Bacterial_Spot: Use resistant varieties, avoid overhead watering, and remove infected plant debris.");
        remedies.add("Strawberry_Healthy: No disease detected.");
        remedies.add("Strawberry_Leaf_scorch: Improve watering practices, fertilize, and remove infected leaves.");
        remedies.add("Squash_Powdery_Mildew: Use fungicides, remove infected plant debris, and increase air circulation.");
        remedies.add("Soybean_Healthy: No disease detected.");
        remedies.add("Raspberry_Healthy: No disease detected.");
        remedies.add("Potato_Healthy: No disease detected.");
        remedies.add("Potato_Late_Blight: Use resistant varieties, remove infected plant debris, and apply fungicides.");
        remedies.add("Potato_Early_Blight: Rotate crops, remove infected plant debris, and use fungicides.");
        remedies.add("Pepper,Bell_Healthy: No disease detected.");
        remedies.add("Pepper,Bell_Bacterial_spot: Use resistant varieties, avoid overhead watering, and remove infected plant debris.");
        remedies.add("Peach_Healthy: No disease detected.");
        remedies.add("Peach_Bacterial_spot: Use resistant varieties, avoid overhead watering, and remove infected plant debris.");
        remedies.add("Orange_Haunglongbing_(Citrus_greening): No cure available. Remove infected trees.");
        remedies.add("Grape_Healthy: No disease detected.");
        remedies.add("Grape_Leaf_Blight_(Isaporis_Leaf_Spot): Use fungicides and remove infected leaves.");
        remedies.add("Grape_Esca_(Black_Measles): No cure available. Prune out infected canes.");
        remedies.add("Grape_Black_Rot: Use fungicides and remove infected berries.");
        remedies.add("Corn_(maize)_Healthy: No disease detected.");
        remedies.add("Corn_(maize)_Northern_Leaf_Blight: Use resistant varieties and remove infected plant debris.");
        remedies.add("Corn_(maize)_Common_rust: Use resistant varieties and remove infected plant debris.");
        remedies.add("Corn_(maize)_Cercospora_leaf_spot_Gray_Leaf_Spot: Use resistant varieties and remove infected plant debris.");
        remedies.add("Cherry_(including_sour)_healthy: No disease detected.");
        remedies.add("Cherry_(including_sour)_Powdery_mildew: Use fungicides and remove infected leaves.");
        remedies.add("Blueberry_healthy: No disease detected.");
        remedies.add("Apple_Healthy: No disease detected.");
        remedies.add("Apple_Cedar_apple_rust: Remove cedar trees in the vicinity and use fungicides.");
        remedies.add("Apple_Black_rot: Use fungicides and remove infected fruit.");
        remedies.add("Apple_Scab: Use fungicides and remove infected leaves and fruit.");

        // Create an ArrayAdapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, remedies);

        // Set the adapter to the ListView
        remediesListView.setAdapter(adapter);
    }
}
