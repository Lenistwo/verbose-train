package pl.lenistwo.restexample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.lenistwo.restexample.entities.User;
import pl.lenistwo.restexample.exceptions.UserNotFoundException;
import pl.lenistwo.restexample.repositories.UserRepository;
import pl.lenistwo.restexample.utills.OffsetPageRequest;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/user-api")
public class UserController {

    private final UserRepository repository;

    @Autowired
    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<User> getAllUsers() {
        return repository.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable Long id) {
        Optional<User> user = repository.findById(id);
        return user.orElseThrow(() -> new UserNotFoundException("User with " + id + " doesnt exist"));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/users-with-limit", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<User> getAllWithLimit(@RequestParam(defaultValue = "0") int limit) {
        return repository.findAll(PageRequest.of(0, limit));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/users-with-skip", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<User> getAllWithSkipAndLimit(@RequestParam int skip, @RequestParam int limit) {
        return repository.findAll(new OffsetPageRequest(skip, limit));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/delete-user")
    public void deleteUserWithId(@RequestParam long id) {
        Optional<User> user = repository.findById(id);
        repository.delete(user.orElseThrow(() -> new UserNotFoundException("User with " + id + " doesnt exist")));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create-user")
    public void createUser(@Valid @RequestBody User user) {
        repository.save(user);
    }

    @PatchMapping("/update-user/{id}")
    public void updateUser(@PathVariable long id, @Valid @RequestBody User user) {
        user.setId(id);
        repository.save(user);
    }
}
