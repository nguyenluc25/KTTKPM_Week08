package com.example.productservice.config;

import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Autowired
    private ProductRepository productRepository;

    @RabbitListener(queues = "order.created")
    public void handleOrderCreated(OrderMessage orderMessage) {
        Product product = productRepository.findById(orderMessage.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStock(product.getStock() - orderMessage.getQuantity());
        productRepository.save(product);
    }
}

class OrderMessage {
    private Long productId;
    private Integer quantity;

    // Getters and Setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}