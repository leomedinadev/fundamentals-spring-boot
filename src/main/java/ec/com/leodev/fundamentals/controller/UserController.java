package ec.com.leodev.fundamentals.controller;

import ec.com.leodev.fundamentals.entity.User;
import ec.com.leodev.fundamentals.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/users")
public class UserController {


    @Autowired
    private IUserService userService;

    @GetMapping("/all")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/all/{page}/{size}")
    public ResponseEntity<?> findAll(@PathVariable Integer page, @PathVariable Integer size) {
        return new ResponseEntity<>(userService.findAllPageable(page, size), HttpStatus.OK);
    }

    @GetMapping("/byEmail/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<?> findByName(@PathVariable String name) {
        return new ResponseEntity<>(userService.getUsersByName(name), HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> find(@PathVariable Long id) {
        return new ResponseEntity<>(userService.find(id), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody User user) {
        userService.save(Collections.singletonList(user));
        return new ResponseEntity<>("Usuario registrado exitosamente.!", HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody User user, @PathVariable Long id) {
        userService.update(id, user);
        return new ResponseEntity<>("Usuario actualizado exitosamente!", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>("Usuario eliminado exitosamente!", HttpStatus.ACCEPTED);
    }


}
