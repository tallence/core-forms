package com.tallence.formeditor.cae.serializer;

import java.util.function.BiConsumer;

public interface ThrowingBiConsumer<T, U, E extends Exception> {
  void accept(T t, U u) throws E;

  static <T, U, E extends Exception> BiConsumer<T, U> unchecked(ThrowingBiConsumer<T, U, E> biConsumer) throws E {
    return (t, u) -> {
      try {
        biConsumer.accept(t, u);
      } catch (Exception exception) {
        throw new RuntimeException(exception);
      }
    };
  }

}
