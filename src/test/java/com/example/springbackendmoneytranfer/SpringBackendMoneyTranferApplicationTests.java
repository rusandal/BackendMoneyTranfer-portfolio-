package com.example.springbackendmoneytranfer;
import com.example.springbackendmoneytranfer.model.Confirm;
import com.example.springbackendmoneytranfer.model.Transfer;
import com.example.springbackendmoneytranfer.repository.TransferRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;

import java.util.HashMap;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SpringBackendMoneyTransferUnitTest {

    private static Transfer transfer = new Transfer();
    private static Confirm confirm = new Confirm();
    private static Map<String, Object> answerObject = new HashMap<>();
    private GenericContainer<?> myapp = new GenericContainer<>("my_transfer:latest")
            .withExposedPorts(5500);

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TransferRepository repository;
    @Autowired
    private TestRestTemplate testRestTemplate;

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
        transfer.setOperationId("11111111-1111-1111-1111-111111111111");
        answerObject.put("operationid", "11111111-1111-1111-1111-111111111111");
        confirm.setCode("0000");
    }

    @Test
    public void sendTransferInfo_thenAddToTransfers_thenStatus200AndReturnId() throws Exception {
        Mockito.when(repository.addTransferToMap(transfer)).thenReturn(transfer.getOperationId());
        Map<String, Transfer> transferMap = new HashMap<>();
        transferMap.put(transfer.getOperationId(), transfer);
        Mockito.when(repository.getTransfers()).thenReturn(transferMap);
        mockMvc.perform(post("/transfer")
                        .content(objectMapper.writeValueAsString(transfer))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(answerObject)));
    }

    @Test
    public void sendTransferInfo_thenNotAddToTransfers_thenStatus400() throws Exception{
        Mockito.when(repository.addTransferToMap(transfer)).thenReturn(transfer.getOperationId());
        mockMvc.perform(post("/transfer")
                        .content(objectMapper.writeValueAsString(transfer))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("{\"id\":0,\"message\":null}"));
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

    @Test
    public void handleRuntimeExceptionTest() throws Exception {
        Mockito.when(repository.addTransferToMap(transfer)).thenThrow(RuntimeException.class);
        mockMvc.perform(post("/transfer")
                        .content(objectMapper.writeValueAsString(transfer))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isInternalServerError());
    }
    @Test
    public void handleIllegalArgumentExceptionTest() throws Exception {
        Mockito.when(repository.addTransferToMap(transfer)).thenThrow(NullPointerException.class);
        mockMvc.perform(post("/transfer")
                        .content(objectMapper.writeValueAsString(transfer))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }
    @Test
    public void testContainer(){
        myapp.start();
        //String port = myapp.getMappedPort(5500).toString();
        ResponseEntity<String> fromApp = testRestTemplate.postForEntity("http://localhost:5500/transfer", transfer, String.class);
        Assert.assertEquals(fromApp.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(!fromApp.getBody().contains("message"));
        ResponseEntity<String> fromApp2 = testRestTemplate.postForEntity("http://localhost:5500/confirmOperation", confirm, String.class);
        Assert.assertTrue(fromApp2.getBody().contains("this operation id"));
    }
}