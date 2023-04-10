package com.nishantLearning.springboottableCreation.controller;

import com.nishantLearning.springboottableCreation.exception.ResourceNotFoundException;
import com.nishantLearning.springboottableCreation.model.Cart;
import com.nishantLearning.springboottableCreation.model.Item;
import com.nishantLearning.springboottableCreation.repo.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CartRepository cartRepository;

    //GetAll
    @Cacheable(value = "cart")
    @GetMapping("/")
    public List<Cart> getAllCarts(){
        LOGGER.debug("This is a debug message from list all carts!");
        return cartRepository.findAll();
    }

    //Get by Id
    @Cacheable(value = "getCartById", key="#p0")
    @GetMapping("/{Id}")
    public Cart getCartById(@PathVariable Long Id){
        try{
            Thread.sleep(10000);
        }catch(InterruptedException e){
            System.out.println(e);
        }
        return cartRepository.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with Id" + Id));
    }

    //Create a cart
    @PostMapping("/")
    public void createCart(@RequestBody Cart cart){
        cartRepository.save(cart);
    }

    //Update a cart
    @CachePut(value ="getCartById")
    @PutMapping("/")
    public ResponseEntity<Cart> updateCart(@RequestBody Cart cart){
        Cart oldCart = cartRepository.findById(cart.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart not exist with id :" + cart.getId()));
        oldCart.setUserName(cart.getUserName());
        oldCart.setItems(cart.getItems());
        Cart updatedCart = cartRepository.save(oldCart);
        return ResponseEntity.ok(updatedCart);

    }

    //Delete a cart
    @CacheEvict(value = "getCartById", allEntries = false)
    @DeleteMapping("/{Id}")
    public ResponseEntity<Cart> deleteCart(@PathVariable Long Id){
        Cart cart = cartRepository.findById(Id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id :" + Id));
        cartRepository.delete(cart);
        return ResponseEntity.ok(cart);
    }

    //Cart with items
    @GetMapping("/savecartitems")
    public ResponseEntity<Cart> saveCartItems(){
        Cart cart = new Cart();
        cart.setUserName("Rudra");

        Item item1 = new Item();
        item1.setItemName("Gaming subsystem");
        item1.setCart(cart);

        Item item2 = new Item();
        item2.setItemName("Gaming mouse");
        item2.setCart(cart);

        Set<Item> itemCart = new HashSet();
        itemCart.add(item1);
        itemCart.add(item2);

        cart.setItems(itemCart);

        cartRepository.save(cart);
        return ResponseEntity.ok(cart);

    }

    @GetMapping("/cachedcheck")
    public String cachedItems(){
        return "Hello please cache this";
    }
}
