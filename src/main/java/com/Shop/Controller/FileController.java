package com.Shop.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.Shop.DTO.ResponseObject;
import com.Shop.Service.Mail.FirebaseImageService;

@Controller
@RequestMapping(path = "/FileUpload")
// @CrossOrigin(origins = "*")
public class FileController {

	@Autowired
	@Qualifier("imageService")
	private FirebaseImageService firebaseImageService;
	@Autowired
	private FirebaseImageService firebaseDocumentFileService;

	@PostMapping("/fileImage")
	public ResponseEntity<ResponseObject> uploadImageFile(@RequestParam("file") MultipartFile file) {
		try {
			if (file != null) {
				// xu li file
				firebaseImageService = new FirebaseImageService();
				// save file to Firebase
				String fileName = firebaseImageService.save(file);
				String imageUrl = firebaseImageService.getFileUrl(fileName);
				System.out.println((imageUrl));

				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("ok", "upload file successfully", imageUrl));
			}
		} catch (Exception exception) {
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
					.body(new ResponseObject("ok", exception.getMessage(), ""));
		}
		return null;
	}

}
