package com.vinfast.service;

import com.vinfast.dto.InventoryDTO;
import com.vinfast.entity.Inventory;
import com.vinfast.form.CreateInventoryForm;
import com.vinfast.models.IInventoryTopModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.vinfast.repository.InventoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService implements IInventoryService {

    @Autowired
    private InventoryRepository _inventoryRepository;
    @Autowired
    private ModelMapper _modelMapper;

    @Override
    public Page<Inventory> getInventoriesByPage(Pageable pageable) {
        return _inventoryRepository.findAll(pageable);
    }

    @Override
    public boolean createAInventory(CreateInventoryForm createInventoryForm) {
        Inventory inventory = _modelMapper.map(createInventoryForm, Inventory.class);
        Inventory inventoryRes = _inventoryRepository.save(inventory);
        return inventoryRes == null ? false: true;
    }

    @Override
    public boolean updateInventoryByID(InventoryDTO inventoryDTO) {
        Inventory inventory = _inventoryRepository.findById(inventoryDTO.getId()).orElse(null);

        if (inventory == null) return false;
        System.out.println(inventory.getCapacity());
        inventory = _modelMapper.map(inventoryDTO, Inventory.class);
        _inventoryRepository.save(inventory);
        return true;
    }

    @Override
    public boolean deleteAInventoryByID(int id) {
        if (!_inventoryRepository.existsById((long)id)) return false;
        _inventoryRepository.deleteById((long)id);
        return true;
    }

    @Override
    public List<IInventoryTopModel> getTopInventory() {
        PageRequest page = PageRequest.of(0, 5);  // Lấy 5 kết quả đầu tiên
        List<IInventoryTopModel> result = _inventoryRepository.findTopInventories(page);
        return result;
    }

    @Override
    public List<InventoryDTO> getAllInventory() {
        List<Inventory> inventories = _inventoryRepository.findAll();

        return _modelMapper.map(inventories, new TypeToken<List<InventoryDTO>>(){}.getType());
    }

    @Override
    public Inventory getInventoryByID(Long id) {
        return _inventoryRepository.findById(id).get();
    }

    @Override
    public List<InventoryDTO> findInventoryIgnoreCase(String name) {
        List<Inventory> inventories = _inventoryRepository.findByNameContainingIgnoreCase(name);
        List<InventoryDTO> inventoryDTOS = _modelMapper.map(inventories, new TypeToken<List<InventoryDTO>>(){}.getType());

        return inventoryDTOS;
    }

    /*
    @Override
    public List<InventoryTopModel> getTopInventory() {
        System.out.println(_inventoryRepository.getTopInventory().size());
        return _inventoryRepository.getTopInventory();
    }
    */

}
