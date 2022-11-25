package com.usbank.mcp.settlements.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.usbank.mcp.settlements.util.CardSettlementFileReader;
import com.usbank.mcp.settlements.util.CardSettlementProcessor;
import com.usbank.mcp.settlements.util.CardSettlementWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchConfig.class);

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	CardSettlementFileReader cardSettlementFileReader;

	@Autowired
	CardSettlementWriter cardSettlementWriter;

	@Autowired
	CardSettlementProcessor cardSettlementProcessor;

	@Bean
	public Job createJob() {
		return jobBuilderFactory.get("MyJob").incrementer(new RunIdIncrementer()).flow(createStep()).end().build();
	}

	@Bean
	public Step createStep() {
		return stepBuilderFactory.get("MyStep").<Object, Object>chunk(1).reader(reader()).processor(processor())
				.writer(writer()).build();
	}

	@Bean
	public ItemReader<Object> reader() {
		Resource[] resources = null;
		ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
		try {
			resources = patternResolver.getResources("file:./data/*.txt");
		} catch (IOException e) {
			LOGGER.error("Error getting File. {}", e);
		}

		MultiResourceItemReader<Object> reader = new MultiResourceItemReader<>();
		reader.setResources(resources);
		reader.setDelegate(cardSettlementFileReader);
		return reader;
	}

	@Bean
	public ItemProcessor<Object, Object> processor() {
		return new CardSettlementProcessor();
	}

	@Bean
	public ItemWriter<Object> writer() {
		return new CardSettlementWriter();
	}
}
