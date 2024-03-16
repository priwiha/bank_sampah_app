package com.example.bank_sampah.activity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.bank_sampah.R;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRScannerActivity2 extends AppCompatActivity {
    public static final int REQUEST_CAMERA_PERMISSION = 101;
    private static final int REQUEST_IMAGE_UPLOAD = 102;

    private SurfaceView cameraPreview;
    private Button scanQRButton;
    private Button uploadImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner2);

        cameraPreview = findViewById(R.id.camera_preview);
        scanQRButton = findViewById(R.id.scan_qr_button);
        uploadImageButton = findViewById(R.id.upload_image_button);

        // Permission untuk akses kamera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }

        // Tombol untuk memindai QR Code
        scanQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Jalankan aktivitas pemindai QR code
                Intent intent = new Intent(QRScannerActivity2.this, ScanQRActivity.class);
                startActivity(intent);
            }
        });

        // Tombol untuk mengunggah gambar
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Buka galeri untuk memilih gambar
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_UPLOAD);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_UPLOAD) {
                // Memuat gambar yang dipilih dari penyimpanan perangkat
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    decodeQRCode(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void decodeQRCode(Bitmap bitmap) {
        try {
            // Decode gambar QR code menggunakan MultiFormatReader
            MultiFormatReader reader = new MultiFormatReader();

            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            hints.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);

            reader.setHints(hints);

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

            // Ubah array piksel menjadi BinaryBitmap
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new RGBLuminanceSource(width, height, pixels)));

            // Decode BinaryBitmap menjadi hasil QR code
            Result result = reader.decode(binaryBitmap);

            // Tampilkan hasil QR code
            String qrText = result.getText();
            Toast.makeText(this, "QR Code Value: " + qrText, Toast.LENGTH_LONG).show();
        } catch (ReaderException e) {
            // QR code tidak dapat dikenali
            Toast.makeText(this, "QR Code tidak ditemukan atau tidak dapat dikenali", Toast.LENGTH_LONG).show();
        }
    }



}