package de.leanovate.pragmatic.ioc;

import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class AbstractScope implements Scope {
    protected ThreadLocal<LinkedList<String>> createStack = ThreadLocal.withInitial(LinkedList<String>::new);

    protected <T> T createInstance(final String key, final Supplier<T> supplier) {

        final LinkedList<String> stack = createStack.get();
        if (stack.contains(key)) {
            throw new CyclicDependencyException(stack);
        }
        try {
            stack.add(key);
            return Objects.requireNonNull(supplier.get(), () -> "Instantiation of '" + key + "' failed");
        } finally {
            stack.removeLast();
            if (stack.isEmpty()) {
                createStack.remove();
            }
        }
    }
}
