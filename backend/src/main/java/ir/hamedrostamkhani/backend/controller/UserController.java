package ir.hamedrostamkhani.backend.controller;

import ir.hamedrostamkhani.backend.dto.request.UserCreateDTO;
import ir.hamedrostamkhani.backend.dto.request.UserUpdateDTO;
import ir.hamedrostamkhani.backend.dto.response.UserResponseDTO;
import ir.hamedrostamkhani.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<UserResponseDTO> create(@ModelAttribute @Valid UserCreateDTO dto) {
        UserResponseDTO saved = userService.create(dto);
        URI location = URI.create("/users/" + saved.id());
        return ResponseEntity.created(location).body(saved);
    }

    @PatchMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @ModelAttribute UserUpdateDTO dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers(@RequestParam Optional<Boolean> deleted, Pageable pageable) {
        var page = userService.getAllBy(deleted, pageable);
        return ResponseEntity.ok()
                .header("X-Pagination-Total", String.valueOf(page.getTotalElements()))
                .header("X-Pagination-Page", String.valueOf(page.getNumber()))
                .header("X-Pagination-Size", String.valueOf(page.getSize()))
                .body(page.getContent());
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getBy(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<UserResponseDTO> softDelete(@PathVariable Long id) {
        userService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}/hard")
    public ResponseEntity<UserResponseDTO> hardDelete(@PathVariable Long id) {
        userService.hardDelete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<UserResponseDTO> restoreDeleted(@PathVariable Long id) {
        userService.restoreDeleted(id);
        return ResponseEntity.noContent().build();
    }
}
