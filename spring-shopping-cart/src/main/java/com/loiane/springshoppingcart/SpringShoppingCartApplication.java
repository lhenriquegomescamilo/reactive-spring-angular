package com.loiane.springshoppingcart;

import com.loiane.springshoppingcart.model.Order;
import com.loiane.springshoppingcart.model.Product;
import com.loiane.springshoppingcart.repository.OrderRepository;
import com.loiane.springshoppingcart.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.UUID;

import static com.loiane.springshoppingcart.repository.RepositoryUtil.deleteAll;
import static com.loiane.springshoppingcart.repository.RepositoryUtil.save;

@SpringBootApplication
public class SpringShoppingCartApplication {


    public static void main(String[] args) {
        SpringApplication.run(SpringShoppingCartApplication.class, args);
    }

    @Bean
    CommandLineRunner init(final ProductRepository repository) {
        return deleteAll(repository, this::stackToSaveProduct);
    }

    private Runnable stackToSaveProduct(final ReactiveMongoRepository<Product, String> repository) {
        return () -> Flux.interval(Duration.ofSeconds(1))
                .take(11)
                .map(i -> i.intValue() + 1)
                .map(this::product)
                .map(save(repository))
                .subscribe();
    }


    private Product product(final Integer inc) {
        final Product coffee = Product.builder()
                .id(UUID.randomUUID().toString())
                .name(String.format("Coffe %d", inc))
                .description("Coffee")
                .price(inc + 1.50)
                .discount(0.7)
                .image(inc.toString())
                .build();
        if (inc % 3 == 0) {
            coffee.setStatus("sale");
            coffee.setDiscounted("discounted");
        }
        return coffee;

    }

    @Bean
    CommandLineRunner initOrder(final OrderRepository repository) {
        final String[] colors = {"blue", "purple", "pink", "green"};
        final String[] status = {"Order Received", "Order Confirmed", "Order Being Prepared", "Delivered"};
        final Runnable stackToSave = stackToSaveOrder(repository, colors, status);
        return args -> repository.deleteAll().subscribe(null, null, stackToSave);
    }


    private Runnable stackToSaveOrder(OrderRepository repository, String[] colors, String[] status) {
        return () -> Flux.interval(Duration.ofSeconds(1))
                .take(4)
                .map(i -> i.intValue() + 1)
                .map(i -> new Order(UUID.randomUUID().toString(), colors[i - 1], status[i - 1]))
                .map(save(repository))
                .subscribe();
    }
}
