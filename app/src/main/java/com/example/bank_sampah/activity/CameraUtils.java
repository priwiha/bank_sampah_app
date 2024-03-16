package com.example.bank_sampah.activity;
import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
public class CameraUtils {
    public static void openCamera(Context context, SurfaceHolder holder) throws IOException {
        Camera camera = Camera.open();

        camera.setPreviewDisplay(holder);
        camera.startPreview();
    }
}
