package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-item-requests.
 */


@AllArgsConstructor
@Data
public class ItemRequestDto {
    private Long id;
    @NotBlank
    private String description;
}
