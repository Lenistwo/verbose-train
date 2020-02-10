package pl.lenistwo.restexample.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.lenistwo.restexample.entities.User;
import pl.lenistwo.restexample.exceptions.UserCannotBeNullException;
import pl.lenistwo.restexample.exceptions.UserNotFoundException;
import pl.lenistwo.restexample.repositories.UserRepository;
import pl.lenistwo.restexample.utills.OffsetPageRequest;

import java.util.List;
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
    @GetMapping(value = "/all-users", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@RequestParam Long id) {
        Optional<User> user = repository.findById(id);
        return user.orElseThrow(() -> new UserNotFoundException("User with " + id + "doesnt exist"));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/all-with-limit", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllWithLimit(@RequestParam int limit) {
        return repository.findAll(PageRequest.of(0, limit));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/all-with-skip", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllWithSkipAndLimit(@RequestParam int skip, @RequestParam int limit) {
        return repository.findAll(new OffsetPageRequest(skip, limit));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/delete-user")
    public void deleteUserWithId(@RequestParam long id) {
        Optional<User> user = repository.findById(id);
        repository.delete(user.orElseThrow(() -> new UserNotFoundException("User with " + id + "doesnt exist")));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create-user")
    public void createUser(@RequestBody User user) {
        Optional<User> optionalUser = Optional.of(user);
        repository.save(optionalUser.orElseThrow(() -> new UserCannotBeNullException("Passed object is null")));
    }
}
