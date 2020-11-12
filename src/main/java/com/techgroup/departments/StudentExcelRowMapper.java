package com.techgroup.departments;

import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.support.rowset.RowSet;

import com.techgroup.departments.dto.StudentDTO;

public class StudentExcelRowMapper implements RowMapper<StudentDTO> {

	@Override
	public StudentDTO mapRow(RowSet rowSet) throws Exception {
		StudentDTO student = new StudentDTO();
		student.setId(rowSet.getColumnValue(0));
		student.setName(rowSet.getColumnValue(1));
		student.setGender(rowSet.getColumnValue(2));
		student.setEmail(rowSet.getColumnValue(3));
		student.setMobile(rowSet.getColumnValue(4));
		student.setRefNo(rowSet.getCurrentRowIndex() + "");
		return student;
	}
}