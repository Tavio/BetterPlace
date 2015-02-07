package br.com.betterplace.web.adapter;

import java.lang.reflect.Array;
import java.util.List;

import br.com.betterplace.web.utils.Orika;

public class Adapters {
    
    public static <S, T> T adapt(S model, Class<T> resource) {
        return Orika.get().map(model, resource);
    }

    public static <S, T> List<T> adaptList(List<S> modelList, Class<T> resource) {
        return Orika.get().mapAsList(modelList, resource);
    }

    @SuppressWarnings("unchecked")
    public static <S, T> T[] adaptArray(S[] modelList, Class<T> resource) {
        T[] out = (T[]) Array.newInstance(resource, modelList.length);
        return Orika.get().mapAsArray(out, modelList, resource);
    }
}