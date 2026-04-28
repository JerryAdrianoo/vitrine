package com.vitrine.service.impl;

import com.vitrine.api.dto.OrderResponse;
import com.vitrine.api.model.Customer;
import com.vitrine.api.model.Order;
import com.vitrine.api.model.OrderItem;
import com.vitrine.api.model.OrderStatus;
import com.vitrine.api.model.Product;
import com.vitrine.api.model.Stock;
import com.vitrine.api.repository.CustomerRepository;
import com.vitrine.api.repository.OrderRepository;
import com.vitrine.api.repository.ProductRepository;
import com.vitrine.api.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderServiceImplTest {

    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;
    private StockRepository stockRepository;
    private ProductRepository productRepository;
    private OrderServiceImpl orderService;

    @BeforeEach
    void setup() {
        orderRepository = mock(OrderRepository.class);
        customerRepository = mock(CustomerRepository.class);
        stockRepository = mock(StockRepository.class);
        productRepository = mock(ProductRepository.class);
        orderService = new OrderServiceImpl(orderRepository, customerRepository, stockRepository, productRepository);
    }

    @Test
    public void placeOrderHappyPathSavesOrderAndDecrementsStock() {
        Customer customer = buildCustomer(1L);
        Product product = buildProduct(10L, new BigDecimal("99.90"));
        Stock stock = buildStock(product, 5);
        OrderItem item = buildOrderItem(product, 2);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(stockRepository.findByProductId(10L)).thenReturn(Optional.of(stock));
        when(productRepository.findById(10L)).thenReturn(Optional.of(product));

        Order result = orderService.placeOrder(1L, List.of(item));

        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals(customer, result.getCustomer());
        assertEquals(new BigDecimal("99.90"), item.getUnitPrice());
        assertEquals(3, stock.getQuantity());
        verify(stockRepository).update(stock);
        verify(orderRepository).save(result);
    }

    @Test
    public void placeOrderCustomerNotFoundThrowsException() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> orderService.placeOrder(99L, List.of(buildOrderItem(buildProduct(1L, BigDecimal.TEN), 1))));

        verify(orderRepository, never()).save(any());
    }

    @Test
    public void placeOrderStockNotFoundThrowsException() {
        Customer customer = buildCustomer(1L);
        Product product = buildProduct(10L, BigDecimal.TEN);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(stockRepository.findByProductId(10L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> orderService.placeOrder(1L, List.of(buildOrderItem(product, 1))));

        verify(orderRepository, never()).save(any());
    }

    @Test
    public void placeOrderInsufficientStockThrowsException() {
        Customer customer = buildCustomer(1L);
        Product product = buildProduct(10L, BigDecimal.TEN);
        Stock stock = buildStock(product, 1);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(stockRepository.findByProductId(10L)).thenReturn(Optional.of(stock));

        assertThrows(IllegalArgumentException.class, () -> orderService.placeOrder(1L, List.of(buildOrderItem(product, 5))));

        verify(orderRepository, never()).save(any());
    }

    @Test
    public void cancelHappyPathSetStatusCancelled() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.cancel(1L);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());

        verify(orderRepository).update(order);
    }

    @Test
    public void cancelOrderNotFoundThrowsException() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> orderService.cancel(99L));

        verify(orderRepository, never()).update(any());
    }

    @Test
    public void cancelAlreadyCancelledThrowsIllegalStateException() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.CANCELLED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(IllegalStateException.class, () -> orderService.cancel(1L));

        verify(orderRepository, never()).update(any());
    }

    private Customer buildCustomer(Long id) {
        Customer c = new Customer();
        c.setId(id);
        c.setName("Test Customer");
        c.setEmail("customer@test.com");
        c.setCpf("12345678901");
        return c;
    }

    private Product buildProduct(Long id, BigDecimal price) {
        Product p = new Product();
        p.setId(id);
        p.setName("Test Product");
        p.setPrice(price);
        return p;
    }

    private Stock buildStock(Product product, int quantity) {
        Stock s = new Stock();
        s.setProduct(product);
        s.setQuantity(quantity);
        return s;
    }

    private OrderItem buildOrderItem(Product product, int quantity) {
        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        return item;
    }
}
