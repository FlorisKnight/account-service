package com.nooty.nootyaccount;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/account")
public class PingController {

    @GetMapping(path = "/ping", produces = "application/json")
    public ResponseEntity get() {

        return ResponseEntity.ok("Pong");
    }
}
