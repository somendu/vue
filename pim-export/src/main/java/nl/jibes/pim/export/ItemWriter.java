package nl.jibes.pim.export;

import com.fasterxml.jackson.core.TreeNode;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import nl.jibes.pim.export.transport.Transport;

/**
 * This is the item writer class to post the split json
 *
 * @author SomenduMaiti
 *
 */
public class ItemWriter extends BufferedWriter<TreeNode> {
	private final Map<String, Object> header;
	private final Transport transport;

	public ItemWriter(
			int bufferSize,
			Map<String, Object> header,
			Transport transport) {
		super(bufferSize);

		this.header = header;
		this.transport = transport;
	}

	@Override
	protected void flush(List<TreeNode> items) throws IOException {
		// yes, we modify the original headers value, but that's okay since we only have it for this purpose
		header.put(PimExportSplitter.OBJECTS_FIELD, items);
		header.put(PimExportSplitter.OBJECT_COUNT_FIELD, items.size());

		transport.write(header, items.size());
	}
}
