package com.nishantLearning.springboottableCreation.controller;


import com.nishantLearning.springboottableCreation.exception.ResourceNotFoundException;
import com.nishantLearning.springboottableCreation.model.Item;
import com.nishantLearning.springboottableCreation.repo.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    //Get all items
    @GetMapping("/")
    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    //Get item by Id
    @GetMapping("/{id}")
    public Item getItemById(@PathVariable Long id){
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id : " + id));
    }

    //Create an item
    @PostMapping("/")
    public ResponseEntity<Item> createItem(@RequestBody Item item){
        itemRepository.save(item);
        return ResponseEntity.ok(item);
    }

    //Update an item
    @PutMapping("/")
    public ResponseEntity<Item> updateItem(@RequestBody Item itemDetails){
        Item oldItem = itemRepository.findById(itemDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id : " + itemDetails.getId()));

        oldItem.setItemName(itemDetails.getItemName());
        oldItem.setCart(itemDetails.getCart());
        Item newItem = itemRepository.save(oldItem);
        return ResponseEntity.ok(newItem);
    }

    //Delete an item
    @DeleteMapping("/{id}")
    public ResponseEntity<Item> deleteItem(@PathVariable Long id){
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id : " + id));

        itemRepository.delete(item);
        return ResponseEntity.ok(item);
    }

}
