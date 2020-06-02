package com.nooty.nootyaccount;

import com.nooty.nootyaccount.models.User;
import com.nooty.nootyaccount.util.JwtUtil;
import com.nooty.nootyaccount.viewmodels.CreateViewModel;
import com.nooty.nootyaccount.viewmodels.LoginViewModel;
import com.nooty.nootyaccount.viewmodels.UpdateViewModel;
import com.nooty.nootyaccount.viewmodels.UserResponseViewModel;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/account")
public class AccountController {
    PasswordStorage passwordStorage = new PasswordStorage();

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @PostMapping(path = "/register", produces = "application/json")
    public ResponseEntity create(@RequestBody CreateViewModel createViewModel) {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(createViewModel.getUsername());
        user.setDisplayname(createViewModel.getDisplayname());
        user.setEmail(createViewModel.getEmail());
        user.setPassword(passwordHasher(createViewModel.getPassword()));

        Optional<User> userOptional = this.accountRepo.findByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            return ResponseEntity.status(404).build();
        }
        userOptional = this.accountRepo.findByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            return ResponseEntity.status(404).build();
        }

        this.accountRepo.save(user);
        UserResponseViewModel u = new UserResponseViewModel(user, jwtTokenUtil.generateToken(user));
        return ResponseEntity.ok(u);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity get(@PathVariable String id) {
        Optional<User> userOptional = this.accountRepo.findById(id);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(404).build();
        }

        User user = userOptional.get();
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @PostMapping(path = "/login", produces = "application/json")
    public ResponseEntity login(@RequestBody LoginViewModel loginViewModel) {
        Optional<User> userOptional = this.accountRepo.findByUsername(loginViewModel.getUsername());
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(404).build();
        }

        User user = userOptional.get();
        if (!verifyPassword(loginViewModel.getPassword(), user.getPassword())) {
            return ResponseEntity.status(404).build();
        }

        UserResponseViewModel u = new UserResponseViewModel(user, jwtTokenUtil.generateToken(user));
        return ResponseEntity.ok(u);
    }

    @PatchMapping(path = "/update/{id}", produces = "application/json")
    public ResponseEntity update(@RequestBody UpdateViewModel updateViewModel, @PathVariable String id){
        Optional<User> userOptional = this.accountRepo.findById(id);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(404).build();
        }
        User user = userOptional.get();

        if (!updateViewModel.getUsername().equals(user.getUsername()) && updateViewModel.getUsername() != null && updateViewModel.getUsername() != "") {
            user.setUsername(updateViewModel.getUsername());
        }
        if (!updateViewModel.getDisplayname().equals(user.getDisplayname()) && updateViewModel.getDisplayname() != null && updateViewModel.getDisplayname() != "") {
            user.setDisplayname(updateViewModel.getDisplayname());
        }
        if (!updateViewModel.getEmail().equals(user.getEmail()) && updateViewModel.getEmail() != null && updateViewModel.getEmail() != "") {
            user.setEmail(updateViewModel.getEmail());
        }
        if (!verifyPassword(updateViewModel.getPassword(),user.getPassword()) && updateViewModel.getPassword() != null && updateViewModel.getPassword() != "") {
            user.setPassword(passwordHasher(updateViewModel.getPassword()));
        }
        this.accountRepo.save(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity delete(@PathVariable String id) {
        Optional<User> userOptional = this.accountRepo.findById(id);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(404).build();
        }
        User user = userOptional.get();
        this.accountRepo.delete(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/auth", produces = "application/json")
    public ResponseEntity authorize(@RequestBody UserResponseViewModel userResponseViewModel) {

        if (jwtTokenUtil.validateToken(userResponseViewModel.getToken(), userResponseViewModel)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(401).build();
    }

    private String passwordHasher(String pass){
        try {
            return passwordStorage.createHash(pass);
        } catch (PasswordStorage.CannotPerformOperationException e) {
            e.printStackTrace();
        }
        return pass;
    }

    private boolean verifyPassword(String password, String storedPassword){
        try {
            return passwordStorage.verifyPassword(password, storedPassword);
        } catch (PasswordStorage.CannotPerformOperationException e) {
            e.printStackTrace();
        } catch (PasswordStorage.InvalidHashException e) {
            e.printStackTrace();
        }
        return false;
    }
}
