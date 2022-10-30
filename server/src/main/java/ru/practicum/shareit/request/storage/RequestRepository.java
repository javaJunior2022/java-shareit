package ru.practicum.shareit.request.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequestor_IdOrderByCreatedDesc(long requestorId);

    @Query("from ItemRequest t1 where t1.requestor.id<>?1")
    List<ItemRequest> findAllByOtherUser(Long userId, Pageable pageable);

}
