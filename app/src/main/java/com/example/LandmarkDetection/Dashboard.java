package com.example.LandmarkDetection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmark;
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmarkDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    Button startCamera;
    TextView resultText;
    static final String FAILED = "This landmark is not in our database.";
    static final String WORKED = "Found! Look above to view your landmark.";
    static final int CAMERA_PER = 31245;
    static final int CAMERA_CODE = 9876;
    static final String TAG = "DHARMIK";

    ListView listView;
    LocationAdapter locationAdapter;
    ArrayList<Landmark> landmarks = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        startCamera = findViewById(R.id.id_StartCamera);
        resultText = findViewById(R.id.id_resultText);
        listView = findViewById(R.id.id_listview);
        landmarks = (ArrayList<Landmark>) getIntent().getSerializableExtra("list");

        startCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToLaunchCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentToLaunchCamera, CAMERA_CODE);
            }
        });

        locationAdapter = new LocationAdapter(this, R.layout.adapter_location, landmarks);
        listView.setAdapter(locationAdapter);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PER);
        }
    }
    public class LocationAdapter extends ArrayAdapter<Landmark> {
        Context parentContext;
        List<Landmark> list;
        int xmlRes;

        public LocationAdapter(@NonNull Context context, int resource, @NonNull List<Landmark> objects) {
            super(context, resource, objects);
            parentContext = context;
            list = objects;
            xmlRes = resource;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)parentContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View adapterView = layoutInflater.inflate(xmlRes,null);
            final TextView landmarkName = adapterView.findViewById(R.id.id_adapter_textView);
            final ImageView imageView = adapterView.findViewById(R.id.id_adapter_image);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intentToLaunch = new Intent(Dashboard.this, Specific.class);
                    intentToLaunch.putExtra("specificLandmark",landmarks.get(position));
                    startActivity(intentToLaunch);

                }
            });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        tryToSetUp(landmarkName, imageView, position, list);
                    }catch (Exception e){
                        Log.d(TAG, "Need More Time");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    tryToSetUp(landmarkName, imageView, position, list);
                                }catch (Exception e){
                                    Log.d(TAG, "Need More Time");
                                }
                            }
                        },500);
                    }
                }
            },500);
            return adapterView;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CODE && resultCode == RESULT_OK){
            Bitmap rawImage = (Bitmap)data.getExtras().get("data");
            labelImage(rawImage);
        }
    }
    public void labelImage(Bitmap rawImage){
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(rawImage);
        FirebaseVisionCloudDetectorOptions options = new FirebaseVisionCloudDetectorOptions.Builder().setMaxResults(1).build();
        FirebaseVisionCloudLandmarkDetector detector = FirebaseVision.getInstance().getVisionCloudLandmarkDetector(options);
        Task<List<FirebaseVisionCloudLandmark>> result = detector.detectInImage(image)
                .addOnCompleteListener(new OnCompleteListener<List<FirebaseVisionCloudLandmark>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<FirebaseVisionCloudLandmark>> task) {
                        if (task.isSuccessful()) {
                            List<FirebaseVisionCloudLandmark> landmarkS = task.getResult();
                            if(landmarkS.size() == 0){
                                resultText.setText(FAILED);
                            }
                            else {
                                for (FirebaseVisionCloudLandmark landmark : landmarkS) {
                                    String landmarkName = landmark.getLandmark();
                                    landmarks.add(0, new Landmark(landmarkName));
                                    locationAdapter.notifyDataSetChanged();
                                    resultText.setText(WORKED);
                                    Log.d(TAG, "Firebase The landmark: " + landmarkName);
                                }
                            }
                        } else {
                            resultText.setText(FAILED);
                        }
                    }
                });
    }
    public void tryToSetUp(TextView landmarkName, ImageView imageView, int position, List<Landmark> list) throws Exception{
        Picasso.get()
                .load(list.get(position).getURLsAtPos(0))
                .into(imageView);
        landmarkName.setText(list.get(position).getLandmarkName());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PER: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                return;
            }
        }
    }
}
