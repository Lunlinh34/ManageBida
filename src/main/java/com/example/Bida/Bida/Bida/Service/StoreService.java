package com.example.Bida.Bida.Bida.Service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.Bida.Bida.Bida.Model.StoreEntity;
import com.example.Bida.Bida.Bida.Repository.StoreRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StoreService {

    private final String UPLOAD_DIR = "uploads";

    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public List<StoreEntity> findAll() {
        return storeRepository.findAll();
    }

    public void createStore(StoreEntity store, MultipartFile imageFile) throws IOException {
        validateStore(store);

        if (storeRepository.existsByName(store.getName())) {
            throw new IllegalArgumentException("Tên cửa hàng đã tồn tại. Vui lòng chọn tên khác.");
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = saveImage(imageFile);
            store.setImage("/uploads/" + fileName);
        }
        storeRepository.save(store);
    }

    private void validateStore(StoreEntity store) {
        if (store.getName() == null || store.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên cửa hàng không được để trống");
        }
        
        if (store.getPhone() == null || store.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        }

        if (store.getEmail() == null || store.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống");
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

    public void updateStore(Long id, StoreEntity store) {
        StoreEntity existingStore = storeRepository.findById(id).orElse(null);
        if (existingStore != null) {
            existingStore.setName(store.getName());
            existingStore.setAddress(store.getAddress());
            existingStore.setPhone(store.getPhone());
            existingStore.setEmail(store.getEmail());
            storeRepository.save(existingStore);
        }

    }

    public void deleteStore(Long id) {
        storeRepository.deleteById(id);
    }

    public StoreEntity findById(Long id) {
        return storeRepository.findById(id).orElse(null);
    }

    public List<StoreEntity> searchStores(String search) {
        return storeRepository.findByNameContainingIgnoreCase(search);
    }

}
