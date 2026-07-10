package com.mercadoaurora.order.api;

import com.mercadoaurora.order.application.usecase.CancelOrderUseCase;
import com.mercadoaurora.order.application.usecase.ConfirmOrderUseCase;
import com.mercadoaurora.order.application.usecase.CreateOrderUseCase;
import com.mercadoaurora.order.application.usecase.GetOrderUseCase;
import com.mercadoaurora.order.application.usecase.ListOrdersByCustomerUseCase;
import com.mercadoaurora.order.application.usecase.MarkOrderPaidUseCase;
import com.mercadoaurora.order.application.usecase.ReserveOrderStockUseCase;
import com.mercadoaurora.order.application.usecase.StartPaymentUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ApiErrorHandlingTest {
    @Mock CreateOrderUseCase createOrderUseCase;
    @Mock ReserveOrderStockUseCase reserveOrderStockUseCase;
    @Mock StartPaymentUseCase startPaymentUseCase;
    @Mock MarkOrderPaidUseCase markOrderPaidUseCase;
    @Mock ConfirmOrderUseCase confirmOrderUseCase;
    @Mock CancelOrderUseCase cancelOrderUseCase;
    @Mock GetOrderUseCase getOrderUseCase;
    @Mock ListOrdersByCustomerUseCase listOrdersByCustomerUseCase;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        OrderController controller = new OrderController(
                createOrderUseCase,
                reserveOrderStockUseCase,
                startPaymentUseCase,
                markOrderPaidUseCase,
                confirmOrderUseCase,
                cancelOrderUseCase,
                getOrderUseCase,
                listOrdersByCustomerUseCase
        );
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturnSanitizedBadRequestForMalformedJson() throws Exception {
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("Malformed JSON request"));
    }

    @Test
    void shouldReturnSanitizedBadRequestForInvalidUuid() throws Exception {
        mockMvc.perform(get("/api/v1/orders/not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Invalid path parameter: orderId"))
                .andExpect(content().string(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.matchesPattern("(?is).*java\\..*|.*jdbc:.*|.*stack trace.*")
                )));
    }
}
