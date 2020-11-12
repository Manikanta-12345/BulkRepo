package com.techgroup.departments;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;

import com.techgroup.departments.dto.StudentDTO;

public class DepartmentsDatabaseWriter implements ItemStreamWriter<StudentDTO> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentsExcelWriter.class);

	@Override
	public void write(List<? extends StudentDTO> items) throws Exception {
		LOGGER.info("Received the information of {} students DataBaseWriter", items.size());

		items.forEach(i -> LOGGER.debug("Received the information of a student: {}", i));
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		// TODO Auto-generated method stub

	}
	@Override
	public void close() throws ItemStreamException {
		// TODO Auto-generated method stub
		
	}
}
