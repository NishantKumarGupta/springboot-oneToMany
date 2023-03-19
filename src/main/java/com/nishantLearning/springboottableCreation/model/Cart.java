package com.nishantLearning.springboottableCreation.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.io.Serializable;

import java.util.Set;

@Entity
@Table(name = "cart")
public class Cart implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_id")
    private Long id;

    @Column(name = "user_name")
    private String userName;
    @JsonManagedReference
    @JsonIgnore
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Item> items;


    //Constructors
    public Cart() {
    }

    public Cart(Long id, String userName, Set<Item> items) {
        this.id = id;
        this.userName = userName;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", items=" + items +
                '}';
    }
}


