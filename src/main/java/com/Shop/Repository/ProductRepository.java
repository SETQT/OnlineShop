package com.Shop.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Shop.DTO.CateDTO;
import com.Shop.Model.Category;
import com.Shop.Model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
//	List<Product> findByCategoryId(Long categoryId);

//	@Query(value = "SELECT e FROM Product e WHERE e.Category.id =?1 offset ?2 limit ?3", nativeQuery = true)
//	List<Product> findByCategoryId(Long id);
	Page<Product> findByCategoryId(Long id, Pageable pageable);

	// Các phương thức khác liên quan đến ProductService
	// Lấy 5 sản phẩm có (numberInit - amount) lớn nhất
	@Query(value = "SELECT p FROM Product p ORDER BY (p.numberInit - p.amount) DESC")
	List<Product> findTop5ProductsByNumberInitMinusAmount();

	// Lấy 5 sản phẩm có amount nhỏ nhất
	@Query(value = "SELECT p FROM Product p ORDER BY p.amount ASC")
	List<Product> findTop5ProductsByAmount();

	// Tính tổng số sản phẩm
	@Query("SELECT COUNT(p) FROM Product p")
	Long getTotalProductCount();

//	@Query("SELECT p FROM Product p")
	@Query(value = "SELECT e FROM Product e offset ?1 limit ?2", nativeQuery = true)
	List<Product> findProductsByOffsetAndLimit(int offset, int limit);

	@Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<Product> findByNameContainingIgnoreCase(@Param("name") String name);

	@Query("SELECT NEW com.Shop.DTO.CateDTO(c.id, c.name,COALESCE(COUNT(p), 0)) "
			+ "FROM Category c LEFT JOIN Product p ON c.id = p.category.id GROUP BY c.id")
	List<CateDTO> getCategoriesWithCount();

	List<Product> findByCategory(Category category);
}
