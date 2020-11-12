package com.techgroup.departments;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.mapping.BeanWrapperRowMapper;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import com.techgroup.departments.dto.StudentDTO;

@Configuration
public class BatchConfig {
	private static final String PROPERTY_EXCEL_SOURCE_FILE_PATH = "excel.to.database.job.source.file.path";

	@Bean
	ItemReader<StudentDTO> excelStudentReader(Environment environment) {
		PoiItemReader<StudentDTO> reader = new PoiItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new ClassPathResource(environment.getRequiredProperty(PROPERTY_EXCEL_SOURCE_FILE_PATH)));
		reader.setRowMapper(excelRowMapper());
		return reader;
	}

	private RowMapper<StudentDTO> excelRowMapper() {

		return new StudentExcelRowMapper();
	}

	/**
	 * If your Excel document has no header, you have to create a custom row mapper
	 * and configure it here.
	 */
	/*
	 * private RowMapper<StudentDTO> excelRowMapper() { return new
	 * StudentExcelRowMapper(); }
	 */

	@Bean
	ItemProcessor<StudentDTO, StudentDTO> excelStudentProcessor() {
		return new DepartmentsProcessor();
	}

	@Bean
	@StepScope
	ItemStreamWriter<StudentDTO> excelStudentWriter() {
		return new DepartmentsExcelWriter();
	}

	@Bean
	@StepScope
	ItemStreamWriter<StudentDTO> excelDatabaseWriter() {
		return new DepartmentsDatabaseWriter();
	}

	@Bean
	Step excelFileToDatabaseStep(ItemReader<StudentDTO> excelStudentReader,
			ItemProcessor<StudentDTO, StudentDTO> excelStudentProcessor, ItemWriter<StudentDTO> excelStudentWriter,
			StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("excelFileToDatabaseStep").<StudentDTO, StudentDTO>chunk(100)
				.reader(excelStudentReader).processor(excelStudentProcessor)
				.writer(classifierCompositeItemWriter(excelDatabaseWriter(), excelStudentWriter()))
				.stream(excelDatabaseWriter()).stream(excelStudentWriter()).build();
	}

	@Bean
	public ClassifierCompositeItemWriter<StudentDTO> classifierCompositeItemWriter(ItemWriter<StudentDTO> dataBaseWriter,
			ItemWriter<StudentDTO> excelWriter) {
		ClassifierCompositeItemWriter<StudentDTO> classifierCompositeItemWriter = new ClassifierCompositeItemWriter<>();
		classifierCompositeItemWriter.setClassifier((Classifier<StudentDTO, ItemWriter<? super StudentDTO>>) person -> {
			System.out.println("person " + person);
			if (person.getValidationStatus() != null && person.getValidationStatus().equalsIgnoreCase("Failed")) {
				System.out.println("yes failed");
				return excelWriter;
			} else {
				return dataBaseWriter;
			}
		});
		return classifierCompositeItemWriter;
	}

	@Bean
	Job excelFileToDatabaseJob(JobBuilderFactory jobBuilderFactory,
			@Qualifier("excelFileToDatabaseStep") Step excelFileToDatabaseStep) {
		return jobBuilderFactory.get("excelFileToDatabaseJob").incrementer(new RunIdIncrementer())
				.flow(excelFileToDatabaseStep).end().build();
	}
}
