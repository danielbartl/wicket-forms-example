package dev.jbaby;

import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends ListCrudRepository<User,Long> {

}
