package de.leanovate.pragmatic.ioc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class SingletonScope implements Scope {
    private ThreadLocal<Set<String>> createStack = ThreadLocal.withInitial(HashSet<String>::new);

    private volatile Map<String, Object> instances = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(final String name, final Supplier<T> supplier) {

        T instance = (T) instances.get(name);

        if (instance == null) {
            synchronized (this) {
                instance = (T) instances.get(name);

                if (instance == null) {
                    if (createStack.get().contains(name)) {
                        throw new RuntimeException("Cyclic reference to '" + name + "'");
                    }
                    createStack.get().add(name);
                    instance = supplier.get();
                    instances = new HashMap<>(instances);
                    instances.put(name, instance);
                    createStack.get().remove(name);
                    if (createStack.get().isEmpty()) {
                        createStack.remove();
                    }
                }
            }
        }
        return instance;
    }
}
