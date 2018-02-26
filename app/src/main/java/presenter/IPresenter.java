package presenter;


public interface IPresenter<V> {
    void attach(V view);
    void detach();
}
