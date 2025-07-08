package fa.training.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fa.training.model.Category;
import fa.training.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<Category> getAllCategories() {
		return categoryRepository.findAllByOrderByCategoryNameAsc();
	}
	
	public Category getCategoryWithName(String categoryName) {
		Optional<Category> category = categoryRepository.findByCategoryName(categoryName);
		return category.isPresent() ? category.get() : null;
		
	}
	
	public boolean doesCategoryNameExist(String categoryName) {
		Optional<Category> category = categoryRepository.findByCategoryName(categoryName);
		return category.isPresent();
	}
	
	public void add(Category category) {
		categoryRepository.save(category);
	}
	
	public void delete(Category category) {
		categoryRepository.delete(category);
	}
	
}
