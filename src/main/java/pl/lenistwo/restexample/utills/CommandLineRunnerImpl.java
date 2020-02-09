package pl.lenistwo.restexample.utills;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.lenistwo.restexample.entities.User;
import pl.lenistwo.restexample.repositories.UserRepository;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final UserRepository repo;

    @Autowired
    public CommandLineRunnerImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 15; i++) {
            repo.save(new User("Adam","Kox"," Gmail"));
        }
    }
}
