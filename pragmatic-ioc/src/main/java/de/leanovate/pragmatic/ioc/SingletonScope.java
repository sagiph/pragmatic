package de.leanovate.pragmatic.ioc;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class SingletonScope extends AbstractScope {
    private volatile Map<String, Object> instances = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(final Class<T> injectedClass, final Optional<String> name,
            final Supplier<? extends T> supplier) {

        final String key = name.map((n) -> injectedClass.getName() + ":" + n).orElse(injectedClass.getName());
        T instance = (T) instances.get(key);

        if (instance == null) {
            synchronized (this) {
                instance = (T) instances.get(key);

                if (instance == null) {
                    instance = createInstance(key, supplier);
                    addinstance(key, instance);
                }
            }
        }
        return instance;
    }

    @Override
    public void bind(final Object instance) {

        Objects.requireNonNull(instance);
        addinstance(instance.getClass().getName(), instance);
    }

    @Override
    public <T> void bind(final Class<? super T> injectedClass, final T instance) {

        addinstance(Objects.requireNonNull(injectedClass).getName(), Objects.requireNonNull(instance));
    }

    private synchronized void addinstance(final String key, final Object instance) {
        final Map<String, Object> newInstances = new HashMap<>();
        newInstances.put(key, instance);
        this.instances = newInstances;
    }
}
