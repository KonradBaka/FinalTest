package pl.kurs.finaltest.security;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private UserRepository repository;
    private ModelMapper mapper;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository repository, ModelMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/api/register")
    public ResponseEntity<UserDto> addUser(@RequestBody CreateUserCommand command) {
        User newUser = mapper.map(command, User.class);
        newUser.setPassword(passwordEncoder.encode(command.getPassword()));
        User savedUser = repository.save(newUser);

        return ResponseEntity.ok(new UserDto(savedUser.getId(), savedUser.getUsername(), command.getPassword(), command.getRole()));
    }
}
