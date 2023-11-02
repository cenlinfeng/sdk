package com.ddtsdk.common.jsonadapter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by CZG on 2019/10/10.
 * 基于ParameterizedType实现泛型类类型参数化
 */

public class ParameterizedTypeImpl implements ParameterizedType {

    private final Class raw;
    private final Type[] args;
    private final Type owner;

    public ParameterizedTypeImpl(Class raw, Type[] args, Type owner) {
        this.raw = raw;
        this.args = args != null ? args : new Type[0];
        this.owner = owner;
    }
    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }
    @Override
    public Type getRawType() {
        return raw;
    }
    @Override
    public Type getOwnerType() {
        return owner;
    }
}
