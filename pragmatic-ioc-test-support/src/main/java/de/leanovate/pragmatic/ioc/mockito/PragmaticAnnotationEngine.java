package de.leanovate.pragmatic.ioc.mockito;

import de.leanovate.pragmatic.ioc.Injector;
import de.leanovate.pragmatic.ioc.TestInjectors;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.DefaultAnnotationEngine;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.reflection.FieldReader;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Alternative implementation of the {@link org.mockito.internal.configuration.InjectingAnnotationEngine}.
 */
public class PragmaticAnnotationEngine implements AnnotationEngine {
    private final AnnotationEngine delegate = new DefaultAnnotationEngine();

    private final AnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

    private final boolean mockAll;

    public PragmaticAnnotationEngine() {

        this(false);
    }

    public PragmaticAnnotationEngine(final boolean mockAll) {

        this.mockAll = mockAll;
    }

    @Override
    public Object createMockFor(final Annotation annotation, final Field field) {

        final Object mock = delegate.createMockFor(annotation, field);

        bindMock(field, mock);

        return mock;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void process(final Class<?> clazz, final Object testInstance) {

        if (mockAll) {
            TestInjectors.reset(Mockito::mock);
        } else {
            TestInjectors.reset();
        }
        processIndependentAnnotations(testInstance.getClass(), testInstance);
        processInjectMocks(testInstance.getClass(), testInstance);
    }

    private void processIndependentAnnotations(final Class<?> clazz, final Object testInstance) {

        Class<?> classContext = clazz;
        while (classContext != Object.class) {
            //this will create @Mocks, @Captors, etc:
            delegate.process(classContext, testInstance);
            //this will create @Spies:
            spyAnnotationEngine.process(classContext, testInstance);

            for (Field field : classContext.getDeclaredFields()) {
                bindMock(field, new FieldReader(testInstance, field).read());
            }

            classContext = classContext.getSuperclass();
        }
    }

    private void processInjectMocks(final Class<?> clazz, final Object testInstance) {

        Class<?> classContext = clazz;
        while (classContext != Object.class) {
            for (Field field : classContext.getDeclaredFields()) {
                if (field.getAnnotation(InjectMocks.class) != null) {
                    injectMocks(field, testInstance);
                }
            }
            classContext = classContext.getSuperclass();
        }
    }

    private void injectMocks(final Field field, final Object testInstance) {

        try {
            final Field injectorField = field.getType().getField("INJECTOR");
            final Object injector = injectorField.get(null);

            if ( injector instanceof Injector) {
                new FieldSetter(testInstance, field).set(((Injector) injector).inject());
                return;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Ignore this
        }

        try {
            new FieldSetter(testInstance, field).set(field.getType().newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new MockitoException("No suitable constructor found", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void bindMock(final Field field, final Object mock) {

        final Mock mockAnnoation = field.getAnnotation(Mock.class);

        if (mockAnnoation != null) {

            for (Class eif : mockAnnoation.extraInterfaces()) {
                TestInjectors.bind(eif, mock);
            }
            TestInjectors.bind(mock);
        } else if (field.getAnnotation(MockitoAnnotations.Mock.class) != null) {
            TestInjectors.bind(mock);
        }
    }
}
