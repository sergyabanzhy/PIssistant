package com.example.sergzhy.pissistant;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import javax.annotation.Nonnull;

import edu.cmu.pocketsphinx.Assets;
import presenter.IVoiceRecognitionView;
import presenter.IVoiceRecognizePresenter;
import presenter.VoiceRecognizePresenter;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity implements IVoiceRecognitionView {
    private static final String TAG = MainActivity.class.getSimpleName();

    private IVoiceRecognizePresenter mPresenter;
    private static final int RECORD_AUDIO_PERMISSION = 15420;
    private static final int WRITE_EXTERNAL_PERMISSION = 15920;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        initPresenter();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        mPresenter.attach(this);
        mPresenter.startListeningActivatingPhrase();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        mPresenter.stopListeningActivatingPhrase();
        mPresenter.detach();
    }

    private void initPresenter() {
        Log.d(TAG, "initPresenter");
        mPresenter = new VoiceRecognizePresenter(initAssets());
    }

    private Assets initAssets() {
        Log.d(TAG, "initAssets");
        try {
            return new Assets(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onCreate");
        super.onDestroy();
    }

    @Override
    public void onActivationPhraseDetected() {
        Log.d(TAG, "onActivationPhraseDetected");
        //TODO start assistant request
    }


    //TODO Fix permission request. Looks like it is completely incorrect
    @Override
    public void onRequestPermissionsResult(int requestCode, @Nonnull String[] permissions, @Nonnull int[] grantResults) {

        switch (requestCode) {
            case RECORD_AUDIO_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "PERMISSION_GRANTED [Record Audio]");
                }
                break;

            case WRITE_EXTERNAL_PERMISSION:
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "PERMISSION_GRANTED [Write external]");
                break;
            }
        }
    }

    private void checkPermissions() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    RECORD_AUDIO_PERMISSION);
        }

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_PERMISSION);
        }
    }
}
