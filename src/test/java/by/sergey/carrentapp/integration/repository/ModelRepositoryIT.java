package by.sergey.carrentapp.integration.repository;

import by.sergey.carrentapp.domain.entity.Model;
import by.sergey.carrentapp.integration.IntegrationBaseTest;
import by.sergey.carrentapp.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

@RequiredArgsConstructor
public class ModelRepositoryIT extends IntegrationBaseTest {

    private final ModelRepository modelRepository;

    @Test
    void findById(){
        Optional<Model> model = modelRepository.findById(1L);
        boolean present = model.isPresent();
    }
}
