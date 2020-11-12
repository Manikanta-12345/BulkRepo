package com.techgroup.departments;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.techgroup.departments.dto.StudentDTO;

public class DepartmentsProcessor implements ItemProcessor<StudentDTO, StudentDTO> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentsProcessor.class);

	@Override
	public StudentDTO process(StudentDTO item) throws Exception {
		LOGGER.info("Processing student information: {}", item);
		item.setValidationStatus("Success");
		if (item.getName().matches(".*\\d.*")) {
			System.out.println("yes failed process");
			item.setValidationStatus("Failed");
			String remarks = item.getRemarks();// student name invalid,gender invalid
			item.setRemarks(remarks == null ? "Student Name Invalid " : remarks + " ," + "Student Name Invalid");
		}
		if (item.getGender().matches(".*\\d.*")) {
			System.out.println("yes failed process");
			item.setValidationStatus("Failed");
			String remarks = item.getRemarks();// student name invalid,gender invalid
			item.setRemarks(remarks == null ? " Invalid Gender" : remarks + " ," + "Invalid Gender");
		}

		return item;
	}
}
