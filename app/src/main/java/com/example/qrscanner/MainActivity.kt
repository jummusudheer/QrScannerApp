package com.example.qrscanner
import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.qrscanner.databinding.ActivityMainBinding
import com.example.qrscanner.databinding.ActivityScannerBinding
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.net.URL


class MainActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private lateinit var scannerView: ZXingScannerView
    private lateinit var resultTextView: TextView
    private lateinit var binding: ActivityMainBinding
    private lateinit var scannerBinding: ActivityScannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.qrScanner.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST)
            } else {
                setScannerView()
            }
        }


    }

    private fun setScannerView() {
        scannerBinding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(scannerBinding.root)
        scannerView = scannerBinding.scannerView
        scannerView.setAutoFocus(true)
        scannerView.setResultHandler(this)
        intializeDefaultColor()
        scannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        if (::scannerView.isInitialized) {
            scannerView.stopCamera()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::scannerView.isInitialized && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            scannerView.startCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setScannerView()
            } else {
                showPermissionDeniedDialog()
            }
        }
    }

    override fun handleResult(rawResult: Result?) {
        Log.e("TAG", "handleResult: " + rawResult?.text)
        val product = rawResult?.text ?: ""
        if(product.isNotEmpty()){
            Log.e("TAG","hadleResult:$product")
        showProductDialog(product)
        changeEdgesColor(product)
        }
        else{
            Log.e("TAG","hadleResult: No QR code detected")
            handleError("No QR code detected")
            changeEdgesColor(product)
        }

    }

    private fun handleError(errorMessage: String) {
        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()

    }

    private fun showProductDialog(product: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Result Found")
        dialogBuilder.setMessage(product)

        if (isValidUrl(product)) {
            dialogBuilder.setPositiveButton("Open Link") { dialog, _ ->
                openInChrome(product)
            }

            dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        }
        else {
            dialogBuilder.setPositiveButton("OK") { dialog, _ ->
                Toast.makeText(applicationContext, "Result: $product", Toast.LENGTH_LONG).show()
                changeEdgesColor(product)
                dialog.dismiss()
            }

            binding.resultTextView.text = ""
            setContentView(binding.root)
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun changeEdgesColor(product: String) {

        val color = if (product.isNotEmpty()) R.color.green else R.color.red
        scannerBinding.topLeftEdge.setBackgroundColor(ContextCompat.getColor(this, color))
        scannerBinding.topLeftEdgeTop.setBackgroundColor(ContextCompat.getColor(this, color))
        scannerBinding.topRightEdge.setBackgroundColor(ContextCompat.getColor(this, color))
        scannerBinding.topRightEdgeTop.setBackgroundColor(ContextCompat.getColor(this, color))
        scannerBinding.bottomLeftEdge.setBackgroundColor(ContextCompat.getColor(this, color))
        scannerBinding.bottomLeftEdgeLeft.setBackgroundColor(ContextCompat.getColor(this, color))
        scannerBinding.bottomRightEdge.setBackgroundColor(ContextCompat.getColor(this, color))
        scannerBinding.bottomRightEdgeRight.setBackgroundColor(ContextCompat.getColor(this, color))
    }

    private fun intializeDefaultColor(){
        scannerBinding.topLeftEdge.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
        scannerBinding.topLeftEdgeTop.setBackgroundColor(ContextCompat.getColor(this,  R.color.purple_200))
        scannerBinding.topRightEdge.setBackgroundColor(ContextCompat.getColor(this,  R.color.purple_200))
        scannerBinding.topRightEdgeTop.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
        scannerBinding.bottomLeftEdge.setBackgroundColor(ContextCompat.getColor(this,  R.color.purple_200))
        scannerBinding.bottomLeftEdgeLeft.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
        scannerBinding.bottomRightEdge.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
        scannerBinding.bottomRightEdgeRight.setBackgroundColor(ContextCompat.getColor(this,  R.color.purple_200))
    }

    fun isValidUrl(url: String): Boolean {
        return try {
            URL(url).toURI()
            true
        } catch (e: Exception) {
            false
        }
    }
    fun openInChrome(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                `package` = "com.android.chrome"
            }
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun showPermissionDeniedDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Camera Permission Denied")
        dialogBuilder.setMessage("Please grant camera permission to use the QR scanner.")
        dialogBuilder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            finish()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }
    companion object {
        private const val CAMERA_PERMISSION_REQUEST = 100
    }
}
