package com.vitrine.web;

import com.vitrine.persistence.repository.impl.CategoryRepositoryImpl;
import com.vitrine.persistence.repository.impl.CustomerRepositoryImpl;
import com.vitrine.persistence.repository.impl.OrderRepositoryImpl;
import com.vitrine.persistence.repository.impl.PaymentRepositoryImpl;
import com.vitrine.persistence.repository.impl.ProductRepositoryImpl;
import com.vitrine.persistence.repository.impl.StockRepositoryImpl;
import com.vitrine.service.impl.AuthServiceImpl;
import com.vitrine.service.impl.CategoryServiceImpl;
import com.vitrine.service.impl.CustomerServiceImpl;
import com.vitrine.service.impl.OrderServiceImpl;
import com.vitrine.service.impl.PaymentServiceImpl;
import com.vitrine.service.impl.ProductServiceImpl;
import com.vitrine.web.exception.AppExceptionMapper;
import com.vitrine.web.exception.ConstraintViolationExceptionMapper;
import com.vitrine.web.resource.AuthResource;
import com.vitrine.web.resource.CategoryResource;
import com.vitrine.web.resource.CustomerResource;
import com.vitrine.web.resource.OrderResource;
import com.vitrine.web.resource.PaymentResource;
import com.vitrine.web.resource.ProductResource;
import com.vitrine.web.security.AuthFilter;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.glassfish.jersey.server.ResourceConfig;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;

@OpenAPIDefinition(
        info = @Info(
                title = "Vitrine API",
                version = "1.0",
                description = "E-commerce API of Vitrine's project"
        )
)
public class AppConfig extends ResourceConfig {

    public AppConfig() {
        //The order is important: repositories -> services that they consume -> resources that consume the services.
        CustomerRepositoryImpl customerRepository = new CustomerRepositoryImpl();
        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        StockRepositoryImpl stockRepository = new StockRepositoryImpl();
        OrderRepositoryImpl orderRepository = new OrderRepositoryImpl();
        PaymentRepositoryImpl paymentRepository = new PaymentRepositoryImpl();
        CategoryRepositoryImpl categoryRepository = new CategoryRepositoryImpl();

        CustomerServiceImpl customerService = new CustomerServiceImpl(customerRepository);
        AuthServiceImpl authService = new AuthServiceImpl(customerRepository);
        ProductServiceImpl productService = new ProductServiceImpl(productRepository,
                stockRepository);
        OrderServiceImpl orderService = new OrderServiceImpl(orderRepository,
                customerRepository, stockRepository, productRepository);
        PaymentServiceImpl paymentService = new PaymentServiceImpl(paymentRepository,
                orderRepository);
        CategoryServiceImpl categoryService = new CategoryServiceImpl(categoryRepository);

        register(new CustomerResource(customerService));
        register(new ProductResource(productService));
        register(new OrderResource(orderService));
        register(new PaymentResource(paymentService));
        register(new AuthResource(authService));
        register(new CategoryResource(categoryService));
        register(JacksonConfig.class);
        register(AppExceptionMapper.class);
        register(ConstraintViolationExceptionMapper.class);
        register(AuthFilter.class);
        register(OpenApiResource.class);
    }
}
