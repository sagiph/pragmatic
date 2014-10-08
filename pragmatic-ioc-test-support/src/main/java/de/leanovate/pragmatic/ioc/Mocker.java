package de.leanovate.pragmatic.ioc;

public interface Mocker {
    <T> T createMock(Class<T> clazz);
}
