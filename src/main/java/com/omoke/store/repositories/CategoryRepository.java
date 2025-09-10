package com.omoke.store.repositories;

import com.omoke.store.product.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}
