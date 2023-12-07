package com.Shop.Controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Shop.DTO.OrderDTO;
import com.Shop.DTO.ResponseObject;
import com.Shop.Model.Client;
import com.Shop.Model.Order;
import com.Shop.Model.OrderItem;
import com.Shop.Service.Client.ClientService;
import com.Shop.Service.OrderService.OrderService;
import com.Shop.Service.ProductService.ProductService;
import com.Shop.Utils.OrderHelper;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	ProductService productServic;
	private final OrderService orderService;
	private final ClientService clientService;

	@Autowired
	public OrderController(OrderService orderService, ClientService clientService) {
		this.orderService = orderService;
		this.clientService = clientService;
	}

	@PostMapping("/add")
	public ResponseEntity<Object> addOrder(@RequestBody OrderDTO orderDTO, @RequestParam String address,
			@RequestParam String name, @RequestParam String email

	) {
		try {
			Order savedOrder = new Order();
			OrderHelper hper = new OrderHelper(productServic);
			Client client = clientService.findByPhoneNumber(orderDTO.getPhone());
			if (client == null) {
				client = Client.builder().address(address).email(email).name(name).phoneNumber(orderDTO.getPhone())
						.build();

				clientService.save(client);
			}
			List<OrderItem> orderItems = hper.convertDTOToOrderItemList(orderDTO);
			savedOrder.setOrderItems(orderItems);
			savedOrder.setClient(client);
			ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
			LocalDateTime vietnamTime = LocalDateTime.now(zoneId);
			// Sau đó, đặt thời gian đơn hàng
			savedOrder.setOrderDate(vietnamTime);
			savedOrder.calculateTotal();
			orderService.save(savedOrder);

			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("success", "Đã thêm đơn hàng", savedOrder));

		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject("failed", "Không thể thêm đơn hàng", e.getMessage()));
		}

	}

	@GetMapping("/search")
	public ResponseEntity<Object> getOrdersBetweenDates(
			@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

		List<Order> orders = orderService.findOrdersBetweenDates(startDate, endDate);

		if (orders.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("failed", "Không có đơn hàng này", null));

		}

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Đơn hàng của bạn", orders));

	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getOne(@PathVariable Long id) {
		try {
			Optional<Order> saved = orderService.findById(id);
			if (saved.isEmpty())
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("success", "Không có đơn hàng này", null));
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("success", "Đơn hàng của bạn", saved.get()));

		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("failed", "Đơn hàng của bạn", null));
		}

	}

	@PostMapping("/update/{id}")
	public ResponseEntity<Object> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO,
			@RequestParam String address, @RequestParam String name, @RequestParam String email

	) {
		try {
			Optional<Order> saved = orderService.findById(id);
			if (saved.isEmpty())
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("success", "Không có đơn hàng này", null));

			Order savedOrder = new Order();

			OrderHelper hper = new OrderHelper(productServic);
			Client client = clientService.findByPhoneNumber(orderDTO.getPhone());
			if (client == null) {
				client = Client.builder().address(address).email(email).name(name).phoneNumber(orderDTO.getPhone())
						.build();

				clientService.save(client);
			}
			List<OrderItem> orderItems = hper.convertDTOToOrderItemList(orderDTO);
			savedOrder.setOrderItems(orderItems);
			savedOrder.setClient(client);
			savedOrder.calculateTotal();
			// Lấy múi giờ của Việt Nam
			ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
			LocalDateTime vietnamTime = LocalDateTime.now(zoneId);
			// Sau đó, đặt thời gian đơn hàng
			savedOrder.setOrderDate(vietnamTime);

			orderService.delete(id);
			orderService.save(savedOrder);

			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("success", "Đã cập nhật đơn hàng", savedOrder));

		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseObject("failed", "Không thể cập nhật đơn hàng", e.getMessage()));
		}

	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<Object> deleteOrder(@PathVariable Long id) {
		try {
			orderService.delete(id);
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Đã xóa đơn hàng", null));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("failed", "Không tìm thấy đơn hàng để xóa", null));
		}

	}

	@GetMapping("/getByPage")
	public ResponseEntity<Object> getOrdersByPage(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {

		try {
//			List<Product> productPage = productService.findAll(PageRequest.of(1, 2));
//			List<Order> productPage = orderService.findProductsByPage(page - 1, size, sortBy);
			Map<String, Object> productPage = orderService.findProductsWithPaginationDetails(page - 1, size, sortBy);
			Map<String, Object> response = new HashMap<>();
			if (!productPage.isEmpty()) {
				response.put("message", "Success");
				response.put("data", productPage); // yourData là dữ liệu của bạn

				return new ResponseEntity<>(response, HttpStatus.OK);
//				return ResponseEntity.status(HttpStatus.OK)
//						.body(new ResponseObject("ok", "Danh sách sản phẩm theo trang", productPage));
			} else {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseObject("failed", "Không có sản phẩm", null));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseObject("failed", "Lấy danh sách sản phẩm thất bại", null));
		}
	}

	@GetMapping("/getAll")
	public ResponseEntity<Object> getAllOrders() {
		List<Order> orders = null;
		try {
			orders = orderService.findAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!orders.isEmpty()) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("success", "Danh sách đơn hàng", orders));
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("failed", "Không có đơn hàng", null));
		}
	}
}
