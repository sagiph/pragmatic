package de.leanovate.pragmatic.ioc;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class TestScope extends AbstractScope {
    static ThreadLocal<Map<String, Object>> PER_THREAD_INSTANCES = ThreadLocal.withInitial(HashMap::new);

    @Override
    public <T> T getInstance(Class<T> injectedClass, Optional<String> name, Supplier<? extends T> supplier) {

        final String key = name.map((n) -> injectedClass.getName() + ":" + n).orElse(injectedClass.getName());
        final Map<String, Object> instances = PER_THREAD_INSTANCES.get();

        @SuppressWarnings("unchecked")
        T instance = (T) instances.get(key);

        if (instance == null) {
            instance = createInstance(key, supplier);
            instances.put(key, instance);
        }

        return instance;
    }

    @Override
    public void bind(final Object instance) {

        Objects.requireNonNull(instance);
        final Map<String, Object> instances = PER_THREAD_INSTANCES.get();
        instances.put(instance.getClass().getName(), instance);
    }

    public void reset() {

        PER_THREAD_INSTANCES.remove();
    }

    public void define(final String name, final Object instance) {

        PER_THREAD_INSTANCES.get().put(name, instance);
    }
}
