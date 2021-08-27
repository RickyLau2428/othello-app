package observers;

import java.util.List;

public abstract class Observable {
    private List<Observer> observers;

    public void addObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
}
