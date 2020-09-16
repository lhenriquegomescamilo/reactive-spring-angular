package com.loiane.springshoppingcart.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.Disposable;

import java.util.Objects;
import java.util.function.Function;

@Slf4j
public class RepositoryUtil {

    private RepositoryUtil() {
        throw new IllegalArgumentException("Class cannot be instantiate");
    }

    public static <T> Function<T, Disposable> save(final ReactiveMongoRepository<T, String> repository) {
        return record -> repository.save(record).map(Objects::toString)
                .subscribe(log::info);
    }


    public static <T> CommandLineRunner deleteAll(
            final ReactiveMongoRepository<T, String> repository,
            final Function<ReactiveMongoRepository<T, String>, Runnable> runnable
    ) {

        return args -> repository.deleteAll().subscribe(
                null,
                null,
                runnable.apply(repository)
        );
    }
}
