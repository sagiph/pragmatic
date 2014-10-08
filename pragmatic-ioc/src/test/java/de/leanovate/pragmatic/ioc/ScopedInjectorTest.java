package de.leanovate.pragmatic.ioc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;

import java.util.function.Supplier;

public class ScopedInjectorTest {
    @Test
    public void testInject() {

        Scope scope = mock(Scope.class);
        Supplier<?> supplier = mock(Supplier.class);

        ScopedInjector<Object> injector = new ScopedInjector<>("testInstance", () -> scope, supplier);

        verifyZeroInteractions(scope, supplier);

        injector.inject();

        verify(scope).getInstance("testInstance", supplier);
        verifyZeroInteractions(supplier);
    }
}