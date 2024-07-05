package com.cricketprediction.androidpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private CameraManager cameraManager;
    private String cameraId;
    private boolean isFlashOn;
    private Button btnToggleFlash;

    private Handler handler = new Handler();
    private boolean isContinuousBlinking = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnToggleFlash = findViewById(R.id.btnOnOff);
        boolean hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);


        if (!hasFlash) {
            // Device doesn't support flash
            // Handle this case
            return;
        }

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            // Get the camera ID for the flashlight
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        btnToggleFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle between continuous and rhythmic blinking patterns
                if (!isContinuousBlinking) {
                    startContinuousBlinking();
                } else {
                    stopContinuousBlinking();
                }
            }
        });
    }




    private void startContinuousBlinking() {
        // Start continuous blinking pattern
        isContinuousBlinking = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleFlash();
                handler.postDelayed(this, 500); // Adjust the interval as needed
            }
        }, 0);
    }

    private void stopContinuousBlinking() {
        // Stop continuous blinking pattern
        isContinuousBlinking = false;
        handler.removeCallbacksAndMessages(null);
        turnOffFlash();
    }

    private void toggleFlash() {
        // Toggle the flashlight state
        if (isFlashOn) {
            turnOffFlash();
        } else {
            turnOnFlash();
        }
    }

    private void turnOnFlash() {
        // Turn on the flashlight
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, true);
            }
            isFlashOn = true;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void turnOffFlash() {
        // Turn off the flashlight
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, false);
            }
            isFlashOn = false;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release resources when the activity is destroyed
        handler.removeCallbacksAndMessages(null);
    }
}