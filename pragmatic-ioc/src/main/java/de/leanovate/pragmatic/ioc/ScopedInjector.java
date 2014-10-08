package de.leanovate.pragmatic.ioc;

import java.util.function.Supplier;

/**
 * Generic scope based injector.
 *
 * @param <T> the type to be injected
 */
public class ScopedInjector<T> implements Injector<T> {
    private final String name;

    private final Supplier<Scope> scopeSupplier;

    private final Supplier<? extends T> supplier;

    /**
     * Standard constructor.
     *
     * @param name the name inside the scope
     * @param scopeSupplier supplier for the {@link de.leanovate.pragmatic.ioc.Scope}
     * @param supplier supplier for the instance
     */
    public ScopedInjector(final String name, final Supplier<Scope> scopeSupplier,
            final Supplier<? extends T> supplier) {

        this.name = name;
        this.scopeSupplier = scopeSupplier;
        this.supplier = supplier;
    }

    @Override
    public T inject() {

        return scopeSupplier.get().getInstance(name, supplier);
    }
}
