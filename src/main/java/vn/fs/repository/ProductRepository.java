package vn.fs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.fs.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	// Liệt kê sản phẩm theo danh mục
	@Query(value = "SELECT * FROM products WHERE category_id = ?", nativeQuery = true)
	public List<Product> listProductByCategory(Long categoryId);

	// 10 sản phẩm hàng đầu theo danh mục
	@Query(value = "SELECT * FROM products AS b WHERE b.category_id = ?;", nativeQuery = true)
	List<Product> listProductByCategory10(Long categoryId);
	
	// Liệt kê sản phẩm mới
	@Query(value = "SELECT * FROM products ORDER BY entered_date DESC limit 20;", nativeQuery = true)
	public List<Product> listProductNew20();
	
	// Search Product
	@Query(value = "SELECT * FROM products WHERE product_name LIKE %?1%" , nativeQuery = true)
	public List<Product> searchProduct(String productName);
	
	//đếm số lượng theo sản phẩm
	@Query(value = "SELECT c.category_id,c.category_name,\r\n"
			+ "COUNT(*) AS SoLuong\r\n"
			+ "FROM products p\r\n"
			+ "JOIN categories c ON p.category_id = c.category_id\r\n"
			+ "GROUP BY c.category_name;" , nativeQuery = true)
	List<Object[]> listCategoryByProductName();
	
	// 20 sản phẩm bán chạy nhất
	@Query(value = "SELECT p.product_id,\r\n"
			+ "COUNT(*) AS SoLuong\r\n"
			+ "FROM order_details p\r\n"
			+ "JOIN products c ON p.product_id = c.product_id\r\n"
			+ "GROUP BY p.product_id\r\n"
			+ "ORDER by SoLuong DESC limit 20;", nativeQuery = true)
	public List<Object[]> bestSaleProduct20();
	
	@Query(value = "select * from products o where product_id in :ids", nativeQuery = true)
	List<Product> findByInventoryIds(@Param("ids") List<Integer> listProductId);

	@Query(value = "select * from products p where p.product_id =?1 ", nativeQuery = true)
	Product findByProductId(Long id);

}
