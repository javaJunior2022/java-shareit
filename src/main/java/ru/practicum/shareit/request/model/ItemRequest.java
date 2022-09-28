package ru.practicum.shareit.request.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "requests")
public class ItemRequest {

    @Id
    private long id;
    @Column(name = "description", length = 512)
    private String description;
    @Column(name = "requestor_id")
    private long requestor;
    @Column(name = "created")
    private LocalDateTime created;

}
