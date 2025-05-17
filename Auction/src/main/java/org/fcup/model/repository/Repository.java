package org.fcup.model.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<O, I> {
    boolean save(O object) throws Exception;
    boolean update(O object) throws Exception;
    List<O> findAll();
    Optional<O> findById(I id);
}
