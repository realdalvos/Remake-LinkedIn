package org.hbrs.se2.project.services.factory;

import javax.persistence.Entity;

@FunctionalInterface
public interface AbstractEntityFactory<T, S> {

    T createEntity(S input);

}
