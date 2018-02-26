package presenter;

public interface IVoiceRecognizePresenter extends IPresenter<IVoiceRecognitionView> {
    void startListeningActivatingPhrase();
    void stopListeningActivatingPhrase();
}
