package com.nesrux.admin.catalogo.infrastructure.services.local;

import com.nesrux.admin.catalogo.domain.video.Resource;
import com.nesrux.admin.catalogo.infrastructure.services.StorageService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorageService implements StorageService {
    private final Map<String, Resource> storage;

    public Map<String, Resource> storage(){
        return this.storage;
    }
    public void reset() {
        this.storage.clear();
    }

    public InMemoryStorageService() {
        this.storage = new ConcurrentHashMap<>();
    }


    @Override
    public void deleteAll(Collection<String> names) {
        names.forEach(this.storage::remove);
    }

    @Override
    public Optional<Resource> get(String name) {
        return this.storage.get(name);
    }

    @Override
    public List<String> list(String prefix) {

        if (prefix == null) {
            return Collections.emptyList();
        }
        return this.storage.keySet().stream()
                .filter(it -> it.startsWith(prefix))
                .toList();
    }

    @Override
    public void store(String name, Resource resource) {
        this.storage.put(name, resource);
    }
}
