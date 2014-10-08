package de.leanovate.pragmatic.ioc;

public class TestInjectors {
    public static final TestScope TEST_SCOPE = new TestScope();

    public static void init() {

        if (Scopes.singletonScope != TEST_SCOPE) {
            Scopes.singletonScope = TEST_SCOPE;
        }
    }

    public static <T> void define(Class<? super T> clazz, T instance) {

        init();
        TEST_SCOPE.define(clazz.getName(), instance);
    }

    public static void reset() {

        init();
        TEST_SCOPE.reset();
    }
}
