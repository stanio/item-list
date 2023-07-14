package net.example.spring.web;

import java.net.URI;
import java.util.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import net.example.spring.data.Item;
import net.example.spring.data.ItemRepository;

@RestController
@RequestMapping(ItemController.PATH)
public class ItemController {

    public static final String PATH = "/api/items";

    private ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public Collection<ItemEntity> listItems() {
        return itemRepository.findAll().stream()
                             .map(ItemController::initEntity)
                             .toList();
    }

    @PostMapping
    public ResponseEntity<ItemEntity> createItem(@Valid @RequestBody ItemUpdate params) {
        Item item = itemRepository.save(updateValues(new Item(), params));
        return ResponseEntity
                .created(URI.create(PATH + "/" + item.getId()))
                .body(initEntity(item));
    }

    @GetMapping("/{id}")
    public ItemEntity getItem(@PathVariable String id) {
        return itemRepository.findById(parseId(id))
                .map(ItemController::initEntity)
                .orElseThrow(() -> notFound(id));
    }

    @PutMapping("/{id}")
    public ItemEntity updateItem(@PathVariable String id,
                                 @Valid @RequestBody ItemUpdate params) {
        return itemRepository.findById(parseId(id))
                .map(item -> updateValues(item, params))
                .map(itemRepository::save)
                .map(ItemController::initEntity)
                .orElseThrow(() -> notFound(id));
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable String id) {
        itemRepository.findById(parseId(id))
                      .ifPresentOrElse(itemRepository::delete,
                                       () -> { throw notFound(id); });
    }

    private static ItemEntity initEntity(Item item) {
        return new ItemEntity(PATH + "/" + item.getId(), item.getLabel());
    }

    private static Item updateValues(Item item, ItemUpdate params) {
        item.setLabel(params.getLabel());
        return item;
    }

    private static Long parseId(String str) {
        try {
            long id = Long.parseLong(str);
            if (id > 0) {
                return id;
            }
        } catch (NumberFormatException e) {
            // fall through
        }
        throw notFound(str);
    }

    private static ErrorResponseException notFound(Object itemId) {
        ErrorResponseException ex = new ErrorResponseException(HttpStatus.NOT_FOUND);
        ex.setDetail("No such item ID=" + itemId);
        return ex;
    }

}
