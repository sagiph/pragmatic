package de.leanovate.pragmatic.ioc;

/**
 * Generic injector for a type.
 *
 * @param <T> the type to be injected
 */
public interface Injector<T> {
    /**
     * Inject a concrete instance.
     *
     * @return the instance to inject
     */
    T inject();

    /**
     * Get the scope of this injector.
     *
     * @return the scope of injection
     */
    Scope getScope();
}
