package com.projectkorra.core.util.data;

import java.util.function.Function;

public final class CollectionUtil {

	private CollectionUtil() {
	}

	public static <T> T[] fillArray(T[] arr, Function<Integer, T> filler) {
		for (int i = 0; i < arr.length; ++i) {
			arr[i] = filler.apply(i);
		}

		return arr;
	}
}
