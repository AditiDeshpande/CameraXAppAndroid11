package com.example.cameraxapp

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
    private lateinit var viewFinder: PreviewView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraCaptureButton = findViewById<Button>(R.id.camera_capture_button)
        viewFinder = findViewById<PreviewView>(R.id.viewFinder)

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
        /*
        Create an instance of the ProcessCameraProvider. This is used to bind the
        camera lifecyle to owner's lifecyle.
        This allows you not to worry about opening and closing the camera
        since CameraX is lifecyle aware.
         */
        val cameraProviderFuture
                = ProcessCameraProvider.getInstance(this)

        /*

         */
        cameraProviderFuture.addListener(Runnable {
            //Used to bind the lifecyle of cameras to the lifecycle
            //owner within the application's process
            val cameraProvider: ProcessCameraProvider
            =cameraProviderFuture.get()

            //Initialize your Preview object
            preview = Preview.Builder().build()

            //Select back camera
            val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
            try{
                //Unbind use cases before rebinding
                cameraProvider.unbindAll()

                //Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                        this , cameraSelector ,
                        preview)
                preview?.setSurfaceProvider(viewFinder
                        .createSurfaceProvider(
                          camera?.cameraInfo))
            }catch (exc: Exception) {
                /*
                There are few ways this code could fail, like if the app is no
                longer in focus. Wrap this code in catch block to log if there's
                failure
                 */
                Log.e(TAG, "Use case binding failed", exc)
            }
        } , ContextCompat.getMainExecutor(this))
        /*Above getMainExecuttor is the second param to Runnable in addListener
        This returns an Executor that runs on the main thread.
         */

    }

    private fun takePhoto(){
        //TODO
    }

    private fun allPermissionsGranted()
            = Companion.REQUIRED_PERMISSIONS.all{
        ContextCompat.checkSelfPermission(
                baseContext , it
        ) == PackageManager.PERMISSION_GRANTED
    }

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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        //Check if the request code is correct
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if(allPermissionsGranted()){
                startCamera()
            }else{
                Toast.makeText(this ,
                "Permissions not granted by the user" ,
                Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}