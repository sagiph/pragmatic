package de.leanovate.pragmatic.ioc;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Generic scope based injector.
 *
 * @param <T> the type to be injected
 */
public class ScopedInjector<T> implements Injector<T> {
    private final Class<T> injectedClass;

    private final Optional<String> name;

    private final Supplier<Scope> scopeSupplier;

    private final Supplier<? extends T> supplier;

    /**
     * Standard constructor.
     *
     * @param injectedClass class of {@code T}
     * @param name the name inside the scope
     * @param scopeSupplier supplier for the {@link de.leanovate.pragmatic.ioc.Scope}
     * @param supplier supplier for the instance
     */
    public ScopedInjector(final Class<T> injectedClass, final Optional<String> name, final Supplier<Scope> scopeSupplier,
            final Supplier<? extends T> supplier) {

        this.injectedClass = injectedClass;
        this.name = name;
        this.scopeSupplier = scopeSupplier;
        this.supplier = supplier;
    }

    @Override
    public T inject() {

        return scopeSupplier.get().getInstance(injectedClass, name, supplier);
    }

    @Override
    public Scope getScope() {

        return scopeSupplier.get();
    }
}
