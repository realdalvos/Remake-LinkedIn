package org.hbrs.se2.project.services.factory;

import java.io.Serializable;

@FunctionalInterface
public interface AbstractEntityFactory<T, S extends Serializable> {

    T createEntity(S input);

}
