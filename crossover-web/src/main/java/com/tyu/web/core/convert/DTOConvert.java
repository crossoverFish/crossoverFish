package com.tyu.web.core.convert;

public interface DTOConvert<T,S> {
    T convertEntity(S s);

    T convertDTO(S s);

}
