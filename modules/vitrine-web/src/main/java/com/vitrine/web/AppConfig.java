package com.vitrine.web;

import com.vitrine.persistence.repository.impl.CustomerRepositoryImpl;
import com.vitrine.persistence.repository.impl.OrderRepositoryImpl;
import com.vitrine.persistence.repository.impl.PaymentRepositoryImpl;
import com.vitrine.persistence.repository.impl.ProductRepositoryImpl;
import com.vitrine.persistence.repository.impl.StockRepositoryImpl;
import com.vitrine.service.impl.CustomerServiceImpl;
import com.vitrine.service.impl.OrderServiceImpl;
import com.vitrine.service.impl.PaymentServiceImpl;
import com.vitrine.service.impl.ProductServiceImpl;
import com.vitrine.web.resource.CustomerResource;
import com.vitrine.web.resource.OrderResource;
import com.vitrine.web.resource.PaymentResource;
import com.vitrine.web.resource.ProductResource;
import org.glassfish.jersey.server.ResourceConfig;

public class AppConfig extends ResourceConfig {

    public AppConfig() {
        //The order is important: repositories -> services that they consume -> resources that consume the services.
        CustomerRepositoryImpl customerRepository = new CustomerRepositoryImpl();
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        StockRepositoryImpl stockRepository = new StockRepositoryImpl();
        OrderRepositoryImpl orderRepository = new OrderRepositoryImpl();
        PaymentRepositoryImpl paymentRepository = new PaymentRepositoryImpl();

        CustomerServiceImpl customerService = new CustomerServiceImpl(customerRepository);
        ProductServiceImpl productService = new ProductServiceImpl(productRepository,
                stockRepository);
        OrderServiceImpl orderService = new OrderServiceImpl(orderRepository,
                customerRepository, stockRepository, productRepository);
        PaymentServiceImpl paymentService = new PaymentServiceImpl(paymentRepository,
                orderRepository);

        register(new CustomerResource(customerService));
        register(new ProductResource(productService));
        register(new OrderResource(orderService));
        register(new PaymentResource(paymentService));
        register(JacksonConfig.class);
    }
}
