package com.Shop.Controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Shop.DTO.ResponseObject;
import com.Shop.Service.DataService.DataServiceImpl;

@RestController
@RequestMapping("/backup")
public class BackupController {

	private final DataServiceImpl dataService;

	public BackupController(DataServiceImpl dataService) {
		this.dataService = dataService;
	}

	@GetMapping("/export")
	public ResponseEntity<Resource> downloadExcelBackup() throws IOException {
		dataService.createExcelBackup();

		// Tạo Resource từ file Excel
		Path path = Paths.get("backup.xlsx");
		Resource resource = (Resource) new UrlResource(path.toUri());

		// Trả về response để client có thể download file Excel
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	@PostMapping("/import")
	public ResponseEntity<Object> importData(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("failed", "bạn cần gửi file", null));

		}

		try {
			dataService.importExcelData(file.getInputStream());
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "import thành công", null));
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK).body("Lỗi trong quá trình nhập dữ liệu từ file.");
		}
	}
}
