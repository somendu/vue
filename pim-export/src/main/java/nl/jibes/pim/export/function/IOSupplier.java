package nl.jibes.pim.export.function;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * Same as {@link Supplier}, but allows an {@link IOException} to be thrown
 *
 * @author edgardegraaff
 *
 * @param <T>
 */
@FunctionalInterface
public interface IOSupplier<T> {
	T get() throws IOException;
}
