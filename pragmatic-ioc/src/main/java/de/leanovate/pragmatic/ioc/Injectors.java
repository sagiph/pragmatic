package de.leanovate.pragmatic.ioc;

import java.util.function.Supplier;

/**
 * Collection of commonly used {@link de.leanovate.pragmatic.ioc.Injector}s.
 */
public class Injectors {
    /**
     * Create a singleton injector for a type.
     *
     * @param injectedClass the type to be injected
     * @param supplier the supplier of the singleton instance (in production is is only invoked once)
     * @param <T> the type to be injected
     * @return singleton injector for {@code T}
     */
    public static <T> Injector<T> singleton(final Class<T> injectedClass, final Supplier<? extends T> supplier) {

        return new ScopedInjector<>(injectedClass.getName(), Scopes::getSingletonScope, supplier);
    }
}
