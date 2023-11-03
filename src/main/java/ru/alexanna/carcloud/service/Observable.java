package ru.alexanna.carcloud.service;

public interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(Long id);
}
