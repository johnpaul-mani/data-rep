package com.usbank.mcp.settlements.config;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerConfig.class);

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job job;

	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	@Scheduled(cron = "0 */1 * * * ?")
	public void scheduleByFixedRate() throws Exception {
		LOGGER.info("Batch job starting");
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("JobID", String.valueOf(System.currentTimeMillis())).toJobParameters();
		JobExecution jobExecution = jobLauncher.run(job, jobParameters);
		LOGGER.info("Batch job executed successfully\n");
		printStatistics(jobExecution);
	}

	protected static void printStatistics(JobExecution jobExecution) {
		for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
			LOGGER.info("-----------------------------------");
			LOGGER.info("STEP name: {}", stepExecution.getStepName());
			LOGGER.info("-----------------------------------");
			LOGGER.info("CommitCount: {}", stepExecution.getCommitCount());
			LOGGER.info("FilterCount: {}", stepExecution.getFilterCount());
			LOGGER.info("ProcessSkipCount: {}", stepExecution.getProcessSkipCount());
			LOGGER.info("ReadCount: {}", stepExecution.getReadCount());
			LOGGER.info("ReadSkipCount: {}", stepExecution.getReadSkipCount());
			LOGGER.info("RollbackCount: {}", stepExecution.getRollbackCount());
			LOGGER.info("SkipCount: {}", stepExecution.getSkipCount());
			LOGGER.info("WriteCount: {}", stepExecution.getWriteCount());
			LOGGER.info("WriteSkipCount: {}", stepExecution.getWriteSkipCount());
			if (stepExecution.getFailureExceptions().size() > 0) {
				LOGGER.info("Exceptions:");
				LOGGER.info("-----------------------------------");
				for (Throwable t : stepExecution.getFailureExceptions()) {
					LOGGER.info(t.getMessage());
				}
			}
			LOGGER.info("-----------------------------------");
		}
	}

}
