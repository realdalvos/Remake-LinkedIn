package org.hbrs.se2.project.services.factory;

@FunctionalInterface
public interface AbstractEntityFactory<T> {

    T createEntity();

}
