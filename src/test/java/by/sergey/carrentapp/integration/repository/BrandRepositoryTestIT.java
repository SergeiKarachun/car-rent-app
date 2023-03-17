package by.sergey.carrentapp.integration.repository;


import by.sergey.carrentapp.domain.entity.Brand;
import by.sergey.carrentapp.domain.entity.Model;
import by.sergey.carrentapp.domain.projection.BrandFullView;
import by.sergey.carrentapp.integration.annatation.IT;
import by.sergey.carrentapp.integration.utils.builder.ExistsEntityBuilder;
import by.sergey.carrentapp.integration.utils.builder.TestEntityBuilder;
import by.sergey.carrentapp.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static by.sergey.carrentapp.integration.utils.TestEntityIdConst.BRAND_ID_FOR_DELETE;
import static by.sergey.carrentapp.integration.utils.TestEntityIdConst.EXISTS_BRAND_ID;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/*@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CarRentApplication.class,
initializers = ConfigDataApplicationContextInitializer.class) equals SpringBootTest */
@IT
@RequiredArgsConstructor
//@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BrandRepositoryTestIT {

    private final BrandRepository brandRepository;

    @Test
    void saveBrand(){
        Brand brandToSave = TestEntityBuilder.createBrand();
        Brand savedBrand = brandRepository.saveAndFlush(brandToSave);
        assertNotNull(savedBrand.getId());
    }

    @Test
    void checkUpdateBrand() {
        var brandToUpdate = brandRepository.findById(EXISTS_BRAND_ID).get();
        brandToUpdate.setName("chery");

        brandRepository.saveAndFlush(brandToUpdate);

        var updatedBrand = brandRepository.findById(brandToUpdate.getId()).get();
        assertThat(updatedBrand).isEqualTo(brandToUpdate);
        assertEquals(brandToUpdate.getName(), updatedBrand.getModels().stream().map(Model::getBrand).findFirst().get().getName());
    }



    @Test
    void checkDeleteBrand() {
        Optional<Brand> brandToDelete = brandRepository.findById(BRAND_ID_FOR_DELETE);

        brandToDelete.ifPresent(br -> brandRepository.delete(br));

        assertThat(brandRepository.findById(BRAND_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void findByName() {
        Brand expectedBrand = ExistsEntityBuilder.getExistBrand();
        var brand = Optional.of(ExistsEntityBuilder.getExistBrand());
        Optional<Brand> actualBrand = brandRepository.findByName(expectedBrand.getName());
        actualBrand.ifPresent(actual -> assertEquals(expectedBrand, actual));
    }

    @Test
    void findByNameIgnoreCase() {
        Optional<Brand> actual = brandRepository.findByNameIgnoreCase("AUDi");
        Optional<Brand> expect = Optional.of(ExistsEntityBuilder.getExistBrand());

        assertThat(actual).isNotNull();
        assertEquals(expect, actual);
    }

    @Test
    void findAllByNameContainingIgnoreCase() {
        List<Brand> actual = brandRepository.findAllByNameContainingIgnoreCase("Di");
        Optional<Brand> exist = Optional.of(ExistsEntityBuilder.getExistBrand());

        assertThat(actual).isNotNull();
        assertEquals(exist, actual.stream().findFirst());
    }

    @Test
    void findByNameInIgnoreCase() {
        List<Brand> actual = brandRepository.findByNameInIgnoreCase(List.of("audi", "reno"));
        Brand existBrand = ExistsEntityBuilder.getExistBrand();
        assertThat(actual).hasSize(1);
        assertEquals(existBrand, actual.get(0));
    }

    @Test
    void findAllByFullView() {
        List<BrandFullView> actual = brandRepository.findAllByFullView();
        assertThat(actual).hasSize(8);

        List<String> modelNames = actual.stream().map(BrandFullView::getModelName).toList();

        assertThat(modelNames).hasSize(8).containsExactlyInAnyOrder(
                "A6",
                "A8",
                "525",
                "M5",
                "Rapid",
                "Superb",
                "Model S",
                "Model X");
    }

    @Test
    void findFullViewById() {
        List<BrandFullView> actual = brandRepository.findFullViewById(EXISTS_BRAND_ID);

        assertThat(actual).hasSize(2);
        List<String> models = actual.stream().map(BrandFullView::getModelName).collect(Collectors.toList());
        assertThat(models).hasSize(2).containsExactlyInAnyOrder("A6", "A8");
    }

    @Test
    void findAllFullViewByName() {
        List<BrandFullView> brands = brandRepository.findAllFullViewByName("bmw");

        List<String> models = brands.stream().map(BrandFullView::getModelName)
                .collect(Collectors.toList());

        assertThat(models).hasSize(2).containsExactlyInAnyOrder("525", "M5");
    }
}