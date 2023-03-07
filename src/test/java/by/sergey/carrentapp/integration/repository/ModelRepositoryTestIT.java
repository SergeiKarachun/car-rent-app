package by.sergey.carrentapp.integration.repository;

import by.sergey.carrentapp.domain.entity.Brand;
import by.sergey.carrentapp.domain.entity.Car;
import by.sergey.carrentapp.domain.entity.Model;
import by.sergey.carrentapp.domain.model.Transmission;
import by.sergey.carrentapp.integration.annatation.IT;
import by.sergey.carrentapp.integration.utils.TestEntityIdConst;
import by.sergey.carrentapp.integration.utils.builder.ExistsEntityBuilder;
import by.sergey.carrentapp.integration.utils.builder.TestEntityBuilder;
import by.sergey.carrentapp.repository.BrandRepository;
import by.sergey.carrentapp.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static by.sergey.carrentapp.integration.utils.TestEntityIdConst.MODEL_ID_FOR_DELETE;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
class ModelRepositoryTestIT {

    private final ModelRepository modelRepository;

    private final BrandRepository brandRepository;

    @Test
    void saveModel() {
        Model model = TestEntityBuilder.createModel();
        Brand brand = brandRepository.findById(TestEntityIdConst.EXISTS_BRAND_ID).get();
        brand.setModel(model);

        Model savedModel = modelRepository.saveAndFlush(model);

        assertThat(savedModel.getBrand().getName()).isEqualTo("Audi");
        assertEquals(TestEntityIdConst.EXISTS_BRAND_ID, savedModel.getBrand().getId());
    }

    @Test
    void saveModelWithNotExistedBrand() {
        Brand brand = TestEntityBuilder.createBrand();
        Model model = TestEntityBuilder.createModel();

        brand.setModel(model);

        Brand savedBrand = brandRepository.saveAndFlush(brand);

        Model savedModel = modelRepository.saveAndFlush(model);

        assertThat(savedModel.getId()).isNotNull();
        assertThat(savedModel.getBrand().getId()).isNotNull();
    }

    @Test
    void saveModelWithNotExistedCar() {
        Brand existBrand = ExistsEntityBuilder.getExistBrand();
        Model modelToSave = TestEntityBuilder.createModel();
        Car carToSave = TestEntityBuilder.createCar();
        existBrand.setModel(modelToSave);
        carToSave.setModel(modelToSave);

        modelRepository.saveAndFlush(modelToSave);

        assertThat(modelToSave.getId()).isNotNull();
        assertThat(carToSave.getId()).isNotNull();
        assertThat(modelToSave.getCars()).contains(carToSave);
        assertThat(carToSave.getModel().getId()).isEqualTo(modelToSave.getId());
    }

    @Test
    void updateModel() {
        Model modelTpUpdate = ExistsEntityBuilder.getExistModel();
        modelTpUpdate.setName("S6");
        modelTpUpdate.setTransmission(Transmission.AUTOMATIC);

        modelRepository.saveAndFlush(modelTpUpdate);
        Model updatedModel = modelRepository.findById(modelTpUpdate.getId()).get();

        assertThat(updatedModel).isEqualTo(modelTpUpdate);
    }

    @Test
    void deleteModel() {
        Optional<Model> model = modelRepository.findById(MODEL_ID_FOR_DELETE);

        model.ifPresent(md -> modelRepository.delete(md));

        assertThat(modelRepository.findById(MODEL_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void findModelByName() {
        Optional<Model> a6 = modelRepository.findModelByNameIgnoreCase("a6");
        Optional<Model> existModel = Optional.of(ExistsEntityBuilder.getExistModel());

        assertEquals(existModel, a6);
    }

    @Test
    void findModelsByBrandName() {
        List<Model> audi = modelRepository.findModelsByBrandName("audi");

        List<String> actual = audi.stream().map(Model::getName).collect(Collectors.toList());

        assertThat(actual).hasSize(2).containsExactlyInAnyOrder("A8", "A6");
    }

    @Test
    void findModelsByBrandId() {
        List<Model> actual = modelRepository.findModelsByBrandId(TestEntityIdConst.EXISTS_BRAND_ID);

        List<String> actualName = actual.stream().map(Model::getName).collect(Collectors.toList());

        assertThat(actualName).hasSize(2).containsExactlyInAnyOrder("A8", "A6");
    }

    @Test
    void findAllByTransmission() {
        List<Model> allByTransmission = modelRepository.findAllByTransmission(Transmission.MANUAL);

        List<String> actualModels = allByTransmission.stream().map(Model::getName).collect(Collectors.toList());

        assertThat(actualModels).hasSize(3).containsExactlyInAnyOrder("A6", "Superb", "Rapid");
    }
}