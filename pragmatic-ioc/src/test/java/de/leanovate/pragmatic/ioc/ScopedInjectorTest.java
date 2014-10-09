package de.leanovate.pragmatic.ioc;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;

import java.util.Optional;
import java.util.function.Supplier;

public class ScopedInjectorTest {
    @Test
    public void testInject() {

        Scope scope = mock(Scope.class);
        Supplier<?> supplier = mock(Supplier.class);

        ScopedInjector<Object> injector =
                new ScopedInjector<>(Object.class, Optional.of("testInstance"), () -> scope, supplier);

        verifyZeroInteractions(scope, supplier);

        injector.inject();

        verify(scope).getInstance(Object.class, Optional.of("testInstance"), supplier);
        verifyZeroInteractions(supplier);
    }

    @Test
    public void testGetScope() {

        Scope scope = mock(Scope.class);
        Supplier<?> supplier = mock(Supplier.class);

        ScopedInjector<Object> injector = new ScopedInjector<>(Object.class, Optional
                .of("testInstance"), () -> scope, supplier);

        assertThat(injector.getScope(), sameInstance(scope));
    }
}