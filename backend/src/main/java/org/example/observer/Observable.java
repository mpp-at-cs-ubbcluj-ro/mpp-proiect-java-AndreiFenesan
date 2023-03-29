package org.example.observer;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> observer);

    void removeObserver(Observer<E> observer);

    void notifyAllObservers(E event);
}
