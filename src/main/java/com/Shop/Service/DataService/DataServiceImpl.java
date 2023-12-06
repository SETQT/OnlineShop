package com.Shop.Service.DataService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Shop.Model.Category;
import com.Shop.Model.Client;
import com.Shop.Model.Order;
import com.Shop.Model.OrderItem;
import com.Shop.Model.Product;
import com.Shop.Repository.CategoryRepository;
import com.Shop.Repository.ClientRepository;
import com.Shop.Repository.OrderItemRepository;
import com.Shop.Repository.OrderRepository;
import com.Shop.Repository.ProductRepository;

@Service
public class DataServiceImpl implements DataService {
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	public void createExcelBackup() throws IOException {
		Workbook workbook = new XSSFWorkbook();

		createCategorySheet(workbook);
		createClientSheet(workbook);
		createOrderItemSheet(workbook);
		createProductSheet(workbook);
		createOrderSheet(workbook);
		FileOutputStream fileOut = new FileOutputStream("backup.xlsx");
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
	}

	private void createOrderSheet(Workbook workbook) {
		Sheet sheet = workbook.createSheet("Orders");
		List<Order> orders = orderRepository.findAll();

		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("ID");
		headerRow.createCell(1).setCellValue("Client ID");
		headerRow.createCell(2).setCellValue("Total Amount");
		headerRow.createCell(3).setCellValue("Order Date");

		int rowNum = 1;
		for (Order order : orders) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(order.getId());
			row.createCell(1).setCellValue(order.getClient().getId());
			row.createCell(2).setCellValue(order.getTotal());
			row.createCell(3).setCellValue(order.getOrderDate().toString());
			// Các cột khác của Order nếu cần
		}
	}

	private void createCategorySheet(Workbook workbook) {
		Sheet sheet = workbook.createSheet("Categories");
		List<Category> categories = categoryRepository.findAll();

		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("ID");
		headerRow.createCell(1).setCellValue("Name");

		int rowNum = 1;
		for (Category category : categories) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(category.getId());
			row.createCell(1).setCellValue(category.getName());
		}
	}

	private void createClientSheet(Workbook workbook) {
		Sheet sheet = workbook.createSheet("Clients");
		List<Client> clients = clientRepository.findAll();

		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("ID");
		headerRow.createCell(1).setCellValue("Name");
		headerRow.createCell(2).setCellValue("Address");
		headerRow.createCell(3).setCellValue("Phone Number");
		headerRow.createCell(4).setCellValue("Email");

		int rowNum = 1;
		for (Client client : clients) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(client.getId());
			row.createCell(1).setCellValue(client.getName());
			row.createCell(2).setCellValue(client.getAddress());
			row.createCell(3).setCellValue(client.getPhoneNumber());
			row.createCell(4).setCellValue(client.getEmail());
		}
	}

	private void createOrderItemSheet(Workbook workbook) {
		Sheet sheet = workbook.createSheet("OrderItems");
		List<OrderItem> orderItems = orderItemRepository.findAll();

		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("ID");
		headerRow.createCell(1).setCellValue("Order ID");
		headerRow.createCell(2).setCellValue("Product ID");
		headerRow.createCell(3).setCellValue("Quantity");
		headerRow.createCell(4).setCellValue("Price");
		headerRow.createCell(5).setCellValue("Profit");

		int rowNum = 1;
		for (OrderItem orderItem : orderItems) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(orderItem.getId());
			row.createCell(1).setCellValue(orderItem.getProduct().getId());
			row.createCell(2).setCellValue(orderItem.getQuantity());
			row.createCell(3).setCellValue(orderItem.getPrice());
			row.createCell(4).setCellValue(orderItem.getProfit());
		}
	}

	private void createProductSheet(Workbook workbook) {
		Sheet sheet = workbook.createSheet("Products");
		List<Product> products = productRepository.findAll();

		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("ID");
		headerRow.createCell(1).setCellValue("Name");
		headerRow.createCell(2).setCellValue("Image");
		headerRow.createCell(3).setCellValue("Price");
		headerRow.createCell(4).setCellValue("Category ID");
		headerRow.createCell(5).setCellValue("Amount");
		headerRow.createCell(6).setCellValue("Discount");
		headerRow.createCell(7).setCellValue("Profit");

		int rowNum = 1;
		for (Product product : products) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(product.getId());
			row.createCell(1).setCellValue(product.getName());
			row.createCell(2).setCellValue(product.getImage());
			row.createCell(3).setCellValue(product.getPrice());
			row.createCell(4).setCellValue(product.getCategory().getId());
			row.createCell(5).setCellValue(product.getAmount());
			row.createCell(6).setCellValue(product.getDiscount());
			row.createCell(7).setCellValue(product.getProfit());
		}
	}

	public void importExcelData(InputStream inputStream) throws IOException {
		Workbook workbook = new XSSFWorkbook(inputStream);

		importCategories(workbook);
		importProducts(workbook);
		importOrders(workbook);
		importClients(workbook);
		importOrderItems(workbook);

		workbook.close();
		inputStream.close();
	}

	private void importCategories(Workbook workbook) {
		Sheet sheet = workbook.getSheet("Categories");
		Iterator<Row> iterator = sheet.iterator();
		if (iterator.hasNext())
			iterator.next();
		while (iterator.hasNext()) {
			Row currentRow = iterator.next();
			Category category = new Category();

			Double value = currentRow.getCell(0).getNumericCellValue();
			category.setId(value.longValue());
			category.setName(currentRow.getCell(1).getStringCellValue());

			categoryRepository.save(category);
		}
	}

	private void importProducts(Workbook workbook) {
		Sheet sheet = workbook.getSheet("Products");
		Iterator<Row> iterator = sheet.iterator();
		if (iterator.hasNext())
			iterator.next();
		while (iterator.hasNext()) {
			Row currentRow = iterator.next();
			Product product = new Product();
			Double value = currentRow.getCell(0).getNumericCellValue();
			product.setId(value.longValue());
			product.setName(currentRow.getCell(1).getStringCellValue());
			product.setImage(currentRow.getCell(2).getStringCellValue());
			value = currentRow.getCell(3).getNumericCellValue();
			product.setPrice(value);
			value = currentRow.getCell(4).getNumericCellValue();
			Category cate = categoryRepository.getById(value.longValue());
			product.setCategory(cate);
			value = currentRow.getCell(5).getNumericCellValue();
			product.setAmount(value.longValue());
			value = currentRow.getCell(6).getNumericCellValue();
			product.setDiscount(value);
			value = currentRow.getCell(7).getNumericCellValue();
			product.setProfit(value);

			// Đọc các trường khác và gán giá trị vào đối tượng Product

			productRepository.save(product);
		}
	}

	private void importOrders(Workbook workbook) {
		Sheet sheet = workbook.getSheet("Orders");
		Iterator<Row> iterator = sheet.iterator();
		if (iterator.hasNext())
			iterator.next();
		while (iterator.hasNext()) {
			Row currentRow = iterator.next();
			Order order = new Order();
			Double value = currentRow.getCell(0).getNumericCellValue();
			order.setId(value.longValue());
			value = currentRow.getCell(1).getNumericCellValue();
			Client client = clientRepository.getById(value.longValue());
			order.setClient(client);
			value = currentRow.getCell(2).getNumericCellValue();
			order.setTotal(value);
			if (currentRow.getCell(3) != null && currentRow.getCell(3).getCellType() == CellType.STRING) {

				String orderDateStr = currentRow.getCell(3).getStringCellValue();

				// Chuyển đổi chuỗi thành LocalDateTime
				LocalDateTime orderDate = LocalDateTime.parse(orderDateStr);

				order.setOrderDate(orderDate);
			}

			orderRepository.save(order);
		}
	}

	public void importOrderItems(Workbook workbook) {
		Sheet sheet = workbook.getSheet("OrderItems");
		Iterator<Row> iterator = sheet.iterator();
		if (iterator.hasNext())
			iterator.next();
		while (iterator.hasNext()) {
			Row currentRow = iterator.next();
			OrderItem orderItem = new OrderItem();
			Double value = currentRow.getCell(0).getNumericCellValue();
			orderItem.setId(value.longValue());
			value = currentRow.getCell(2).getNumericCellValue();
			Product product = productRepository.getById(value.longValue());
			orderItem.setProduct(product);
			value = currentRow.getCell(3).getNumericCellValue();
			orderItem.setQuantity(value.intValue());
			value = currentRow.getCell(4).getNumericCellValue();
			orderItem.setPrice(value);
			// lượng

			value = currentRow.getCell(1).getNumericCellValue();
			Order order = orderRepository.getById(value.longValue());
			List<OrderItem> listOrder = order.getOrderItems();
			listOrder.add(orderItem);
			order.setOrderItems(listOrder);
			orderRepository.save(order);
			// Tiếp tục lấy thông tin và gán giá trị cho đối tượng OrderItem

		}
	}

	private void importClients(Workbook workbook) {
		Sheet sheet = workbook.getSheet("Clients");
		Iterator<Row> iterator = sheet.iterator();
		if (iterator.hasNext())
			iterator.next();
		while (iterator.hasNext()) {
			Row currentRow = iterator.next();
			Client client = new Client();
			Double value = currentRow.getCell(0).getNumericCellValue();
			client.setId(value.longValue());
			client.setName(currentRow.getCell(1).getStringCellValue());
			client.setAddress(currentRow.getCell(2).getStringCellValue());
			client.setPhoneNumber(currentRow.getCell(3).getStringCellValue());
			client.setEmail(currentRow.getCell(4).getStringCellValue());

			// Đọc các trường khác và gán giá trị vào đối tượng Client

			clientRepository.save(client);
		}
	}

	@Override
	public List<Category> getAllCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Client> getAllClients() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderItem> getAllOrderItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getAllProducts() {
		// TODO Auto-generated method stub
		return null;
	}
}
