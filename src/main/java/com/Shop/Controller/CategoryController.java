package com.Shop.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Shop.DTO.CateDTO;
import com.Shop.DTO.ResponseObject;
import com.Shop.Model.Category;
import com.Shop.Service.CategotyService.CategoryService;
import com.Shop.Service.ProductService.ProductService;

@RestController
@RequestMapping("/cate")
public class CategoryController {

	@Autowired
	CategoryService cateService;
	@Autowired
	ProductService productService;

	@GetMapping("/getAll")
	public ResponseEntity<ResponseObject> getAllProducts() {
		List<CateDTO> cate = null;
		try {
			cate = productService.findCateInfo();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Danh sách sản phẩm", cate));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("failed", "không có sản phẩm", cate));
		}
	}

	@GetMapping("/cate/{categoryId}")
	public ResponseEntity<ResponseObject> getCategoryById(@PathVariable Long categoryId) {
		try {
			Optional<Category> category = cateService.findById(categoryId);
			if (category.isPresent()) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("ok", "Thông tin sản phẩm", category.get()));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject("failed", "Không tìm thấy sản phẩm", null));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("failed", "Lấy thông tin sản phẩm thất bại", null));
		}
	}

	// Thêm mới một Category
	@PostMapping("/add")
	public ResponseEntity<ResponseObject> addCategory(@RequestParam String name) {
		try {
			Category newC = new Category(name);
			Category savedCategory = cateService.save(newC);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ResponseObject("ok", "Thêm sản phẩm thành công", savedCategory));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("failed", "Thêm sản phẩm thất bại", null));
		}
	}

	// Sửa thông tin một Category
	@PutMapping("/update")
	public ResponseEntity<ResponseObject> updateCategory(@RequestParam Long categoryId, @RequestParam String name) {
		try {
			Optional<Category> existingCategory = cateService.findById(categoryId);
			if (existingCategory.isPresent()) {
				Category cate = existingCategory.get();
				cate.setName(name);
				Category savedCategory = cateService.save(cate);
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("ok", "Cập nhật sản phẩm thành công", savedCategory));
			} else {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("failed", "Không tìm thấy sản phẩm", null));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("failed", "Cập nhật sản phẩm thất bại", null));
		}
	}

	// Xóa một Category
	@PostMapping("/delete/{categoryId}")
	public ResponseEntity<ResponseObject> deleteCategory(@PathVariable Long categoryId) {
		try {
			cateService.deleteCategory(categoryId);
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Xóa sản phẩm thành công", null));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("failed", "Xóa sản phẩm thất bại", null));
		}
	}

}
