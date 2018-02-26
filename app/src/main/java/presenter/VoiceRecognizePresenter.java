package presenter;

import android.util.Log;

import edu.cmu.pocketsphinx.Assets;
import model.IVoiceRecognizeModel;
import model.VoiceRecognizeModel;

public class VoiceRecognizePresenter implements
        IVoiceRecognizePresenterCallback,
        IVoiceRecognizePresenter {

    private static final String TAG = VoiceRecognizePresenter.class.getSimpleName();

    private IVoiceRecognizeModel mModel;

    private IVoiceRecognitionView mView;

    public VoiceRecognizePresenter(Assets assets) {
        initVoiceRecognizeModel(assets);
    }

    @Override
    public void attach(IVoiceRecognitionView view) {
        Log.d(TAG, "attach");
        this.mView = view;
    }

    private void initVoiceRecognizeModel(Assets assets) {
        Log.d(TAG, "initVoiceRecognizeModel");
        this.mModel = new VoiceRecognizeModel(this, assets);
    }

    @Override
    public void detach() {
        Log.d(TAG, "detach");
        mView = null;
        mModel.stopListeningActivatingPhrase();
    }

    @Override
    public void onActivationPhraseDetected(String result) {
        Log.d(TAG, "onActivationPhraseDetected, phrase [" + result +"]");
        mView.onActivationPhraseDetected();
    }

    @Override
    public void startListeningActivatingPhrase() {
        Log.d(TAG, "startListeningActivatingPhrase");
        mModel.startListeningActivatingPhrase();
    }

    @Override
    public void stopListeningActivatingPhrase() {
        Log.d(TAG, "stopListeningActivatingPhrase");
        mModel.stopListeningActivatingPhrase();
    }
}
