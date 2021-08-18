package com.taxserviceapp.web.controller;

import com.taxserviceapp.web.controller.MainController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(MainController.class)
class MainControllerTest {

    private MockMvc mockMvc;

    @Test
    public void testMainPage() throws Exception {
        this.mockMvc.perform(get("/main"))
                .andExpect(status().isOk());
//                .andExpect(view().name(""));

    }

}