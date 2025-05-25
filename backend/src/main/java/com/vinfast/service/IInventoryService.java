package com.vinfast.service;

import com.vinfast.dto.InventoryDTO;
import com.vinfast.entity.Inventory;
import com.vinfast.form.CreateInventoryForm;
import com.vinfast.models.IInventoryTopModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IInventoryService {
    Page<Inventory>  getInventoriesByPage (Pageable pageable);
    boolean createAInventory(CreateInventoryForm createInventoryForm);
    boolean updateInventoryByID(InventoryDTO inventoryDTO);
    boolean deleteAInventoryByID(int id);
    List<IInventoryTopModel> getTopInventory();

    List<InventoryDTO> getAllInventory();

    Inventory getInventoryByID(Long id);

    List<InventoryDTO> findInventoryIgnoreCase(String name);
}
