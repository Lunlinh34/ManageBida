package com.example.Bida.Bida.Bida.Repository;

import com.example.Bida.Bida.Bida.Enum.CategoryTable;
import com.example.Bida.Bida.Bida.Model.TableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface TableRepository extends JpaRepository<TableEntity, Long> {
    List<TableEntity> findByCategoryTable(CategoryTable categoryTable);
    Page<TableEntity> findByCategoryTable(CategoryTable categoryTable, Pageable pageable);
    @Query("SELECT t FROM TableEntity t WHERE " +
           "t.categoryTable = :category AND " +
           "(LOWER(t.name) LIKE %:search% OR " +
           "CAST(t.hourlyRate AS string) LIKE %:search%)")
    List<TableEntity> findByFilters(
        CategoryTable category,
        String search
    );
    
    boolean existsByName(String name);
}
