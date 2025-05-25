package Managers;


import model.Route;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;



/**
 * Класс для управления коллекцией
 * @author sh_ub
 */
public class CollectionManager {
    private static final Map<Long, Route> routes = new HashMap<>();
    static ArrayList<Route> collection = new ArrayList<>();
    private LocalDateTime lastInitTime;
    private final LocalDateTime lastSaveTime;
    private final DbRoutesManager dbRoutesManager;

    public CollectionManager(DbRoutesManager dbRoutesManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.dbRoutesManager = dbRoutesManager;
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
        dbRoutesManager.deleteRoute(r.getId());
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
        dbRoutesManager.updateRoute(r);
        sort();
        return true;
    }

    /**
     * Метод для инициализации коллекции
     */
    public synchronized boolean init()  {
        collection.clear();
        lastInitTime = LocalDateTime.now();
        collection = dbRoutesManager.loadCollection();
        collection.forEach(r -> routes.put(r.getId(), r));

        sort();
        return true;
    }

    /**
     * Метод для очищения коллекции
     */
    public void clear(String ownerId) {
        dbRoutesManager.clearTables(ownerId);
        collection = dbRoutesManager.loadCollection();
        collection.forEach(r -> routes.put(r.getId(), r));

    }
    public ArrayList<Route> getCollection() {
        return collection;
    }

    @Override
    public String toString() {
        return collection.isEmpty() ?  "" :  collection.stream().map(Route::toString).collect(Collectors.joining("\n"));
    }
}
