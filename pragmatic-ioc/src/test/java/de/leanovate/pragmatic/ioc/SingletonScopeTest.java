package de.leanovate.pragmatic.ioc;

import org.junit.Test;

public class SingletonScopeTest {

    SingletonScope singletonScope = new SingletonScope();

    @Test(expected = CyclicDependencyException.class)
    public void testCycleDetection() {

        singletonScope.getInstance("testCycleDetection", () ->
                singletonScope.getInstance("testCycleDetection", () -> null));
    }

    @Test(expected = CyclicDependencyException.class)
    public void testCycleDetection2() {

        singletonScope.getInstance("testCycleDetection2", () ->
                singletonScope.getInstance("testCycleDetection2a", () ->
                        singletonScope.getInstance("testCycleDetection2", () -> null)));
    }

    @Test(expected = NullPointerException.class)
    public void testRequestNonNull() {

        singletonScope.getInstance("notNull", () -> null);
    }
}