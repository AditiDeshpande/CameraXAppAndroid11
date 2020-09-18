package com.example.cameraxapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Request camera permissions
        if(allPermissionsGranted()) {
            startCamera()
        }
        else{
            ActivityCompat.requestPermissions(
                this ,REQUIRED_PERMISSIONS , REQUEST_CODE_PERMISSIONS
            )
        }
    }
}