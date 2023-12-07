package com.Shop.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Shop.DTO.ProductDTO;
import com.Shop.DTO.ResponseObject;
import com.Shop.Model.Category;
import com.Shop.Model.Product;
import com.Shop.Service.CategotyService.CategoryService;
import com.Shop.Service.Mail.FirebaseImageService;
import com.Shop.Service.ProductService.ProductService;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "*", allowedHeaders = { "Content-Type", "Authorization" })
public class ProductController {
	@Autowired
	private FirebaseImageService firebaseImageService;
	private final ProductService productService;
	private final CategoryService cateService;

	public ProductController(ProductService productService, CategoryService cateService) {
		this.cateService = cateService;
		this.productService = productService;
	}

	@GetMapping("/getAll")
	public ResponseEntity<ResponseObject> getAllProducts() {
		List<Product> products = null;
		try {
			products = productService.findAll();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Danh sách sản phẩm", products));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("failed", "không có sản phẩm", products));
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseObject> getProductById(@PathVariable Long id) {
		Optional<Product> product = null;
		try {
			product = productService.findById(id);
			if (product.isPresent())
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Sản phẩm", product.get()));
			else
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("failed", "không có sản phẩm", null));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("failed", "không có sản phẩm", null));
		}
	}

	@PostMapping("/add")
	public ResponseEntity<ResponseObject> createProduct(@RequestBody ProductDTO productDTO) {
		Product newProduct = new Product(productDTO.getName(), productDTO.getPrice(), productDTO.getAmount(),
				productDTO.getDiscount(), productDTO.getImage(), productDTO.getProfit());
		Optional<Category> cate = java.util.Optional.empty();
		try {
			cate = cateService.findById(productDTO.getCategory());
			if (cate.isEmpty())
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("failed", "Không có loại sản phẩm này", null));
			newProduct.setCategory(cate.get());
			productService.save(newProduct);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("failed", "Sản phẩm chưa được tạo", null));
			// TODO Auto-generated catch block
		}

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Sản phẩm đã được tạo", newProduct));
	}

	@PostMapping(path = "/addFull", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseObject> createProductFull(
			@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("name") String name,
			@RequestParam("price") String price, @RequestParam("amount") String amount,
			@RequestParam("discount") String discount, @RequestParam("profit") String profit,
			@RequestParam("category") String category) {
		String imageUrl = null;
		if (file != null) {
			// xu li file
			firebaseImageService = new FirebaseImageService();
			// save file to Firebase
			String fileName;
			try {
				fileName = firebaseImageService.save(file);
				imageUrl = firebaseImageService.getFileUrl(fileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("failed", "Sản phẩm chưa được tạo, Lỗi image", null));
			}

		}
		double parsedPrice = Double.parseDouble(price);
		Long parsedAmount = Long.parseLong(amount);
		double parsedDiscount = Double.parseDouble(discount);
		double parsedProfit = Double.parseDouble(profit);
		Product newProduct = new Product(name, parsedPrice, parsedAmount, parsedDiscount, imageUrl, parsedProfit);
		Optional<Category> cate = java.util.Optional.empty();
		try {
			cate = cateService.findById(Long.parseLong(category));
			if (cate.isEmpty())
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("failed", "Không có loại sản phẩm này", null));
			newProduct.setCategory(cate.get());
			productService.save(newProduct);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("failed", "Sản phẩm chưa được tạo", null));
			// TODO Auto-generated catch block
		}

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Sản phẩm đã được tạo", newProduct));
	}

	@GetMapping("/search")
	public ResponseEntity<Object> searchProductsByName(@RequestParam String name) {
		List<Product> products = productService.findProductsByName(name);

		if (products.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("failed", "Không có loại sản phẩm này", null));
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject("ok", "Tất cả loại sản phẩm với tên này", products));

	}

	@GetMapping("/getByPage")
	public ResponseEntity<ResponseObject> getProductsByPage(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "sortBy") String sortBy) {

		try {
//			List<Product> productPage = productService.findAll(PageRequest.of(1, 2));
			List<Product> productPage = productService.findProductsByPage(page - 1, size, sortBy);

			if (!productPage.isEmpty()) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("ok", "Danh sách sản phẩm theo trang", productPage));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseObject("failed", "Không có sản phẩm", null));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("failed", "Lấy danh sách sản phẩm thất bại", null));
		}
	}

	@PostMapping("/update")
	public ResponseEntity<ResponseObject> updateProduct(@RequestBody ProductDTO productDTO) {

		Optional<Category> cate = java.util.Optional.empty();
		Optional<Product> pro = java.util.Optional.empty();
		Product oldProduct;
		try {
			cate = cateService.findById(productDTO.getCategory());
			pro = productService.findById(productDTO.getId());
			if (cate.isEmpty() || pro.isEmpty())
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("failed", "Không có loại sản phẩm này", null));

			oldProduct = pro.get();
			oldProduct.setAmount(productDTO.getAmount());
			oldProduct.setCategory(cate.get());
			oldProduct.setName(productDTO.getName());
			oldProduct.setDiscount(productDTO.getDiscount());
			oldProduct.setImage(productDTO.getImage());
			oldProduct.setPrice(productDTO.getPrice());

			System.out.println(oldProduct.toString());
			productService.save(oldProduct);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("failed", "Sản phẩm chưa được tạo", null));
			// TODO Auto-generated catch block
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject("ok", "Sản phẩm đã được cập nhật", oldProduct));
	}

	@GetMapping("/getByCategoryId")
	public ResponseEntity<Object> getProductsByCategoryId(@RequestParam Long id,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10000") int size,
			@RequestParam(defaultValue = "id") String sortBy) {
//		try {
//			List<Product> products = productService.getByCategory(id, page - 1, size, sortBy);
//
//			if (!products.isEmpty()) {
//				return ResponseEntity.status(HttpStatus.OK)
//						.body(new ResponseObject("ok", "Danh sách sản phẩm theo loại", products));
//			} else {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND)
//						.body(new ResponseObject("failed", "Không có sản phẩm thuộc loại này", null));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body(new ResponseObject("failed", "Lấy danh sách sản phẩm thất bại", null));
//		}

		Map<String, Object> productPage = productService.getByCategory(id, page - 1, size, sortBy);
		Map<String, Object> response = new HashMap<>();
		if (!productPage.isEmpty()) {
			response.put("message", "Success");
			response.put("data", productPage); // yourData là dữ liệu của bạn

			return new ResponseEntity<>(response, HttpStatus.OK);
//			return ResponseEntity.status(HttpStatus.OK)
//					.body(new ResponseObject("ok", "Danh sách sản phẩm theo trang", productPage));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseObject("failed", "Không có sản phẩm", null));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
		try {
			productService.deleteProduct(id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("failed", "lỗi", null));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok", "Sản phẩm đã bị xóa", null));
	}
}
