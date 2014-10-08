package de.leanovate.pragmatic.ioc;

import java.util.function.Supplier;

public class ScopedInjector<T> implements Injector<T> {
    private final String name;

    private final Supplier<Scope> scopeSupplier;

    private final Supplier<? extends T> supplier;

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
