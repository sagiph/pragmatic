package de.leanovate.pragmatic.ioc;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class TestScope extends AbstractScope {
    static ThreadLocal<Map<String, Object>> PER_THREAD_INSTANCES = ThreadLocal.withInitial(HashMap::new);

    static ThreadLocal<Function<Class, Object>> MOCKER = new ThreadLocal<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> injectedClass, Optional<String> name, Supplier<? extends T> supplier) {

        final String key = name.map((n) -> injectedClass.getName() + ":" + n).orElse(injectedClass.getName());
        final Map<String, Object> instances = PER_THREAD_INSTANCES.get();

        T instance = (T) instances.get(key);

        if (instance == null) {
            if (MOCKER.get() != null) {
                instance = (T) MOCKER.get().apply(injectedClass);
            } else {
                instance = createInstance(key, supplier);
            }
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

    @Override
    public <T> void bind(final Class<? super T> injectedClass, final T instance) {

        final Map<String, Object> instances = PER_THREAD_INSTANCES.get();
        instances.put(Objects.requireNonNull(injectedClass).getName(), Objects.requireNonNull(instance));
    }

    public <T> T bindMock(final Class<T> injectedClass) {

        if (MOCKER.get() == null) {
            throw new RuntimeException("No mocker defined");
        }
        @SuppressWarnings("unchecked")
        T instance = (T) MOCKER.get().apply(injectedClass);
        bind(injectedClass, instance);
        return instance;
    }

    public void reset(final Function<Class, Object> mocker) {

        PER_THREAD_INSTANCES.remove();
        if (mocker != null) {
            MOCKER.set(mocker);
        } else {
            MOCKER.remove();
        }
    }
}
