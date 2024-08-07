package com.pushpush.core.utils;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class EnumUtils {
    public static <E extends Enum<E>> byte byteOrdinal(Enum<E> e) {
        return (byte) e.ordinal();
    }

    public static <E extends Enum<E>> List<E> listOf(Class<E> enumClass) {
        return Arrays.asList(enumClass.getEnumConstants());
    }
}
