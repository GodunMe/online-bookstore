package fa.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fa.training.model.Category;
import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	public Optional<Category> findByCategoryName(String categoryName);
	
	public List<Category> findAllByOrderByCategoryNameAsc();
}
