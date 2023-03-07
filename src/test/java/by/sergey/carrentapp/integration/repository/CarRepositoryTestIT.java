package by.sergey.carrentapp.integration.repository;

import by.sergey.carrentapp.domain.entity.Brand;
import by.sergey.carrentapp.domain.entity.Car;
import by.sergey.carrentapp.domain.entity.Category;
import by.sergey.carrentapp.domain.entity.Model;
import by.sergey.carrentapp.domain.model.Color;
import by.sergey.carrentapp.integration.annatation.IT;
import by.sergey.carrentapp.integration.utils.builder.ExistsEntityBuilder;
import by.sergey.carrentapp.integration.utils.builder.TestEntityBuilder;
import by.sergey.carrentapp.repository.BrandRepository;
import by.sergey.carrentapp.repository.CarRepository;
import by.sergey.carrentapp.repository.CategoryRepository;
import by.sergey.carrentapp.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static by.sergey.carrentapp.integration.utils.TestEntityIdConst.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@IT
@RequiredArgsConstructor
class CarRepositoryTestIT {

    private final BrandRepository brandRepository;
    private final ModelRepository modelRepository;
    private final CategoryRepository categoryRepository;
    private final CarRepository carRepository;

    @Test
    void saveCarWithoutExistedBrandAndModelAndCategory() {
        Car car = TestEntityBuilder.createCar();
        Brand brand = TestEntityBuilder.createBrand();
        Model model = TestEntityBuilder.createModel();
        Category category = TestEntityBuilder.createCategory();

        brandRepository.saveAndFlush(brand);
        brand.setModel(model);
        modelRepository.saveAndFlush(model);
        categoryRepository.saveAndFlush(category);
        //car.setBrand(brand);
        car.setModel(model);
        car.setCategory(category);

        Car savedCar = carRepository.saveAndFlush(car);
        assertThat(savedCar.getBrand().getId()).isNotNull();
        assertThat(savedCar.getModel().getId()).isNotNull();
        assertThat(savedCar.getCategory().getId()).isNotNull();
        assertThat(savedCar.getModel().getBrand().getId()).isNotNull();
        assertThat(savedCar.getId()).isNotNull();
    }

    @Test
    void saveCar() {
        Car car = TestEntityBuilder.createCar();
        car.setModel(ExistsEntityBuilder.getExistModel());
        car.setCategory(ExistsEntityBuilder.getExistCategory());

        Car savedCar = carRepository.saveAndFlush(car);

        assertThat(savedCar.getBrand().getId()).isNotNull();
        assertThat(savedCar.getModel().getId()).isNotNull();
        assertThat(savedCar.getCategory().getId()).isNotNull();
        assertThat(savedCar.getModel().getBrand().getId()).isNotNull();
        assertThat(savedCar.getId()).isNotNull();
    }

    @Test
    void updateCar() {
        Car carToUpdate = carRepository.findById(EXISTS_CAR_ID).get();
        Model existModel = modelRepository.findById(3L).get();
        Category existCategory = categoryRepository.findById(EXISTS_CATEGORY_ID).get();

        carToUpdate.setColor(Color.RED);
        carToUpdate.setModel(existModel);
        carToUpdate.setCategory(existCategory);

        Car updatedCar = carRepository.saveAndFlush(carToUpdate);

        assertThat(updatedCar).isEqualTo(carToUpdate);
    }

    @Test
    void findById() {
        Optional<Car> actual = carRepository.findById(EXISTS_CAR_ID);

        actual.ifPresent(car -> assertEquals(ExistsEntityBuilder.getExistCar(), car));
    }

    @Test
    void deleteCar() {
        Optional<Car> carToDelete = carRepository.findById(CAR_ID_FOR_DELETE);
        carToDelete.ifPresent(carRepository::delete);

        assertThat(carRepository.findById(CAR_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void findCarByCarNumberIgnoreCase() {
        Optional<Car> actual = carRepository.findCarByCarNumberIgnoreCase("7594aB-7");
        Car existCar = ExistsEntityBuilder.getExistCar();
        actual.ifPresent(car -> assertEquals(existCar, car));
    }

    @Test
    void findAllByCategoryNameIgnoreCase() {
        List<Car> cars = carRepository.findAllByCategoryNameIgnoreCase("sPorT");

        assertThat(cars).hasSize(2);
    }

    @Test
    void findAllByTransmission() {
        List<Car> actualCars = carRepository.findAllByTransmission("maNual");

        assertThat(actualCars).hasSize(3).containsExactlyInAnyOrderElementsOf(carRepository.findAllById(List.of(1L, 5L, 6L)));
    }

    @Test
    void findAllByOrderId() {
        List<Car> actualCars = carRepository.findAllByOrderId(EXISTS_ORDER_ID);
        Car existCar = ExistsEntityBuilder.getExistCar();
        assertThat(actualCars).hasSize(1).containsExactlyInAnyOrder(existCar);
    }

    @Test
    void findAllByYearBefore() {
        List<Car> actualCars = carRepository.findAllByYearBefore(2020);
        assertThat(actualCars).hasSize(3);
    }

    @Test
    void findAllRepaired() {
        List<Car> actualCars = carRepository.findAllRepaired();
        assertThat(actualCars).hasSize(8);
    }

    @Test
    void findAllBroken() {
        List<Car> allBroken = carRepository.findAllBroken();
        assertThat(allBroken).isEmpty();
    }

    @Test
    void isCarAvailable() {
        Car existCar = ExistsEntityBuilder.getExistCar();

        boolean carAvailable = carRepository.isCarAvailable(existCar.getId(), LocalDateTime.of(2022, 7, 12, 0, 0, 0),
                LocalDateTime.of(20022, 7, 12, 11, 30, 0));
        assertFalse(carAvailable);

        boolean carAvailable2 = carRepository.isCarAvailable(existCar.getId(), LocalDateTime.of(2022, 7, 14, 0, 0, 0),
                LocalDateTime.of(20022, 7, 15, 11, 30, 0));
        assertTrue(carAvailable2);

        boolean carAvailable3 = carRepository.isCarAvailable(existCar.getId(), LocalDateTime.of(2022, 7, 10, 0, 0, 0),
                LocalDateTime.of(20022, 7, 11, 11, 30, 0));
        assertFalse(carAvailable3);

        boolean carAvailable4 = carRepository.isCarAvailable(existCar.getId(), LocalDateTime.of(2022, 7, 11, 1, 0, 0),
                LocalDateTime.of(20022, 7, 11, 11, 30, 0));
        assertFalse(carAvailable4);

        boolean carAvailable5 = carRepository.isCarAvailable(existCar.getId(), LocalDateTime.of(2022, 7, 8, 1, 0, 0),
                LocalDateTime.of(20022, 7, 10, 11, 30, 0));
        assertFalse(carAvailable5);
    }
}