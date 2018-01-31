package com.soar.timetoeat.auth.dao;

import com.soar.timetoeat.auth.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
