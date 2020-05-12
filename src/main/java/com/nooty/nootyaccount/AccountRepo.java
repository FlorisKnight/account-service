package com.nooty.nootyaccount;

import com.nooty.nootyaccount.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface AccountRepo extends CrudRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
