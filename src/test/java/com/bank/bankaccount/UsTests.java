package com.bank.bankaccount;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bank.bankaccount.controller.AccountController;
import com.bank.bankaccount.entity.Account;
import com.bank.bankaccount.repository.AccountRepository;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@ExtendWith(SpringExtension.class)
@WebMvcTest(value = AccountController.class)
public class UsTests {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    AccountRepository accountRepository;

    Account accountTest = new Account(1L, "John SMITH", 1000.0);

    @Test
    public void allUStests()  throws Exception {

        // ADD ACCOUNT
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/testAddClient")
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);


        List<Account> listAccount = new ArrayList<>(Arrays.asList(accountTest));


        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.of(accountTest));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/accounts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(1000.0)));


        Mockito.when(accountRepository.getById(1L)).thenReturn(accountTest);
        // US 1
        mockMvc.perform(MockMvcRequestBuilders
                .post("/accounts/1/deposit/56.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(1056.0)));

        // US 2
        mockMvc.perform(MockMvcRequestBuilders
                .post("/accounts/1/withdrawal/40.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(1016.0)));

        // US 3
        mockMvc.perform(MockMvcRequestBuilders
                .get("/accounts/1/operations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].typeOperation", is("DEPOSIT")))
                .andExpect(jsonPath("$[1].typeOperation", is("WITHDRAWAL")));
        /* operation list example
        [{"typeOperation":"DEPOSIT","date":1676475309535,"previousBalance":500.0,"newBalance":600.0,"amount":100.0},
        {"typeOperation":"WITHDRAWAL","date":1676475315472,"previousBalance":655.6,"newBalance":615.6,"amount":40.0}]
         */
    }

    @Test
    public void getTest() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/accounts/test")
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
    }


/*
        Mockito.when(accountRepository.findAll()).thenReturn(listAccount);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/accounts/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
 */

}
