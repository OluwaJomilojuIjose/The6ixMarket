package com.example.management;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import android.util.Log;
import java.util.concurrent.ExecutionException;
import com.google.common.util.concurrent.ListenableFuture;
import androidx.camera.view.PreviewView;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CameraSensorActivity extends AppCompatActivity {

    private PreviewView previewView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_sensor);

        previewView = findViewById(R.id.previewView);
        startCamera();
    }

    protected void onStart() {
        super.onStart();
        checkCameraPermission();
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                // Permission denied
                Log.e("CameraX", "Camera permission denied");
            }
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Create a Preview
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // Select the back camera
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                // Bind the camera to the lifecycle
                cameraProvider.unbindAll();
                Camera camera = cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview);

            } catch (ExecutionException | InterruptedException e) {
                Log.e("CameraX", "Failed to initialize CameraX", e);
            }
        }, ContextCompat.getMainExecutor(this));
    }

}
