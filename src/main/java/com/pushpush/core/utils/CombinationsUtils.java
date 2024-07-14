package com.pushpush.core.utils;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@UtilityClass
public class CombinationsUtils {

    public static <A, B, R> List<R> allCombinations2(Collection<A> as, Collection<B> bs, Function2<A, B, R> constructor) {
        List<R> result = new ArrayList<>();
        as.forEach(a -> bs.forEach(b -> result.add(constructor.apply(a, b))));
        return result;
    }

    public static <A, B, C, R> List<R> allCombinations3(Iterable<A> as, Iterable<B> bs, Iterable<C> cs, Function3<A, B, C, R> constructor) {
        List<R> result = new ArrayList<>();
        as.forEach(a -> bs.forEach(b -> cs.forEach(c -> result.add(constructor.apply(a, b, c)))));
        return result;
    }

    @FunctionalInterface
    interface Function2<A, B, R> {
        R apply(A a, B b);
    }

    @FunctionalInterface
    public interface Function3<A, B, C, R> {
        R apply(A a, B b, C c);
    }

}
