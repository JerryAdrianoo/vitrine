package com.vitrine.service.impl;

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
import com.vitrine.api.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository, StockRepository stockRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Order placeOrder(Long customerId, List<OrderItem> items) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));

        for (OrderItem item : items) {
            Stock stock = stockRepository.findByProductId(item.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Stock not found for the product: " + item.getProduct().getId()));

            if (stock.getQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException("Stock insufficient for the product: " + item.getProduct().getId());
            }

            stock.setQuantity(stock.getQuantity() - item.getQuantity());
            stockRepository.update(stock);

            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProduct().getId()));

            item.setProduct(product);
            item.setUnitPrice(product.getPrice());
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setItems(items);

        items.forEach(
                item -> item.setOrder(order));

        orderRepository.save(order);

        return order;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> findByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    public void cancel(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " +
                        orderId));

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order has already been cancelled.");
        }

        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.update(order);
    }
}
