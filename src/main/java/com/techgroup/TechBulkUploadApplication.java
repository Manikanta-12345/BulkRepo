package com.techgroup;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.techgroup.departments.ExcelFileToDatabaseJobLauncher;

@SpringBootApplication
@EnableBatchProcessing
public class TechBulkUploadApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(TechBulkUploadApplication.class, args);
		ExcelFileToDatabaseJobLauncher launcher = context.getBean(ExcelFileToDatabaseJobLauncher.class);
		try {
			launcher.launchXmlFileToDatabaseJob();
		} catch (Exception e) {

		}
	}

}
