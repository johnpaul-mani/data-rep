package com.usbank.mcp.settlements.config;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
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
				.addString("JobID", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
				jobLauncher.run(job, jobParameters);
		LOGGER.info("Batch job executed successfully\n");
	}
}
