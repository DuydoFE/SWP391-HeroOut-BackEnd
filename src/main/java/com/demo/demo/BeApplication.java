package com.demo.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "EvenAPI", version = "1.0", description = "Information"))
@SecurityScheme(name = "api", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class BeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeApplication.class, args);
	}

	// API

	// Method
	// URL

	// GET: lấy dữ liệu
	// POST: create new
	// PUT: update
	// DELETE: xoá

	// Quản lý sinh viên
	// 1. lấy danh sách sinh viên
	// => GET /api/student

	// 2. tạo 1 sinh viên mới
	// => POST /api/student

	// 3. update thông tin sinh viên
	// => PUT /api/student/id

	// 4. Delete 1 thằng sinh viên nào đó
	// => DELETE /api/student/id

}
