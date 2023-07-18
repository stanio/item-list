package net.example.spring.web;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import net.example.spring.data.Item;
import net.example.spring.data.ItemRepository;
import net.example.spring.web.security.AuthUser;

@RestController
@RequestMapping(ItemController.PATH)
public class ItemController {

    public static final String PATH = "/api/items";

    private ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public Collection<ItemEntity> listItems(
            @RequestParam(name = "owner", defaultValue = "") String ownerFilter) {
        String owner = AuthUser.isAdmin() ? ownerFilter : AuthUser.getName();
        if (owner.isBlank()) {
            return itemRepository.findAll().stream()
                    .map(ItemController::initEntity)
                    .toList();
        }
        return itemRepository.findByOwner(owner)
                .stream().map(ItemController::initEntity)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ItemEntity> createItem(@Valid @RequestBody ItemUpdate params) {
        Item item = updateValues(new Item(), params);
        item.setOwner(AuthUser.getName());
        item = itemRepository.save(item);
        return ResponseEntity
                .created(URI.create(PATH + "/" + item.getId()))
                .body(initEntity(item));
    }

    private Item findItem(String id) {
        Long parsedId = parseId(id);
        Optional<Item> item = AuthUser.isAdmin()
                ? itemRepository.findById(parsedId)
                : itemRepository.findByIdAndOwner(parsedId, AuthUser.getName());
        return item.orElseThrow(() -> notFound(parsedId));
    }

    @GetMapping("/{id}")
    public ItemEntity getItem(@PathVariable String id) {
        return initEntity(findItem(id));
    }

    @PutMapping("/{id}")
    public ItemEntity updateItem(@PathVariable String id,
                                 @Valid @RequestBody ItemUpdate params) {
        Item item = findItem(id);
        item = itemRepository.save(updateValues(item, params));
        return initEntity(item);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable String id) {
        itemRepository.delete(findItem(id));
    }

    private static ItemEntity initEntity(Item item) {
        return new ItemEntity(PATH + "/" + item.getId(),
                              item.getLabel(), item.getOwner());
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
