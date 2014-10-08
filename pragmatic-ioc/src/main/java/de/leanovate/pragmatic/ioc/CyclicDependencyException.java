package de.leanovate.pragmatic.ioc;

import java.util.List;

public class CyclicDependencyException extends RuntimeException {
    public CyclicDependencyException(final List<String> stack) {

        super("Cyclic reference to " + String.join(" -> ", stack) + "");
    }
}
