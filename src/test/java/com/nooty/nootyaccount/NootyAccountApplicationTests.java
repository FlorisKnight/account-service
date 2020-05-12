package com.nooty.nootyaccount;

import com.nooty.nootyaccount.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
class NootyAccountApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepo accountRepo;

    @BeforeEach
    public void init() {
        User u = new User();
        given(this.accountRepo.findById("1")).willReturn(Optional.of(u));
    }

    @Test
    void templateTest() throws Exception {
        /*
        this.mockMvc.perform(post("/")
                .contentType("application/json")
                .content("{\"username\":\"admin\"}"))
				.andDo(print())
				.andExpect(status().isOk());
         */
	}

}
