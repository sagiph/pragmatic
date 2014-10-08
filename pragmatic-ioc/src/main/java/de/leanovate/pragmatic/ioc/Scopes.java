package de.leanovate.pragmatic.ioc;

/**
 * Collection of commonly used {@link de.leanovate.pragmatic.ioc.Scope}s.
 */
public class Scopes {
    static volatile Scope singletonScope = new SingletonScope();

    public static Scope getSingletonScope() {

        return singletonScope;
    }
}
