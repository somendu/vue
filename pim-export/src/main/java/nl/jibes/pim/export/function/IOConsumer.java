package nl.jibes.pim.export.function;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Same as {@link Consumer}, but allows the function to throw an {@link IOException}
 *
 * @author edgardegraaff
 *
 * @param <T>
 */
@FunctionalInterface
public interface IOConsumer<T> {
  void accept(T t) throws IOException;
}
