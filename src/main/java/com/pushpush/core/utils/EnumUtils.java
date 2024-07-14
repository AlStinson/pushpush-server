package com.pushpush.core.utils;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class EnumUtils {
    public static byte byteOrdinal(Enum e) {
        return (byte) e.ordinal();
    }

    public static <E extends Enum> List<E> listOf(Class<E> enumClass) {
        return Arrays.asList(enumClass.getEnumConstants());
    }
}
