package com.Shop.Controller;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Shop.DTO.ResponseObject;
import com.Shop.Repository.OrderRepository;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private com.Shop.Service.ProductService.ProductService ProductService;

	@GetMapping("/dashboard")
	public ResponseEntity<Object> dashboard() {
		try {
			Object result = ProductService.getProductInformation();
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("success", "Thông số thống kê, ", result));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ResponseObject("failed", "lấy sản phẩm thất bại", null));
		}

	}

	@GetMapping("/getStatistic")
	public ResponseEntity<Object> getStatistic(@RequestParam("type") String type,
			@RequestParam(value = "dateStart", required = false) LocalDateTime dateStart,
			@RequestParam(value = "dateEnd", required = false) LocalDateTime dateEnd) {
		if (dateStart == null && dateEnd == null) {

			dateEnd = LocalDateTime.now();
			dateStart = dateEnd.minus(5, ChronoUnit.DAYS);
		}

		List<Object> result = new ArrayList<>();
		switch (type) {
		case "month":
			result = getFiveMostRecentMonths();
			break;
		case "year":
			result = getFiveMostRecentYears();
			break;
		case "week":
			result = getFiveMostRecentWeeks();
			break;
		case "date":
			result = getStatisticsByType(dateStart, dateEnd, ChronoUnit.DAYS);
			break;
		default:
			return ResponseEntity.badRequest().body("Invalid type");
		}

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Thông số thống kê, ", result));
	}

	@GetMapping("/getProductSold")
	public ResponseEntity<Object> getProductSold(@RequestParam("type") String type,
			@RequestParam(value = "dateStart", required = false) LocalDateTime dateStart,
			@RequestParam(value = "dateEnd", required = false) LocalDateTime dateEnd) {
		if (dateStart == null && dateEnd == null) {

			dateEnd = LocalDateTime.now();
			dateStart = dateEnd.minus(5, ChronoUnit.DAYS);
		}

		List<Object> result = new ArrayList<>();
		switch (type) {
		case "month":
			result = getSoldMonths();
			break;
		case "year":
			result = getSoldYears();
			break;
		case "week":
			result = getSoldWeeks();
			break;
		case "date":
			result = getSoldDate(dateStart, dateEnd);
			break;
		default:
			return ResponseEntity.badRequest().body("Invalid type");
		}

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("success", "Thông số thống kê, ", result));
	}

	private List<Object> getSoldDate(LocalDateTime dateStart, LocalDateTime dateEnd) {

		dateEnd = dateEnd.plusDays(1);
		dateStart = dateStart.minusDays(1);
		List<Object[]> soldProducts = orderRepository.countSoldProductsByProduct(dateStart, dateEnd);
		List<String> productNames = new ArrayList<>();
		List<Long> soldQuantities = new ArrayList<>();
		List<Long> remainingQuantities = new ArrayList<>();

		for (Object[] product : soldProducts) {
			String productName = (String) product[0];
			Long soldQuantity = (Long) product[1];
			Long remainingQuantity = (Long) product[2];

			productNames.add(productName);
			soldQuantities.add(soldQuantity);
			remainingQuantities.add(remainingQuantity);
		}
		List<Object> result = new ArrayList<>();
		result.add(soldQuantities);
		result.add(remainingQuantities);
		result.add(productNames);
		return result;
	}

	private List<Object> getSoldMonths() {
		LocalDateTime startDate = LocalDateTime.now();

		List<Object[]> soldProducts = orderRepository.countSoldProductsByProductAndMonth(startDate.getYear(),
				startDate.getMonthValue());
		List<String> productNames = new ArrayList<>();
		List<Long> soldQuantities = new ArrayList<>();
		List<Long> remainingQuantities = new ArrayList<>();

		for (Object[] product : soldProducts) {
			String productName = (String) product[0];
			Long soldQuantity = (Long) product[1];
			Long remainingQuantity = (Long) product[2];

			productNames.add(productName);
			soldQuantities.add(soldQuantity);
			remainingQuantities.add(remainingQuantity);
		}
		List<Object> result = new ArrayList<>();
		result.add(soldQuantities);
		result.add(remainingQuantities);
		result.add(productNames);
		return result;
	}

	private List<Object> getSoldYears() {
		LocalDateTime startDate = LocalDateTime.now();

		List<Object[]> soldProducts = orderRepository.countSoldProductsByProductAndYear(startDate.getYear());
		List<String> productNames = new ArrayList<>();
		List<Long> soldQuantities = new ArrayList<>();
		List<Long> remainingQuantities = new ArrayList<>();

		for (Object[] product : soldProducts) {
			String productName = (String) product[0];
			Long soldQuantity = (Long) product[1];
			Long remainingQuantity = (Long) product[2];

			productNames.add(productName);
			soldQuantities.add(soldQuantity);
			remainingQuantities.add(remainingQuantity);
		}
		List<Object> result = new ArrayList<>();
		result.add(soldQuantities);
		result.add(remainingQuantities);
		result.add(productNames);
		return result;
	}

	private List<Object> getSoldWeeks() {
		LocalDateTime startDate = LocalDateTime.now();

		List<Object[]> soldProducts = orderRepository.countSoldProductsByProductAndYearWeek(startDate.getYear(),
				startDate.getDayOfYear() / 7 + 1);
		List<String> productNames = new ArrayList<>();
		List<Long> soldQuantities = new ArrayList<>();
		List<Long> remainingQuantities = new ArrayList<>();

		for (Object[] product : soldProducts) {
			String productName = (String) product[0];
			Long soldQuantity = (Long) product[1];
			Long remainingQuantity = (Long) product[2];

			productNames.add(productName);
			soldQuantities.add(soldQuantity);
			remainingQuantities.add(remainingQuantity);
		}
		List<Object> result = new ArrayList<>();
		result.add(soldQuantities);
		result.add(remainingQuantities);
		result.add(productNames);
		return result;
	}

	private List<Object> getStatisticsByType(LocalDateTime dateStart, LocalDateTime dateEnd, ChronoUnit unit) {
		List<Double> profits = new ArrayList<>();
		List<Double> revenues = new ArrayList<>();
		List<String> labels = new ArrayList<>();

		long diff = unit.between(dateStart, dateEnd);

		for (int i = 0; i <= diff; i++) {
			LocalDateTime currentStart = dateEnd.minus(i, unit).withHour(0).withMinute(0).withSecond(0);
			LocalDateTime currentEnd = dateEnd.minus(i, unit).withHour(23).withMinute(59).withSecond(59);

			Double profit = orderRepository.getTotalProfitByPeriod(currentStart, currentEnd);
			Double revenue = orderRepository.getTotalRevenueByPeriod(currentStart, currentEnd);

			profits.add(0, profit != null ? profit : 0.0);
			revenues.add(0, revenue != null ? revenue : 0.0);
			labels.add(0, currentStart.toString().substring(0, 10));
		}

		List<Object> result = new ArrayList<>();
		result.add(profits);
		result.add(revenues);
		result.add(labels);

		return result;
	}

	private List<Object> getFiveMostRecentMonths() {
		List<Double> profits = new ArrayList<>();
		List<Double> revenues = new ArrayList<>();
		List<String> months = new ArrayList<>();

		LocalDateTime currentDate = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDate;
		for (int i = 0; i < 5; i++) {
			LocalDateTime currentMonthStart = currentDate.minusMonths(i).withDayOfMonth(1).withHour(0).withMinute(0)
					.withSecond(0);
			LocalDateTime currentMonthEnd = currentDate.minusMonths(i)
					.withDayOfMonth(currentDate.minusMonths(i).toLocalDate().lengthOfMonth()).withHour(23)
					.withMinute(59).withSecond(59);

			Double revenue = orderRepository.getTotalRevenueByMonth(currentMonthStart.getYear(),
					currentMonthStart.getMonthValue());
			Double profit = orderRepository.getTotalProfitByMonth(currentMonthStart.getYear(),
					currentMonthStart.getMonthValue());

			profits.add(0, profit != null ? profit : 0.0);
			revenues.add(0, revenue != null ? revenue : 0.0);
			months.add(0, "Tháng " + currentMonthStart.getMonthValue());
		}

		List<Object> result = new ArrayList<>();
		result.add(profits);
		result.add(revenues);
		result.add(months);

		return result;
	}

	private List<Object> getFiveMostRecentYears() {
		List<Double> profits = new ArrayList<>();
		List<Double> revenues = new ArrayList<>();
		List<String> years = new ArrayList<>();

		LocalDateTime currentDate = LocalDateTime.now();

		for (int i = 0; i < 5; i++) {
			LocalDateTime currentYearStart = currentDate.minusYears(i).withMonth(1).withDayOfMonth(1).withHour(0)
					.withMinute(0).withSecond(0);
			LocalDateTime currentYearEnd = currentDate.minusYears(i).withMonth(12).withDayOfMonth(31).withHour(23)
					.withMinute(59).withSecond(59);

			Double profit = orderRepository.getTotalProfitByYear(currentYearStart.getYear());
			Double revenue = orderRepository.getTotalRevenueByYear(currentYearStart.getYear());

			profits.add(0, profit != null ? profit : 0.0);
			revenues.add(0, revenue != null ? revenue : 0.0);
			years.add(0, "Năm " + currentYearStart.getYear());
		}

		List<Object> result = new ArrayList<>();
		result.add(profits);
		result.add(revenues);
		result.add(years);

		return result;
	}

	private List<Object> getFiveMostRecentWeeks() {
		List<Double> profits = new ArrayList<>();
		List<Double> revenues = new ArrayList<>();
		List<String> weeks = new ArrayList<>();

		LocalDateTime currentDate = LocalDateTime.now();

		for (int i = 0; i < 5; i++) {
			LocalDateTime currentWeekStart = currentDate.minusWeeks(i).with(DayOfWeek.MONDAY).withHour(0).withMinute(0)
					.withSecond(0);
			LocalDateTime currentWeekEnd = currentWeekStart.plusDays(6).withHour(23).withMinute(59).withSecond(59);

			Double profit = orderRepository.getTotalProfitByWeek(currentWeekStart.getYear(),
					currentWeekStart.getDayOfYear() / 7 + 1);
			Double revenue = orderRepository.getTotalRevenueByWeek(currentWeekStart.getYear(),
					currentWeekStart.getDayOfYear() / 7 + 1);

			profits.add(0, profit != null ? profit : 0.0);
			revenues.add(0, revenue != null ? revenue : 0.0);
			weeks.add(0, "Tuần " + currentWeekStart.get(WeekFields.ISO.weekOfYear()));
		}

		List<Object> result = new ArrayList<>();
		result.add(profits);
		result.add(revenues);
		result.add(weeks);

		return result;
	}
}
