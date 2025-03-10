package Managers;


import model.Route;

import java.time.LocalDateTime;

import java.util.*;

/**
 * Класс для управления коллекцией
 * @author sh_ub
 */
public class CollectionManager {
    private Long currentId = 1L;
    private Map<Long, Route> routes = new HashMap<>();
    private Vector<Route> collection = new Vector<Route>();
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private final DumpManager dumpManager;

    public CollectionManager(DumpManager dumpManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.dumpManager = dumpManager;
    }

    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    public boolean isContain(Route route) {
        return routes.containsKey(route.getId());
    }

    public Long getCurrentId() {
        while(routes.get(currentId) != null) currentId++;
        return currentId;
    }
    /**
     * Метод для сохранения коллекции в файл
     */
    public void saveCollection() {
        dumpManager.dump(collection);
        lastSaveTime = LocalDateTime.now();
    }
    /**
     * Метод для добавления маршрута в коллекцию
     * @param r маршрут
     */
    public boolean add(Route r) {
        if (isContain(r)) return false;
        routes.put(r.getId(), r);
        collection.add(r);
        sort();
        return true;
    }
    /**
     * Метод для удаления маршрута из коллекции по id
     * @param id маршрут
     */
    public boolean remove(Long id) {
        var r = routes.get(id);
        if (r == null) return false;
        routes.remove(r.getId());
        collection.remove(r);
        sort();
        return true;
    }
    /**
     * Метод для сортировки коллекции
     */
    public void sort(){
        Collections.sort(collection);
    }

    public boolean update(Route r) {
        if (!isContain(r)) return false;
        collection.remove(routes.get(r.getId()));
        routes.put(r.getId(), r);
        collection.add(r);
        sort();
        return true;
    }
    /**
     * Метод для инициализации коллекции
     */
    public boolean init(){
        collection.clear();
        lastInitTime = LocalDateTime.now();
        collection = dumpManager.read();
        for (var e : collection)
            if (routes.get(e.getId()) != null) {
                collection.clear();
                routes.clear();
                return false;
            } else {
                if (e.getId()>currentId) currentId = e.getId();
                routes.put(e.getId(), e);
            }
        sort();
        return true;
    }
    /**
     * Метод для очищения коллекции
     */
    public void clear() {
        collection.clear();
        routes.clear();
        lastInitTime = LocalDateTime.now();
    }
    public Vector<Route> getCollection() {
        return collection;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (var e : collection){
            str.append(e.toString()).append("\n");
        }
        return str.toString();
    }
}
