package ir.hamedrostamkhani.backend.service;

import ir.hamedrostamkhani.backend.dto.request.UserCreateDTO;
import ir.hamedrostamkhani.backend.dto.request.UserUpdateDTO;
import ir.hamedrostamkhani.backend.dto.response.UserResponseDTO;
import ir.hamedrostamkhani.backend.exception.resource.DuplicateResourceException;
import ir.hamedrostamkhani.backend.exception.resource.ResourceNotFoundException;
import ir.hamedrostamkhani.backend.mapper.UserMapper;
import ir.hamedrostamkhani.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
//    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    private final String avatarFolder = "users/avatar/";

    public UserResponseDTO create(UserCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail()))
            throw new DuplicateResourceException("Email already exists");

        var user = userMapper.toEntity(dto);

        // Todo: Hash password
//        user.setPassword(passwordEncoder.encode(dto.password()));

        System.out.println(dto.getAvatar().isEmpty());

        // Handle avatar
        if (dto.getAvatar() != null && !dto.getAvatar().isEmpty()) {
            String avatarPath = fileStorageService.store(dto.getAvatar(), avatarFolder);
            user.setAvatar(avatarPath);
        }

        var saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    public UserResponseDTO update(Long id, UserUpdateDTO dto) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Todo: Handle password hash if password is not null

        // Update fields via Mapper
        userMapper.update(dto, user);

        // Handle avatar
        if (dto.getAvatar() != null && !dto.getAvatar().isEmpty()) {
            String avatarPath = fileStorageService.update(user.getAvatar(), dto.getAvatar(), avatarFolder);
            user.setAvatar(avatarPath);
        }

        var updated = userRepository.save(user);
        return userMapper.toDto(updated);
    }

    public Page<UserResponseDTO> getAllBy(Optional<Boolean> deleted, Pageable pageable) {
        if (deleted.isEmpty())
            return userRepository.findAll(pageable).map(userMapper::toDto);
        else if (deleted.get())
            return userRepository.findAllDeleted(pageable).map(userMapper::toDto);
        else
            return userRepository.findAllNotDeleted(pageable).map(userMapper::toDto);
    }

    public UserResponseDTO getBy(Long id) {
        return userRepository.findById(id).map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
    }

    public void softDelete(Long id) {
        var affected = userRepository.softDeleteById(id, LocalDateTime.now());

        if (affected == 0)
            throw new ResourceNotFoundException("User not found or already deleted");
    }

    public void restoreDeleted(Long id) {
        var affected = userRepository.restoreById(id);

        if (affected == 0)
            throw new ResourceNotFoundException("User not found or already deleted");
    }

    public void hardDelete(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        userRepository.delete(user);
        fileStorageService.delete(user.getAvatar());
    }
}
