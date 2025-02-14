package com.workintech.s18d1.dao;

import com.workintech.s18d1.entity.BreadType;
import com.workintech.s18d1.entity.Burger;
import com.workintech.s18d1.exceptions.BurgerException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.workintech.s18d1.util.BurgerValidation.validateBurger;

@Slf4j
@Repository
public class BurgerDaoImpl implements BurgerDao {
    private final EntityManager entityManager;

    @Autowired
    public BurgerDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public Burger save(Burger burger) {
        entityManager.persist(burger);
        return burger;
    }

    @Override
    public Burger findById(long id) {
        validateBurger(entityManager.find(Burger.class, id));

        if (entityManager.find(Burger.class, id) == null) {
            throw new BurgerException("Burger with ID " + id + " not found.", HttpStatus.NOT_FOUND);
        } else {
            return entityManager.find(Burger.class, id);
        }
    }

    @Override
    public List<Burger> findAll() {
        TypedQuery<Burger> query = entityManager.createQuery("SELECT b FROM Burger b", Burger.class);
        return query.getResultList();
    }

    @Override
    public List<Burger> findByPrice(double price) {
        TypedQuery<Burger> query = entityManager.createQuery("SELECT b FROM Burger b WHERE b.price > :price ORDER BY b.price DESC", Burger.class);
        query.setParameter("price", price);
        return query.getResultList();
    }

    @Override
    public List<Burger> findByBreadType(BreadType breadType) {
        TypedQuery<Burger> query = entityManager.createQuery("SELECT b FROM Burger b WHERE b.bread_type = :breadType ORDER BY b.bread_type ASC", Burger.class);
        query.setParameter("breadType", breadType);
        return query.getResultList();
    }

    @Override
    public List<Burger> findByContent(String content) {
        TypedQuery<Burger> query = entityManager.createQuery("SELECT b FROM Burger b WHERE LOWER(b.contents) LIKE LOWER(CONCAT('%', :content, '%'))", Burger.class);
        query.setParameter("content", content);
        return query.getResultList();
    }

    @Override
    @Transactional
    public Burger update(Burger newBurger) {
        return entityManager.merge(newBurger);
    }

    @Override
    @Transactional
    public Burger remove(long id) {
        Burger removedBurger = findById(id);
        entityManager.remove(findById(id));
        return removedBurger;
    }
}
