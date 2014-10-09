package de.leanovate.pragmatic.ioc;

import java.util.function.Function;

public class TestInjectors {
    public static final TestScope TEST_SCOPE = new TestScope();

    public static void init() {

        if (Scopes.singletonScope != TEST_SCOPE) {
            Scopes.singletonScope = TEST_SCOPE;
        }
    }

    public static void bind(Object instance) {

        init();
        TEST_SCOPE.bind(instance);
    }

    public static <T> void bind(Class<? super T> injectedClass, T instance) {

        init();
        TEST_SCOPE.bind(injectedClass, instance);
    }

    public static <T> T bindMock(Class<T> injectedClass) {

        init();
        return TEST_SCOPE.bindMock(injectedClass);
    }

    public static void reset() {

        init();
        TEST_SCOPE.reset(null);
    }

    public static void reset(Function<Class, Object> mocker) {

        init();
        TEST_SCOPE.reset(mocker);
    }
}
