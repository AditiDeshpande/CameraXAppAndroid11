package com.example.cameraxapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.camera.core.Camera
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.core.app.ActivityCompat
import java.io.File
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private lateinit var cameraCaptureButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraCaptureButton = findViewById<Button>(R.id.camera_capture_button)
        //Request camera permissions
        if(allPermissionsGranted()) {
            startCamera()
        }
        else{
            ActivityCompat.requestPermissions(
                this ,REQUIRED_PERMISSIONS , REQUEST_CODE_PERMISSIONS
            )
        }

        //Setup the listener for take photo button
        cameraCaptureButton.setOnClickListener {  takePhoto()}
                                                                                                                                                                                                                                                                                                                         

        outputDirectory = getOutputDirectory()
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
        cameraExecutor = Executors.newSingleThreadExecutor()

    }



    private fun startCamera(){
        //TODO
    }

    private fun takePhoto(){
        //TODO
    }

    private fun allPermissionsGranted() = false

    fun getOutputDirectory(): File{
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it , resources.getString(R.string.app_name))
                    .apply { mkdirs() } }

        return if(mediaDir != null && mediaDir.exists())
            mediaDir
        else
            filesDir
    }

    companion object{
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss" +
                "-SSS"
        private const val REQUEST_CODE_PERMISSIONS =10
        private val REQUIRED_PERMISSIONS =
                arrayOf(android.Manifest.permission.CAMERA)
    }
}