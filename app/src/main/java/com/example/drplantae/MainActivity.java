package com.example.drplantae;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    TextView result, confidence;
    ImageView imageView;
    Button picture, upload, remediesButton;
    int imageSize = 224;
    Interpreter tflite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);
        confidence = findViewById(R.id.confidence);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);
        upload = findViewById(R.id.button3);
        remediesButton = findViewById(R.id.remediesButton);

        // Load the model when the activity is created
        loadModel();

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    // Request camera permission if we don't have it.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch gallery to pick an image
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);
            }
        });

        // Add this part to launch RemediesActivity
        remediesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch RemediesActivity when the Remedies button is clicked
                Intent remediesIntent = new Intent(MainActivity.this, RemediesActivity.class);
                startActivity(remediesIntent);
            }
        });
    }

    private void loadModel() {
        try {
            // Load the model using AssetManager
            AssetManager assetManager = getApplicationContext().getAssets();
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd("PlantDisease.tflite");
            FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
            FileChannel fileChannel = fileInputStream.getChannel();

            // Map the model into memory
            ByteBuffer modelBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, assetFileDescriptor.getStartOffset(), assetFileDescriptor.getDeclaredLength());

            // Instantiate the Interpreter
            tflite = new Interpreter(modelBuffer);
        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
        }
    }

    public void classifyImage(Bitmap image) {
        try {
            // Creates inputs for reference.
            // Adjust the buffer size based on the expected input size
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());


            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            // Runs model inference and gets result.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            inputFeature0.loadBuffer(byteBuffer);

            // Perform inference
            float[][] output = new float[1][getNumLabels()];
            tflite.run(inputFeature0.getBuffer(), output);

            // Do something with the result and confidence values
            updateUI(output);

        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
        }
    }


    private void updateUI(float[][] output) {
        int maxClassIndex = argmax(output[0]);
        float maxConfidence = output[0][maxClassIndex];

        // Update the UI with the classification result
        String classifiedAs = getClassLabel(maxClassIndex);
        result.setText("Classified as: " + classifiedAs);
        confidence.setText("Confidence: " + maxConfidence);
    }

    // Find the index of the maximum value in the array
    private int argmax(float[] array) {
        int maxIndex = 0;
        float maxVal = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] > maxVal) {
                maxVal = array[i];
                maxIndex = i;
            }
        }

        return maxIndex;
    }

    // Map class index to label based on your class labels
    private String getClassLabel(int classIndex) {
        String[] classLabels = {
                "apple apple scab", "apple black rot", "apple cedar apple rust", "apple healthy",
                "blueberry healthy", "cherry including sour powdery mildew", "cherry including sour health",
                "corn maize cercospora leaf spot gray leaf spot", "corn maize common rust",
                "corn maize northern leaf blight", "corn maize healthy", "grape black rot",
                "grape esca black measles", "grape leaf blight isariopsis leaf spot", "grape healthy",
                "orange haunglongbing citrus greening", "peach bacterial spot", "peach healthy",
                "pepper bell bacterial spot", "pepper bell healthy", "potato early blight",
                "potato late blight", "potato healthy", "raspberry healthy", "soybean healthy",
                "squash powdery mildew", "strawberry leaf scorch", "strawberry healthy",
                "tomato bacterial spot", "tomato early blight", "tomato late blight",
                "tomato leaf mold", "tomato septoria leaf spot",
                "tomato spider mites two spotted spider mite", "tomato target spot",
                "tomato tomato yellow leaf curl virus",
                "tomato tomato mosaic virus", "tomato healthy"
        };

        if (classIndex >= 0 && classIndex < classLabels.length) {
            return classLabels[classIndex];
        } else {
            return "Unknown Class";
        }
    }

    private int getNumLabels() {
        // Return the total number of classes (labels) in your model
        return 38; // Update this with the correct number of classes in your model
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                handleImage(image);
            } else if (requestCode == 2) {
                try {
                    Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    handleImage(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleImage(Bitmap image) {
        int dimension = Math.min(image.getWidth(), image.getHeight());
        image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
        imageView.setImageBitmap(image);

        image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
        classifyImage(image);
    }

    @Override
    protected void onDestroy() {
        // Close the model when the activity is destroyed to release resources
        if (tflite != null) {
            tflite.close();
        }
        super.onDestroy();
    }
}

