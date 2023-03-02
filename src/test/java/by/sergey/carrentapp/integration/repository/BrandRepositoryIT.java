package by.sergey.carrentapp.integration.repository;

import by.sergey.carrentapp.CarRentApplication;
import by.sergey.carrentapp.domain.entity.Brand;
import by.sergey.carrentapp.integration.IntegrationBaseTest;
import by.sergey.carrentapp.integration.annotation.IT;
import by.sergey.carrentapp.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CarRentApplication.class,
                        initializers = ConfigDataApplicationContextInitializer.class)*/

@RequiredArgsConstructor
//@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class BrandRepositoryIT extends IntegrationBaseTest {

    private final BrandRepository brandRepository;
    private final EntityManager entityManager;

    @Test
    void findById(){

        Brand brand = entityManager.find(Brand.class, 3L);


        Optional<Brand> bmw = brandRepository.findByNameIgnoreCase("bmw");
        assertTrue(bmw.isPresent());
        assertEquals(brand, bmw.get());
        System.out.println(bmw);
    }
}
