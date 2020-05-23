package nl.jibes.pim.export.transport;

import java.io.IOException;
import java.util.Map;

public interface Transport {
	void write(Map<String, Object> value, int numberOfItems) throws IOException;
}
