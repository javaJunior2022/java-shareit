package ru.practicum.shareit.item.ItemStorage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository <Item,Long> {


    List<Item> findByOwner_Id(long userId);


    Optional<Item> findByOwner_IdAndId(long userId, long itemId);
    @Query(" " +
            "select item from Item item " +
            "where (upper(item.name) like upper(concat('%', ?1, '%')) " +
            " or upper(item.description) like upper(concat('%', ?1, '%'))) " +
            " and item.available=true")
    List<Item> search(String text);

}
