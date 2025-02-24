package Managers;


import model.Route;

import java.time.LocalDateTime;

import java.util.*;


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

    public void saveCollection() {
        dumpManager.dump(collection);
        lastSaveTime = LocalDateTime.now();
    }

    public boolean add(Route r) {
        if (isContain(r)) return false;
        routes.put(r.getId(), r);
        collection.add(r);
        sort();
        return true;
    }

    public boolean remove(Long id) {
        var r = routes.get(id);
        if (r == null) return false;
        routes.remove(r.getId());
        collection.remove(r);
        sort();
        return true;
    }

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
}
