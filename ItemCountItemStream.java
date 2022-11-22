package com.usbank.mcp.settlements.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.stereotype.Component;

@Component
public class ItemCountItemStream implements ItemStream {

	private static final Logger LOGGER = LoggerFactory.getLogger(ItemCountItemStream.class);

	public void open(ExecutionContext executionContext) throws ItemStreamException {
	}

	public void update(ExecutionContext executionContext) throws ItemStreamException {
		Object count = executionContext.get("FlatFileItemReader.read.count");
		LOGGER.info("Total Records Processed: {}", count);
	}

	public void close() throws ItemStreamException {
	}
}