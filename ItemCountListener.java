package com.usbank.mcp.settlements.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
public class ItemCountListener implements ChunkListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ItemCountListener.class);

	@Override
	public void beforeChunk(ChunkContext context) {
	}

	@Override
	public void afterChunk(ChunkContext context) {
		int count = context.getStepContext().getStepExecution().getReadCount();
		LOGGER.info("Total Records Processed: {}", count);
	}

	@Override
	public void afterChunkError(ChunkContext context) {
	}

}
