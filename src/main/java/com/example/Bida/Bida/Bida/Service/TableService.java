package com.example.Bida.Bida.Bida.Service;

import com.example.Bida.Bida.Bida.Enum.CategoryTable;
import com.example.Bida.Bida.Bida.Enum.StatusTable;
import com.example.Bida.Bida.Bida.Model.TableEntity;
import com.example.Bida.Bida.Bida.Repository.TableRepository;
import com.example.Bida.Bida.Bida.Utils.StatusTableUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TableService extends BaseService<TableEntity, Long> {

    private final String UPLOAD_DIR = "uploads";

    Map<String, String> dataMap = new HashMap<>();
    private final TableRepository tableRepository;

    
    public TableService(TableRepository repository, TableRepository tableRepository) {
        super(repository);
        this.tableRepository = tableRepository;
        new File(UPLOAD_DIR).mkdirs();
    }
    public List<TableEntity> TablePoor8() {
        PageRequest pageRequest = PageRequest.of(0, 8);
        return tableRepository.findByCategoryTable(CategoryTable.POOL, pageRequest).getContent();
    }
    public List<TableEntity> TableCarom8() {
        PageRequest pageRequest = PageRequest.of(0, 8);
        return tableRepository.findByCategoryTable(CategoryTable.CAROM, pageRequest).getContent();
    }
    public List<TableEntity> TableCarom() {
        return tableRepository.findByCategoryTable(CategoryTable.CAROM);
    }
    public List<TableEntity> TablePoor() {
        return tableRepository.findByCategoryTable(CategoryTable.POOL);
    }

    public List<TableEntity> searchTablesPoorCommon(String search) {
        if (search == null) search = "";
        return tableRepository.findByFilters(
            CategoryTable.POOL,
            search.toLowerCase()
        );
    }
    
    public List<TableEntity> searchTablesCaromCommon(String search) {
        if (search == null) search = "";
        return tableRepository.findByFilters(
            CategoryTable.CAROM,
            search.toLowerCase()
        );
    }

    private void validateTable(TableEntity table) {
        if (table.getName() == null || table.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên bàn không được để trống");
        }
        if (table.getHourlyRate() <= 0) {
            throw new IllegalArgumentException("Giá theo giờ phải lớn hơn 0");
        }
        if (table.getCategoryTable() == null) {
            throw new IllegalArgumentException("Vui lòng chọn loại bàn");
        }
    }

    private String saveImage(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        
        return fileName;
    }

    public void createTable(TableEntity table, MultipartFile imageFile) throws IOException {
        validateTable(table);

        if (tableRepository.existsByName(table.getName())) {
            throw new IllegalArgumentException("Tên bàn đã tồn tại. Vui lòng chọn tên khác.");
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = saveImage(imageFile);
            table.setImage("/uploads/" + fileName);
        }

        if (table.getStatus() == null) {
            table.setStatus(StatusTable.AVAILABLE);
        }

        repository.save(table);
    }

    public Map<String, Object> getStatusData() {
        List<StatusTable> statusList = Arrays.asList(StatusTable.values());
        for (StatusTable status : StatusTable.values()) {
            dataMap.put(status.name(), StatusTableUtils.statusMap.get(status));
        }
        Map<String, Object> result = new HashMap<>();
        result.put("statusList", statusList);
        result.put("statusMap", dataMap);
        return result;
    }

    public Map<String, Object> getCategoryData() {
        List<CategoryTable> categoryList = Arrays.asList(CategoryTable.values());
        for (CategoryTable category : CategoryTable.values()) {
            dataMap.put(category.name(), StatusTableUtils.categoryMap.get(category));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("categoryList", categoryList);
        result.put("categoryMap", dataMap);
        return result;
    }

    public void updateTable(Long id, TableEntity updatedTable, MultipartFile imageFile) throws IOException {
        TableEntity existingTable = findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn với id: " + id));

        existingTable.setName(updatedTable.getName());
        existingTable.setHourlyRate(updatedTable.getHourlyRate());
        existingTable.setStatus(updatedTable.getStatus());
        existingTable.setCategoryTable(updatedTable.getCategoryTable());
        existingTable.setVip(updatedTable.isVip());
        existingTable.setStore(updatedTable.getStore());

        if (imageFile != null && !imageFile.isEmpty()) {
            if (existingTable.getImage() != null) {
                deleteExistingImage(existingTable.getImage());
            }
            
            String fileName = saveImage(imageFile);
            existingTable.setImage("/uploads/" + fileName);
        }

        validateTable(existingTable);
        repository.save(existingTable);
    }

    public void deleteTable(Long id) {
        TableEntity table = findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn với id: " + id));
            
        if (table.getImage() != null) {
            deleteExistingImage(table.getImage());
        }
        
        repository.deleteById(id);
    }

    private void deleteExistingImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                Path filePath = Paths.get(UPLOAD_DIR, fileName);
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                System.err.println("Không thể xóa file ảnh: " + imagePath);
            }
        }
    }

    public void updateTableStatus(Long tableId, StatusTable newStatus) {
        TableEntity table = findById(tableId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn với id: " + tableId));

        table.setStatus(newStatus);
        repository.save(table);
    }

}
