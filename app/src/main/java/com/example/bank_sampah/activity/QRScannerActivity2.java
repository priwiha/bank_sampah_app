package com.example.bank_sampah.activity;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.bank_sampah.R;
import com.example.bank_sampah.utility.GlobalData;
import com.example.bank_sampah.utility.network.UtilsApi;
import com.example.bank_sampah.utility.network.service.DataService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.common.HybridBinarizer;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import android.app.AlertDialog;
import android.hardware.Camera;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class QRScannerActivity2 extends AppCompatActivity implements CompoundBarcodeView.TorchListener  {
    //public static final int REQUEST_CAMERA_PERMISSION = 101;
    private static final int REQUEST_IMAGE_UPLOAD = 102;
    private CompoundBarcodeView barcodeView;
    private Button mFlashButton;
    private boolean isTorchOn = false;

    private Button uploadImageButton;


    /////retrofit2
    private DataService dataService;
    private static final String TAG = QRCodeScannerActivity.class.getSimpleName();
    /////retrofit2

    //global var
    GlobalData globalData = GlobalData.getInstance();
    ArrayList<String> dataList = globalData.getDataList();
    String userid = dataList.get(0);
    //global var

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner2);

        barcodeView = (CompoundBarcodeView) findViewById(R.id.scanner_view);
        mFlashButton = (Button) findViewById(R.id.flash_button);
        uploadImageButton = (Button) findViewById(R.id.upload_image_button);

        barcodeView.setTorchListener(this);
        barcodeView.decodeContinuous(callback);
        // Set click listener for torch button
        mFlashButton.setOnClickListener(view -> toggleTorch());


        // Tombol untuk mengunggah gambar
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Buka galeri untuk memilih gambar
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_UPLOAD);
            }
        });


        /////retrofit2
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Context mContext = QRScannerActivity2.this;
        dataService = UtilsApi.getAPIService();
        /////retrofit2
    }

    private void toggleTorch() {
        if (!isTorchOn) {
            barcodeView.setTorchOn();
            isTorchOn = true;
        } else {
            barcodeView.setTorchOff();
            isTorchOn = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    @Override
    public void onTorchOn() {
        // Torch is turned on
    }

    @Override
    public void onTorchOff() {
        // Torch is turned off
    }

    private DecoratedBarcodeView.TorchListener torchListener = new DecoratedBarcodeView.TorchListener() {
        @Override
        public void onTorchOn() {
            Toast.makeText(QRScannerActivity2.this, "Flashlight on", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onTorchOff() {
            Toast.makeText(QRScannerActivity2.this, "Flashlight off", Toast.LENGTH_SHORT).show();
        }
    };

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            // Do something with the result
            Toast.makeText(QRScannerActivity2.this,"Hasil "+result,Toast.LENGTH_SHORT).show();
            String id_member = result.getText().toString();
            cekMember(QRScannerActivity2.this,id_member);
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
            // Optional callback for when new points are detected
            //Toast.makeText(QRScannerActivity2.this,"Cek Hasil "+resultPoints,Toast.LENGTH_SHORT).show();

        }
    };

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


    private void cekMember(Context mContext, String id_member) {
        //mSwipeRefreshLayout.setRefreshing(true);

        ProgressDialog loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);

        //Toast.makeText(mContext, rg_mail.getText().toString(), Toast.LENGTH_SHORT).show();

        dataService.GetMemberByCode(id_member).
                enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                String ResponseString = response.body().string();
                                // Ambil objek data dari JSON
                                JSONObject jsonRESULTS = new JSONObject(ResponseString);
                                String MessageString = jsonRESULTS.get("message").toString();

                                if (jsonRESULTS.has("data")) {

                                    Object dataObject = jsonRESULTS.get("data");
                                    System.out.println(dataObject);

                                    JSONArray dataArray = new JSONArray();
                                    // Periksa apakah dataObject adalah objek JSON atau array JSON
                                    if (dataObject instanceof JSONArray) {
                                        dataArray = (JSONArray) dataObject;
                                        // Anda dapat melanjutkan pemrosesan seperti biasa jika dataObject adalah array JSON
                                    } else if (dataObject instanceof JSONObject) {
                                        // Buatlah array JSON baru dan tambahkan objek JSON ke dalamnya
                                        dataArray.put(dataObject);
                                        // Anda dapat melanjutkan pemrosesan dengan array JSON yang baru saja dibuat
                                    }

                                    // Output array JSON
                                    System.out.println(dataArray.toString());

                                    Log.e("panjang json array satuan", String.valueOf(dataArray.length()));

                                    if (dataArray.length() > 0) {
                                        getResponJson(dataArray);
                                        System.out.println(MessageString);

                                    } else {
                                        System.out.println(MessageString);
                                    }
                                    Toast.makeText(mContext, MessageString, Toast.LENGTH_SHORT).show();
                                }

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else {
                            loading.dismiss();
                            // Tanggapan HTTP tidak berhasil
                            try {
                                String errorBody = response.errorBody().string();
                                // Tangani errorBody sesuai kebutuhan
                                Toast.makeText(mContext,errorBody,Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        loading.dismiss();
                        //mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(mContext,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getResponJson(JSONArray dataArray) {
        if (dataArray.length() > 0) {
            String id_member="";
            try{
                for (int x = 0; x < dataArray.length(); x++)
                {
                    JSONObject child = dataArray.getJSONObject(x);
                    id_member = child.getString("idmember");
                }

                Intent i = new Intent(QRScannerActivity2.this, TransactionMenuActivity.class);
                i.putExtra("idmember", id_member);
                startActivity(i);

            }catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
    }

}