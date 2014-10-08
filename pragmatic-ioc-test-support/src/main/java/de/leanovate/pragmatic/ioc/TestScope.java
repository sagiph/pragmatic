package de.leanovate.pragmatic.ioc;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TestScope implements Scope {
    static ThreadLocal<Map<String, Object>> PER_THREAD_INSTANCES = ThreadLocal.withInitial(HashMap::new);

    @Override
    public <T> T getInstance(final String name, final Supplier<T> supplier) {

        final Map<String, Object> instances = PER_THREAD_INSTANCES.get();

        @SuppressWarnings("unchecked")
        T instance = (T) instances.get(name);

        if (instance == null) {
            instance = supplier.get();
            instances.put(name, instance);
        }
        return instance;
    }

    public void reset() {

        PER_THREAD_INSTANCES.remove();
    }

    public void define(final String name, final Object instance) {

        PER_THREAD_INSTANCES.get().put(name, instance);
    }
}
