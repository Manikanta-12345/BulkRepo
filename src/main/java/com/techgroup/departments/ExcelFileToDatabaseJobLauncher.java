package com.techgroup.departments;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ExcelFileToDatabaseJobLauncher {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelFileToDatabaseJobLauncher.class);

	private final Job job;

	private final JobLauncher jobLauncher;

	@Autowired
	ExcelFileToDatabaseJobLauncher(@Qualifier("excelFileToDatabaseJob") Job job, JobLauncher jobLauncher) {
		this.job = job;
		this.jobLauncher = jobLauncher;
	}

	public void launchXmlFileToDatabaseJob() throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
			JobRestartException, JobInstanceAlreadyCompleteException {
		LOGGER.info("Starting excelFileToDatabase job");
		JobParameters jobParameters = new JobParametersBuilder().addString("file", "E:/validated/students.xlsx")
				.toJobParameters();
		jobLauncher.run(job, jobParameters);

		LOGGER.info("Stopping excelFileToDatabase job");
	}

	private JobParameters newExecution() {
		Map<String, JobParameter> parameters = new HashMap<>();

		JobParameter parameter = new JobParameter(new Date());
		JobParameter parameter1 = new JobParameter("studentFile");
		parameters.put("currentTime", parameter);
		parameters.put("file", parameter1);
		return new JobParameters(parameters);
	}
}