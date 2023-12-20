package com.Shop.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Shop.DTO.ClientDTO;
import com.Shop.Model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	@Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
	List<Order> findOrdersBetweenDates(@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);

	List<Order> findByOrderItemsId(Long orderItemId);

	@Query("SELECT new com.Shop.DTO.ClientDTO(o.client.id, o.client.name, o.client.address, o.client.phoneNumber, o.client.email, SUM(o.total)) AS money "
			+ "FROM Order o "
			+ "GROUP BY o.client.id , o.client.name, o.client.address, o.client.phoneNumber, o.client.email")
	List<ClientDTO> getClientTotalAmountUsed();

	@Query(value = "SELECT SUM(oi.profit*oi.quantity) " + "FROM \"user_order\" o "
			+ "JOIN \"user_order_order_items\" rl ON o.id = rl.order_id "
			+ "JOIN order_item oi ON rl.order_items_id = oi.id " + "JOIN product p ON oi.product_id = p.id "
			+ "WHERE o.order_date BETWEEN :start AND :end", nativeQuery = true)
	Double getTotalProfitByPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	// Tính tổng doanh thu theo khoảng thời gian
	@Query("SELECT SUM(o.total) FROM Order o WHERE o.orderDate BETWEEN :start AND :end")
	Double getTotalRevenueByPeriod(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

	// Tính tổng doanh thu theo tháng
	@Query(value = "SELECT SUM(o.total) FROM user_order o " + "WHERE EXTRACT(YEAR FROM o.order_date) = :year "
			+ "AND EXTRACT(MONTH FROM o.order_date) = :month", nativeQuery = true)
	Double getTotalRevenueByMonth(@Param("year") int year, @Param("month") int month);

	// Tính tổng doanh thu theo tuần
	@Query(value = "SELECT SUM(o.total) FROM \"user_order\" o " + "WHERE EXTRACT(YEAR FROM o.order_date)= :year "
			+ "AND EXTRACT(WEEK FROM o.order_date) = :week", nativeQuery = true)
	Double getTotalRevenueByWeek(@Param("year") int year, @Param("week") int week);

	// Tính tổng doanh thu theo năm
	@Query(value = "SELECT SUM(o.total) FROM \"user_order\" o "
			+ "WHERE EXTRACT(YEAR FROM o.order_date) = :year", nativeQuery = true)
	Double getTotalRevenueByYear(@Param("year") int year);

	// Tính tổng lợi nhuận theo tháng
	@Query(value = "SELECT SUM(oi.profit*oi.quantity) FROM \"user_order\" o "
			+ "JOIN \"user_order_order_items\" rl ON o.id = rl.order_id "
			+ "JOIN order_item oi ON rl.order_items_id = oi.id " + "JOIN product p ON oi.product_id = p.id "
			+ "WHERE EXTRACT(YEAR FROM o.order_date) = :year "
			+ "AND EXTRACT(MONTH FROM o.order_date) = :month", nativeQuery = true)
	Double getTotalProfitByMonth(@Param("year") int year, @Param("month") int month);

	// Tính tổng lợi nhuận theo năm
	@Query(value = "SELECT SUM(oi.profit*oi.quantity) FROM \"user_order\" o "
			+ "JOIN \"user_order_order_items\" rl ON o.id = rl.order_id "
			+ "JOIN order_item oi ON rl.order_items_id = oi.id " + "JOIN product p ON oi.product_id = p.id "
			+ "WHERE EXTRACT(YEAR FROM o.order_date) = :year", nativeQuery = true)
	Double getTotalProfitByYear(@Param("year") int year);

	// Tính tổng lợi nhuận theo tuần
	@Query(value = "SELECT SUM(oi.profit*oi.quantity) FROM \"user_order\" o "
			+ "JOIN \"user_order_order_items\" rl ON o.id = rl.order_id "
			+ "JOIN order_item oi ON rl.order_items_id = oi.id " + "JOIN product p ON oi.product_id = p.id "
			+ "WHERE EXTRACT(YEAR FROM o.order_date) = :year  "
			+ "AND EXTRACT(WEEK FROM o.order_date) = :week", nativeQuery = true)
	Double getTotalProfitByWeek(@Param("year") int year, @Param("week") int week);

	// -------------------------------------
	@Query(value = "SELECT p.name as productName, SUM(oi.quantity) as soldQuantity , (p.number_init - SUM(oi.quantity)) as remainingQuantity "
			+ "FROM user_order o " + "JOIN \"user_order_order_items\" rl ON o.id = rl.order_id "
			+ "JOIN order_item oi ON rl.order_items_id = oi.id " + "JOIN product p ON oi.product_id = p.id "
			+ "WHERE EXTRACT(YEAR FROM o.order_date) = :year " + "AND EXTRACT(MONTH FROM o.order_date) = :month "
			+ "GROUP BY p.name, p.number_init", nativeQuery = true)
	List<Object[]> countSoldProductsByProductAndMonth(@Param("year") int year, @Param("month") int month);

	@Query(value = "SELECT  SUM(oi.quantity) as soldQuantity  " + "FROM user_order o "
			+ "JOIN \"user_order_order_items\" rl ON o.id = rl.order_id " + "JOIN product p ON oi.product_id = p.id "
			+ "JOIN order_item oi ON rl.order_items_id = oi.id " + "WHERE EXTRACT(YEAR FROM o.order_date) = :year "
			+ "AND EXTRACT(MONTH FROM o.order_date) = :month ", nativeQuery = true)
	Long countALLSoldProductsByProductAndMonth(@Param("year") int year, @Param("month") int month);

	@Query(value = "SELECT p.name as productName, SUM(oi.quantity) as soldQuantity , (p.number_init - SUM(oi.quantity)) as remainingQuantity "
			+ "FROM user_order o " + "JOIN \"user_order_order_items\" rl ON o.id = rl.order_id "
			+ "JOIN order_item oi ON rl.order_items_id = oi.id " + "JOIN product p ON oi.product_id = p.id "
			+ "WHERE EXTRACT(YEAR FROM o.order_date) = :year " + "GROUP BY p.name, p.number_init", nativeQuery = true)
	List<Object[]> countSoldProductsByProductAndYear(@Param("year") int year);

	@Query(value = "SELECT p.name as productName, SUM(oi.quantity) as soldQuantity , (p.number_init - SUM(oi.quantity)) as remainingQuantity "
			+ "FROM user_order o " + "JOIN \"user_order_order_items\" rl ON o.id = rl.order_id "
			+ "JOIN order_item oi ON rl.order_items_id = oi.id " + "JOIN product p ON oi.product_id = p.id "
			+ "WHERE EXTRACT(YEAR FROM o.order_date) = :year " + "AND EXTRACT(WEEK FROM o.order_date) = :week "
			+ "GROUP BY p.name, p.number_init", nativeQuery = true)
	List<Object[]> countSoldProductsByProductAndYearWeek(@Param("year") int year, @Param("week") int week);

	@Query(value = "SELECT p.name as productName, SUM(oi.quantity) as soldQuantity , (p.number_init - SUM(oi.quantity)) as remainingQuantity "
			+ "FROM user_order o " + "JOIN \"user_order_order_items\" rl ON o.id = rl.order_id "
			+ "JOIN order_item oi ON rl.order_items_id = oi.id " + "JOIN product p ON oi.product_id = p.id "
			+ "WHERE o.order_date BETWEEN :dateStart AND :dateEnd"
			+ " GROUP BY p.name, p.number_init", nativeQuery = true)
	List<Object[]> countSoldProductsByProduct(@Param("dateStart") LocalDateTime dateStart,
			@Param("dateEnd") LocalDateTime dateEnd);

}