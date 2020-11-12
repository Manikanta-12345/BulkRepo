package com.techgroup.departments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.beans.factory.annotation.Value;

import com.techgroup.departments.dto.StudentDTO;

public class DepartmentsExcelWriter implements ItemStreamWriter<StudentDTO> {
	@Value("#{jobParameters['file']}")
	private String fileName;
	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentsExcelWriter.class);

	@Override
	public void write(List<? extends StudentDTO> items) throws Exception {
		System.out.println("file name" + fileName);
		LOGGER.info("Received the information  of Excel Writer {} students", items.size());

		items.forEach(i -> LOGGER.debug("Received the information of a student: {}", i));
		updateExcel(fileName, items);
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

	public void updateExcel(String fileName, List<? extends StudentDTO> items) {
		System.out.println("excel creating....");
		String excelFilePath = "JavaBooks.xls";

		try {
			FileInputStream inputStream = new FileInputStream(new File(fileName));
			Workbook workbook = WorkbookFactory.create(inputStream);

			Sheet sheet = workbook.getSheetAt(0);
			int rowCount = sheet.getLastRowNum();
			System.out.println("get last row " + rowCount);
			if (rowCount > 0) {
				rowCount = sheet.getLastRowNum();
			} else {
				rowCount = 1;
			}

			for (StudentDTO employee : items) {
				Row row = sheet.createRow(rowCount++);

				row.createCell(0).setCellValue(employee.getId());

				row.createCell(1).setCellValue(employee.getName());
				row.createCell(2).setCellValue(employee.getGender());
				row.createCell(3).setCellValue(employee.getEmail());
				row.createCell(4).setCellValue(employee.getMobile());
				row.createCell(5).setCellValue(employee.getValidationStatus());
				row.createCell(6).setCellValue(employee.getRemarks());
				row.createCell(7).setCellValue(Integer.parseInt(employee.getRefNo())+1);

			}

			inputStream.close();
			FileOutputStream outputStream = new FileOutputStream(new File(fileName));
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();

		} catch (IOException | EncryptedDocumentException | InvalidFormatException ex) {
			ex.printStackTrace();
		}
	}
}
