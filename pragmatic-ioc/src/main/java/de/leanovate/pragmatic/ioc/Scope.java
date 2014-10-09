package de.leanovate.pragmatic.ioc;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Generic scope of injection.
 *
 * This is primarily used by {@link de.leanovate.pragmatic.ioc.ScopedInjector}.
 */
public interface Scope {
    /**
     * Get or create an instance in this scope.
     *
     * @param injectedClass class of {@code T}
     * @param name the name of the instance
     * @param supplier supplier of the instance (only invoked if there is no instance in this scope yet)
     * @param <T> type of the instance
     * @return instance for {@code name}
     */
    <T> T getInstance(Class<T> injectedClass, Optional<String> name, Supplier<? extends T> supplier);

    /**
     * Bind an existing instance to this scope.
     *
     * @param instance the instance of bind
     */
    void bind(Object instance);

    /**
     * Bind an existing instance to a specific class.
     *
     * @param injectedClass the injected class
     * @param instance the instance
     * @param <T> type of the instance
     */
    <T> void bind(Class<? super T> injectedClass, T instance);
}
