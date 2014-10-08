package de.leanovate.pragmatic.ioc;

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
     * @param name the name of the instance
     * @param supplier supplier of the instance (only invoked if there is no instance in this scope yet)
     * @param <T> type of the instance
     * @return instance for {@code name}
     */
    <T> T getInstance(String name, Supplier<T> supplier);
}
