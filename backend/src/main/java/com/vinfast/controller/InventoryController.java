package com.vinfast.controller;

import com.vinfast.dto.InventoryDTO;
import com.vinfast.entity.Inventory;
import com.vinfast.form.CreateInventoryForm;
import com.vinfast.models.IInventoryTopModel;
import com.vinfast.service.IInventoryService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/inventories")
public class InventoryController {
    @Autowired
    private IInventoryService _inventoryService;
    @Autowired
    private ModelMapper _modelMapper;

    @GetMapping
    public ResponseEntity<List<InventoryDTO>> getAllInventory() {
        List<InventoryDTO> dtos = _inventoryService.getAllInventory();
        return dtos != null ? ResponseEntity.ok(dtos) : ResponseEntity.notFound().build();
    }

    @GetMapping("/page")
    public ResponseEntity<Page<InventoryDTO>> getInventoriesByPage(Pageable pageable) {
        Page<Inventory> pageInventory = _inventoryService.getInventoriesByPage(pageable);
        List<InventoryDTO> inventoryDTOS = _modelMapper.map(pageInventory.getContent(), new TypeToken<List<InventoryDTO>>() {}.getType());
        Page<InventoryDTO> pageInventoryDTOS = new PageImpl<>(inventoryDTOS, pageable, pageInventory.getTotalElements());
        return ResponseEntity.ok(pageInventoryDTOS);
    }

    @PostMapping
    public ResponseEntity<?> createAInventory(@RequestBody CreateInventoryForm createInventoryForm) {
        boolean isCreate = _inventoryService.createAInventory(createInventoryForm);
        if (isCreate) {
            return ResponseEntity.status(200).body("Create OK!");
        }
        return  ResponseEntity.status(500).build();
    }

    @PutMapping
    public ResponseEntity<?> updateInventoryById( @RequestBody InventoryDTO inventoryDTO) {
        System.out.println(inventoryDTO.getCapacity());
        boolean isUpdate = _inventoryService.updateInventoryByID(inventoryDTO);
        return isUpdate ? ResponseEntity.status(200).build() : ResponseEntity.status(500).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAInventory(@PathVariable(name = "id") int id) {
        boolean isDelete = _inventoryService.deleteAInventoryByID(id);
        if (!isDelete) {
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.status(200).body("Delete success");
    }

    @GetMapping("/top")
    public ResponseEntity<List<IInventoryTopModel>> getTopInventory(){
        List<IInventoryTopModel> result = _inventoryService.getTopInventory();
        return ResponseEntity.status(200).body(result);
    }


}
