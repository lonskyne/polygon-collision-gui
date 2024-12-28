package main.observer;

import java.util.ArrayList;
import java.util.List;

public class MyObservable {
    private List<MyListener> listeners = new ArrayList<>();

    public void addListener(MyListener newListener) {
        listeners.add(newListener);
    }

    public void removeListener(MyListener listenerToRemove) {
        listeners.remove(listenerToRemove);
    }

    public void notify(String message) {
        listeners.forEach(myListener -> myListener.update(message));
    }
}
