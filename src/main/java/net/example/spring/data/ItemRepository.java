package net.example.spring.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

// CrudRepository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwner(String owner);

    Optional<Item> findByIdAndOwner(Long id, String owner);

}
