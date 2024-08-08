package de.philipp.data.category;

import java.util.List;

import org.springframework.stereotype.Service;

import de.philipp.data.question.Question;


@Service
public class CategoryService {
	
	private final CategoryRepository categoryRepository;
	
	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	public List<Category> findAllCategories(String stringFilter) {
		if (stringFilter == null || stringFilter.isEmpty()) {
			List<Category> all = categoryRepository.findAll();
			return all;
		} else {
			return categoryRepository.search(stringFilter);
		}
	}
	
	public Category findCategoryById(Long id) {
		return categoryRepository.findById(id).orElse(null);
	}
	
	public void deleteCategory(Category category) {
		categoryRepository.delete(category);
	}
	
	public void saveCategory(Category category) {
		if (category == null) {
			return;
		}
		categoryRepository.save(category);
	}
	
	public List<Question> getQuestions(Category category) {
		return category.getQuestions();
	}
}
