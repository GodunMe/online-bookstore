package fa.training.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fa.training.model.Category;
import fa.training.service.BookService;
import fa.training.service.CategoryService;
import fa.training.util.FormatValidation;

@RestController
public class CategoryRestController {

	@Autowired
    private BookService bookService;

	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("data/categories")
	@ResponseBody
	public ResponseEntity<List<Category>> getAllCategories() {
		List<Category> cl = categoryService.getAllCategories();
		if (cl == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(cl);
	}
	
	@GetMapping("data/categories/check-name/{categoryName}")
	@ResponseBody
	public ResponseEntity<String> isValidNewCategoryName(@PathVariable String categoryName) {
		
		if (!FormatValidation.isValidNameFormat(categoryName)) {
			return ResponseEntity.ok("Invalid category name format.");
		}
		
		if (categoryService.doesCategoryNameExist(categoryName)) {
			return ResponseEntity.ok("Duplicate category name.");
		}
		return ResponseEntity.ok("Valid");
	}
	
	@GetMapping("data/categories-with-number-of-books")
	@ResponseBody
	public ResponseEntity<Map<String, Integer>> numberOfBooks() {
		
		Map<String, Integer> categories = new HashMap<String, Integer>();
		
		List<Category> category = categoryService.getAllCategories();
		if (category == null || category.isEmpty()) {
			ResponseEntity.notFound().build();
		}
		for (Category c : category) {
			Integer count = bookService.getNumberOfBookOfCategory(c);
			categories.put(c.getCategoryName(), count);
		}
		
		return ResponseEntity.ok(categories);
	}
	
	@PostMapping("data/categories/add/{categoryName}")
	@ResponseBody
	public ResponseEntity<String> addCategory(@PathVariable String categoryName) {
		
		Category category = new Category();
		category.setCategoryName(categoryName);
		categoryService.add(category);
		
		return ResponseEntity.ok("Add successfully!");
	}
	
	@DeleteMapping("data/categories/delete/{categoryName}")
	@ResponseBody
	public ResponseEntity<String> deleteCategory(@PathVariable String categoryName) {
		
		Category category = categoryService.getCategoryWithName(categoryName);
		if (category == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found.");
		}
		
		int count = bookService.getNumberOfBookOfCategory(category);
		if (count > 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category is in used.");
		}
		
		categoryService.delete(category);
		
		return ResponseEntity.ok("Delete successfully!");
	}
	
	
}
