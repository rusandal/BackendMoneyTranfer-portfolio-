package com.example.springbackendmoneytranfer;

import com.example.springbackendmoneytranfer.model.Confirm;
import com.example.springbackendmoneytranfer.model.Transfer;
import com.example.springbackendmoneytranfer.repository.TransferRepository;
import com.example.springbackendmoneytranfer.service.ServiceTransfer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SpringBackendMoneyTranferUnitTest {

    static Transfer transfer = new Transfer();
    static Confirm confirm = new Confirm();
    static Map<String, Object> answerObject = new HashMap<>();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TransferRepository repository;

    @BeforeAll
    static void createTransferAndConfirm() {
        Transfer.Amount amount = transfer.new Amount();
        amount.setCurrency("RUR");
        amount.setValue(1000);
        transfer.setCardFromNumber("1111111111111111");
        transfer.setCardFromCVV("123");
        transfer.setCardFromValidTill("12/23");
        transfer.setCardToNumber("2222222222222222");
        transfer.setAmount(amount);
        answerObject.put("operationid", "11111111-1111-1111-1111-111111111111");
    }

    @Test
    public void sendTransferInfo_whenAdd_thenStatus200AndReturnId() throws Exception {
        Mockito.when(repository.addTransferToMap(transfer)).thenReturn(answerObject.get("operationid").toString());
        mockMvc.perform(post("/transfer")
                        .content(objectMapper.writeValueAsString(transfer))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(answerObject)));
    }

    @Test
    public void sendConfirmCode_thenReturnOk() throws Exception {
        mockMvc.perform(post("/confirmOperation")
                        .content("{\"code\":\"0000\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("operationid").isString());
    }

    @Test
    public void sendInvalidCode_thenReturnIllegalArgumentException() throws Exception {
        mockMvc.perform(post("/confirmOperation")
                        .content("{\"code\":\"1111\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("Code is invalid!"))
                .andExpect(jsonPath("id").value(0));
    }
}


//Работает - Полное поднятие контроллера. Записывает в реальные лог

/*import com.example.springbackendmoneytranfer.model.Confirm;
import com.example.springbackendmoneytranfer.model.Transfer;
import com.example.springbackendmoneytranfer.repository.TransferRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBackendMoneyTransferApplicationTests {

    static Transfer transfer = new Transfer();
    static Confirm confirm = new Confirm();

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private TransferRepository repository;

    @BeforeAll
    public static void createObjects(){
        //Transfer transfer=new Transfer();
        Transfer.Amount amount = transfer.new Amount();
        amount.setCurrency("RUR");
        amount.setValue(1000);
        transfer.setCardFromNumber("1111111111111111");
        transfer.setCardFromCVV("123");
        transfer.setCardFromValidTill("12/23");
        transfer.setCardToNumber("2222222222222222");
        transfer.setAmount(amount);

        //Confirm confirm = new Confirm();
        confirm.setCode("0000");
    }

    @org.junit.jupiter.api.Test
    void sendTransferInfo_whenAdd_thenStatus200AndReturnId(){
        Mockito.when(repository.addTransferToList(transfer)).thenReturn("123e4567-e89b-12d3-a456-426655440000");
        ResponseEntity<Object> responseEntity = testRestTemplate.postForEntity("/transfer", transfer, Object.class);

        System.out.println(responseEntity.getBody());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().toString(), containsString("123e4567-e89b-12d3-a456-426655440000"));

        ResponseEntity<String> responseEntity1 = testRestTemplate.postForEntity("/confirmOperation", confirm, String.class);
        assertThat(responseEntity1.getStatusCode(), is(HttpStatus.OK));
    }

    @org.junit.jupiter.api.Test
    void testLogger(){
        *//*testRestTemplate.postForEntity("/transfer", transfer, Object.class);
        as*//*
    }

    @Test
    void contextLoads() {
    }
}*/
