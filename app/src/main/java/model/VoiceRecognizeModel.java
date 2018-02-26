package model;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;
import presenter.IVoiceRecognizePresenterCallback;

import static android.content.Intent.ACTION_SEARCH;

public class VoiceRecognizeModel implements IVoiceRecognizeModel, RecognitionListener {
    private static final String TAG = VoiceRecognizeModel.class.getSimpleName();

    private static final String ACTIVATION_KEYPHRASE = "hi pi";
    private static final String WAKEUP_SEARCH = "wakeup";

    private IVoiceRecognizePresenterCallback mPresenter;

    private SpeechRecognizer mSpeechRecognizer;

    public VoiceRecognizeModel(IVoiceRecognizePresenterCallback presenter, Assets assets) {
        Log.d(TAG, "VoiceRecognizeModel");
        this.mPresenter = presenter;
        initRecognizer(assets);

    }

    //TODO make it async
    private void initRecognizer(Assets assets) {
        Log.d(TAG, "initRecognizer");

        try {

            File assetsDir = assets.syncAssets();

            mSpeechRecognizer = SpeechRecognizerSetup.defaultSetup()
                    .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                    .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                    .getRecognizer();

            mSpeechRecognizer.addListener(this);

            mSpeechRecognizer.addKeyphraseSearch(WAKEUP_SEARCH, ACTIVATION_KEYPHRASE);
            mSpeechRecognizer.addNgramSearch(ACTION_SEARCH, new File(assetsDir, "predefined.lm.bin"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech");

    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "onEndOfSpeech");

        if (!mSpeechRecognizer.getSearchName().equals(WAKEUP_SEARCH)) {
            mSpeechRecognizer.stop();
        }
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        Log.d(TAG, "onPartialResult");

        if (hypothesis == null) {
            return;
        }
        Log.d(TAG, "onPartialResult, result [" + hypothesis.getHypstr() +"]");

        if (hypothesis.getHypstr().equals(ACTIVATION_KEYPHRASE)) {
            mPresenter.onActivationPhraseDetected(hypothesis.getHypstr());
            mSpeechRecognizer.stop();
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        Log.d(TAG, "onResult");

        if (hypothesis == null) {
            return;
        }

        if (ACTIVATION_KEYPHRASE.equals(hypothesis.getHypstr())) {
            mPresenter.onActivationPhraseDetected(hypothesis.getHypstr());
            mSpeechRecognizer.stop();
        }
    }

    @Override
    public void onError(Exception e) {
        Log.d(TAG, "onError");

    }

    @Override
    public void onTimeout() {
        Log.d(TAG, "onTimeout");
        mSpeechRecognizer.stop();

    }

    @Override
    public void startListeningActivatingPhrase() {
        Log.d(TAG, "startListeningActivatingPhrase");
        mSpeechRecognizer.startListening(WAKEUP_SEARCH);

    }

    @Override
    public void stopListeningActivatingPhrase() {
        Log.d(TAG, "stopListeningActivatingPhrase");
        mSpeechRecognizer.stop();
    }
}
