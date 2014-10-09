package de.leanovate.pragmatic.ioc;

import org.junit.Test;

import java.util.Optional;

public class SingletonScopeTest {

    SingletonScope singletonScope = new SingletonScope();

    @Test(expected = CyclicDependencyException.class)
    public void testCycleDetection() {

        singletonScope.getInstance(Object.class, Optional.of("testCycleDetection"), () ->
                singletonScope.getInstance(Object.class, Optional.of("testCycleDetection"), () -> null));
    }

    @Test(expected = CyclicDependencyException.class)
    public void testCycleDetection2() {

        singletonScope.getInstance(Object.class, Optional.of("testCycleDetection2"), () ->
                singletonScope.getInstance(Object.class, Optional.of("testCycleDetection2a"), () ->
                        singletonScope.getInstance(Object.class, Optional.of("testCycleDetection2"), () -> null)));
    }

    @Test(expected = NullPointerException.class)
    public void testRequestNonNull() {

        singletonScope.getInstance(Object.class, Optional.of("notNull"), () -> null);
    }
}