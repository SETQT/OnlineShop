package com.Shop.Service.Generic;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IGenericService <T>{
    List<T> findAll()  throws Exception;
    T save(T entity) throws Exception;
    void delete(Long id) throws Exception;
    //T findById(Long id) throws Exception;
    Optional<T> findById(Long id) throws Exception;

}
