package cerebrik.com.alarmapp.activity;

public abstract class BaseController<V> {

    protected final V view;


    protected BaseController(V view) {
        this.view = view;
    }
}
