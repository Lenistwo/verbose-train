package pl.lenistwo.restexample.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.lenistwo.restexample.entities.User;

import java.util.Collection;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Collection<User> findAll();

    Collection<User> findAll(Pageable limit);

}
