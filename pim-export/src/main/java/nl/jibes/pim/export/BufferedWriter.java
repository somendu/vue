package nl.jibes.pim.export;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class BufferedWriter<T> {
  private final int bufferSize;
  private final List<T> buffer;

  public BufferedWriter(int bufferSize) {
    if (bufferSize < 1) {
      throw new IllegalArgumentException("invalid buffer size");
    }

    this.bufferSize = bufferSize;

    buffer = new ArrayList<>(bufferSize);
  }

  public void flush() throws IOException {
    if (!buffer.isEmpty()) {
      flush(buffer);
      buffer.clear();
    }
  }

  public void write(T item) throws IOException {
    buffer.add(item);

    if (buffer.size() == bufferSize) {
      flush();
    }
  }

  protected abstract void flush(List<T> items) throws IOException;
}
