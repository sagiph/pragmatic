package de.leanovate.pragmatic.ioc;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class SingletonScope extends AbstractScope {
    private Map<String, Object> instances = new ConcurrentHashMap<>();

    private final ReentrantLock createLock = new ReentrantLock();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(final Class<T> injectedClass, final Optional<String> name,
            final Supplier<? extends T> supplier) {

        final String key = name.map((n) -> injectedClass.getName() + ":" + n).orElse(injectedClass.getName());
        T instance = (T) instances.get(key);

        if (instance == null) {
            createLock.lock();
            try {
                instance = (T) instances.get(key);

                if (instance == null) {
                    instance = createInstance(key, supplier);
                    instances.put(key, instance);
                }
            } finally {
                createLock.unlock();
            }
        }
        return instance;
    }

    @Override
    public void bind(final Object instance) {

        Objects.requireNonNull(instance);
        instances.put(instance.getClass().getName(), instance);
    }

    @Override
    public <T> void bind(final Class<? super T> injectedClass, final T instance) {

        instances.put(Objects.requireNonNull(injectedClass).getName(), Objects.requireNonNull(instance));
    }

}
