package com.workintech.s18d1.controller;

import com.workintech.s18d1.dao.BurgerDao;
import com.workintech.s18d1.dao.BurgerDaoImpl;
import com.workintech.s18d1.entity.BreadType;
import com.workintech.s18d1.entity.Burger;
import com.workintech.s18d1.exceptions.BurgerException;
import com.workintech.s18d1.util.BurgerValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
public class BurgerController {
    private final BurgerDao burgerDao;

    @Autowired
    public BurgerController(BurgerDao burgerDao) {
        this.burgerDao = burgerDao;
    }

    @GetMapping("/burger")
    public List<Burger> burgerList(){
        return burgerDao.findAll();
    }

    @GetMapping("/burger/{id}")
    public Burger findBurgerById(@PathVariable int id) {
        return burgerDao.findById(id);
    }

    @PostMapping("/burger")
    public Burger saveBurger(@RequestBody Burger burger) {
        try {
            BurgerValidation.validateBurger(burger);
            burgerDao.save(burger);
            return burger;
        } catch (BurgerException ex) {
            log.error("Error saving burger: {}", ex.getMessage());
            throw ex;
        }
    }

    @PutMapping("/workintech/burgers/{id}")
    public Burger updateBurger(@PathVariable long id, @RequestBody Burger burger) {
        try {
            burger.setId(id);
            return burgerDao.update(burger);
        } catch (BurgerException ex) {
            log.error("Error updating burger: {}", ex.getMessage());
            throw ex;
        }
    }

    @DeleteMapping("/burger/{id}")
    public void removeBurger(@PathVariable int id) {
        try {
            burgerDao.remove(id);
        } catch (Exception ex) {
            log.error("Error removing burger: {}", ex.getMessage());
            throw new BurgerException("Failed to remove burger", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/burger/price/{price}")
    public List<Burger> findByPriceBurger(@PathVariable double price){
        return burgerDao.findByPrice(price);
    }

    @GetMapping("/burger/breadType/{breadType}")
    public List<Burger> findByBreadType(@PathVariable BreadType breadType){
        return burgerDao.findByBreadType(breadType);
    }

    @GetMapping("/burger/content/{content}")
    public List<Burger> findByContent(@PathVariable String content){
        return burgerDao.findByContent(content);
    }

}
