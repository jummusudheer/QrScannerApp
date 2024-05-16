package com.example.qrscanner.food
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.qrscanner.databinding.ActivityMainBinding
import com.google.zxing.Result
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import me.dm7.barcodescanner.zxing.ZXingScannerView
import com.example.qrscanner.R


class MainActivity : AppCompatActivity(),  ZXingScannerView.ResultHandler {
    private lateinit var scannerView: ZXingScannerView
    private lateinit var resultTextView: TextView
private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // resultTextView = findViewById(R.id.resultTextView)

        // Initialize the scanner view
        scannerView = ZXingScannerView(this)
        scannerView.setAutoFocus(true)
        scannerView.setResultHandler(this)
        binding.qrScanner.setOnClickListener{
            // Check camera permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST)
            } else {
                startScanner()
            }

        }

    }


    override fun onPause() {
        super.onPause()
//        scannerView.stopCamera()
    }

    override fun onResume() {
        super.onResume()
//        startScanner()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanner()
            } else {
                showPermissionDeniedDialog()
            }
        }
    }
    private fun startScanner() {
        val intentIntegrator = IntentIntegrator(this)
        intentIntegrator.setPrompt("Scan a barcode or QR Code")
        intentIntegrator.setOrientationLocked(true)
        intentIntegrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null) {
            Log.e("TAG", "onActivityResult: "+intentResult.contents)
            var product = intentResult.contents
            if (product.contains("shampoo") || product.contains("detergent")) {
               binding.resultTextView.text = "No food product found"
            } else {
                showProductDialog(product)
               binding.resultTextView.text = ""
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    override fun handleResult(rawResult: Result?) {
        System.out.println("result:$rawResult")
//        rawResult?.let {
//            val product = getProductFromQRCode(it.text)
//            if (product != null) {
//                showProductDialog(product)
//            } else {
//                resultTextView.text = "No food product found"
//            }
//        }
    }

    private fun showProductDialog(product: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Food Product Found")
        dialogBuilder.setMessage("You have scanned: $product")
        dialogBuilder.setPositiveButton("OK") { dialog, _ ->
           // dialog.dismiss()
            nextPage()

////
//           val foodFragment: Fragment = FoodFragment()
//           val transaction: FragmentTransaction = fragmentManager.beginTransaction()
//           transaction.add(


        // give your fragment container id in first parameter
//
//            transaction.addToBackStack(null) // if written, this transaction will be added to backstack

//            transaction.commit()
//            startActivity(Intent(this, FoodFragment::class.java))
//                finish()
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }
    private fun nextPage(){
        val foodFragment = FoodFragment()
        val transaction=supportFragmentManager.beginTransaction()
        transaction.add(R.id.container_view, foodFragment, "FoodFragment")
            .commit()
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