package com.example.micaelomota.luzparathais;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    private CameraManager mCameraManager;
    private String mCameraId;
    private Boolean isOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verificando se o aparalho tem flash.
        Boolean hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            showAlertError("Infelizmente seu smartphone não tem flash.");
            return;
        }

        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            showAlertError("Ocorreu um erro ao acessar a camera do seu smartphone");
            e.printStackTrace();
        }

        final Button ligar = (Button) findViewById(R.id.btnLigar);
        final Button desligar = (Button) findViewById(R.id.btnDesligar);

        ligar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desligar.setText(R.string.desligar);
                desligar.setBackgroundResource(R.color.filtro);
                desligar.setEnabled(true);

                ligar.setText("");
                ligar.setBackgroundResource(R.color.black);
                ligar.setEnabled(false);

                toggleFlashLight(isOn = true);
            }
        });

        desligar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ligar.setText(R.string.ligar);
                ligar.setBackgroundResource(R.color.filtro);
                ligar.setEnabled(true);

                desligar.setText("");
                desligar.setBackgroundResource(R.color.black);
                desligar.setEnabled(false);

                toggleFlashLight(isOn=false);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isOn){
            toggleFlashLight(isOn = false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isOn){
            toggleFlashLight(isOn = false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isOn){
            toggleFlashLight(isOn = true);
        }
    }

    public void toggleFlashLight(Boolean on) {
        try {
            mCameraManager.setTorchMode(mCameraId, on);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAlertError(String message){
        AlertDialog alert = new AlertDialog.Builder(this)
                .create();
        alert.setTitle("Que pena...");
        alert.setMessage(message);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // closing the application
                finish();
                System.exit(0);
            }
        });
        alert.show();
    }


}
