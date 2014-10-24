package de.leanovate.pragmatic.ioc.mockito;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.Mockito;
import org.mockito.configuration.AnnotationEngine;

import java.util.Objects;

/**
 * This should be used instead of {@link org.mockito.runners.MockitoJUnitRunner} or {@link
 * org.mockito.junit.MockitoJUnitRule}.
 *
 * <ul>
 * <li>
 * All mocks annotated with {@link org.mockito.Mock}, will be initialized and bound to the test scope.
 * </li>
 * <li>
 * The class in testing should be either create inside a {@link org.junit.Before} step or via a {@link
 * org.mockito.InjectMocks} annotation.
 * </li>
 * <li>
 * {@link org.mockito.InjectMocks} operates like this: If a static {@code INJECTOR} is present it will be used
 * otherwise the default constructor is used.
 * </li>
 * </ul>
 * Example:
 * <pre class="code"><code class="java">
 * public class ExampleTest {
 *
 *   &#064;Rule
 *   public MockitoRule mockitoRule = new MockitoRule(this);
 *
 *   &#064;Mock
 *   private SomeObject somemockedobject;
 *
 *   &#064;InjectMock
 *   private ToTest toTest;
 *
 *   &#064;Test
 *   public void shouldDoSomething() {
 *     ...
 *   }
 * }
 * </pre>
 */
public class MockitoRule implements TestRule {
    private final Object testClass;

    private final AnnotationEngine annotationEngine;

    public MockitoRule(final Object testClass) {

        this(testClass, false);
    }

    public MockitoRule(final Object testClass, final boolean mockAll) {

        Objects.requireNonNull(testClass, "Rule target");
        this.annotationEngine = new PragmaticAnnotationEngine(mockAll);
        this.testClass = testClass;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {

                annotationEngine.process(testClass.getClass(), testClass);
                base.evaluate();
                Mockito.validateMockitoUsage();
            }
        };
    }
}
