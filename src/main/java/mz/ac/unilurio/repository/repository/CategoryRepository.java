package mz.ac.unilurio.repository.repository;

import mz.ac.unilurio.repository.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer>{
}
