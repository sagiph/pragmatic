package de.leanovate.pragmatic.ioc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class SingletonScope implements Scope {
    private ThreadLocal<LinkedList<String>> createStack = ThreadLocal.withInitial(LinkedList<String>::new);

    private volatile Map<String, Object> instances = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(final String name, final Supplier<T> supplier) {

        T instance = (T) instances.get(name);

        if (instance == null) {
            synchronized (this) {
                instance = (T) instances.get(name);

                if (instance == null) {
                    final LinkedList<String> stack = createStack.get();
                    if (stack.contains(name)) {
                        throw new CyclicDependencyException(stack);
                    }
                    stack.add(name);
                    instance = Objects.requireNonNull(supplier.get(), () -> "Instantiation of '" + name + "' failed");
                    instances = new HashMap<>(instances);
                    instances.put(name, instance);
                    stack.removeLast();
                    if (stack.isEmpty()) {
                        createStack.remove();
                    }
                }
            }
        }
        return instance;
    }
}
