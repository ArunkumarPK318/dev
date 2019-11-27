package com.jhipster.myapplication.web.rest;

import com.jhipster.myapplication.MyapplivationApp;
import com.jhipster.myapplication.domain.Car;
import com.jhipster.myapplication.domain.Employee;
import com.jhipster.myapplication.repository.CarRepository;
import com.jhipster.myapplication.service.CarService;
import com.jhipster.myapplication.web.rest.errors.ExceptionTranslator;
import com.jhipster.myapplication.service.dto.CarCriteria;
import com.jhipster.myapplication.service.CarQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.jhipster.myapplication.web.rest.TestUtil.sameInstant;
import static com.jhipster.myapplication.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CarResource} REST controller.
 */
@SpringBootTest(classes = MyapplivationApp.class)
public class CarResourceIT {

    private static final String DEFAULT_CAR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CAR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CAR_MODAL = "AAAAAAAAAA";
    private static final String UPDATED_CAR_MODAL = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BRAND_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ENGIN_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ENGIN_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ENGINE_NO = 1;
    private static final Integer UPDATED_ENGINE_NO = 2;
    private static final Integer SMALLER_ENGINE_NO = 1 - 1;

    private static final ZonedDateTime DEFAULT_MANUFACTURE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MANUFACTURE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_MANUFACTURE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarService carService;

    @Autowired
    private CarQueryService carQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCarMockMvc;

    private Car car;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CarResource carResource = new CarResource(carService, carQueryService);
        this.restCarMockMvc = MockMvcBuilders.standaloneSetup(carResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createEntity(EntityManager em) {
        Car car = new Car()
            .carName(DEFAULT_CAR_NAME)
            .carModal(DEFAULT_CAR_MODAL)
            .brandName(DEFAULT_BRAND_NAME)
            .enginType(DEFAULT_ENGIN_TYPE)
            .engineNo(DEFAULT_ENGINE_NO)
            .manufactureDate(DEFAULT_MANUFACTURE_DATE);
        return car;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createUpdatedEntity(EntityManager em) {
        Car car = new Car()
            .carName(UPDATED_CAR_NAME)
            .carModal(UPDATED_CAR_MODAL)
            .brandName(UPDATED_BRAND_NAME)
            .enginType(UPDATED_ENGIN_TYPE)
            .engineNo(UPDATED_ENGINE_NO)
            .manufactureDate(UPDATED_MANUFACTURE_DATE);
        return car;
    }

    @BeforeEach
    public void initTest() {
        car = createEntity(em);
    }

    @Test
    @Transactional
    public void createCar() throws Exception {
        int databaseSizeBeforeCreate = carRepository.findAll().size();

        // Create the Car
        restCarMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isCreated());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate + 1);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getCarName()).isEqualTo(DEFAULT_CAR_NAME);
        assertThat(testCar.getCarModal()).isEqualTo(DEFAULT_CAR_MODAL);
        assertThat(testCar.getBrandName()).isEqualTo(DEFAULT_BRAND_NAME);
        assertThat(testCar.getEnginType()).isEqualTo(DEFAULT_ENGIN_TYPE);
        assertThat(testCar.getEngineNo()).isEqualTo(DEFAULT_ENGINE_NO);
        assertThat(testCar.getManufactureDate()).isEqualTo(DEFAULT_MANUFACTURE_DATE);
    }

    @Test
    @Transactional
    public void createCarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carRepository.findAll().size();

        // Create the Car with an existing ID
        car.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarMockMvc.perform(post("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCars() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList
        restCarMockMvc.perform(get("/api/cars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].carName").value(hasItem(DEFAULT_CAR_NAME)))
            .andExpect(jsonPath("$.[*].carModal").value(hasItem(DEFAULT_CAR_MODAL)))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].enginType").value(hasItem(DEFAULT_ENGIN_TYPE)))
            .andExpect(jsonPath("$.[*].engineNo").value(hasItem(DEFAULT_ENGINE_NO)))
            .andExpect(jsonPath("$.[*].manufactureDate").value(hasItem(sameInstant(DEFAULT_MANUFACTURE_DATE))));
    }
    
    @Test
    @Transactional
    public void getCar() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get the car
        restCarMockMvc.perform(get("/api/cars/{id}", car.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(car.getId().intValue()))
            .andExpect(jsonPath("$.carName").value(DEFAULT_CAR_NAME))
            .andExpect(jsonPath("$.carModal").value(DEFAULT_CAR_MODAL))
            .andExpect(jsonPath("$.brandName").value(DEFAULT_BRAND_NAME))
            .andExpect(jsonPath("$.enginType").value(DEFAULT_ENGIN_TYPE))
            .andExpect(jsonPath("$.engineNo").value(DEFAULT_ENGINE_NO))
            .andExpect(jsonPath("$.manufactureDate").value(sameInstant(DEFAULT_MANUFACTURE_DATE)));
    }


    @Test
    @Transactional
    public void getCarsByIdFiltering() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        Long id = car.getId();

        defaultCarShouldBeFound("id.equals=" + id);
        defaultCarShouldNotBeFound("id.notEquals=" + id);

        defaultCarShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCarShouldNotBeFound("id.greaterThan=" + id);

        defaultCarShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCarShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCarsByCarNameIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carName equals to DEFAULT_CAR_NAME
        defaultCarShouldBeFound("carName.equals=" + DEFAULT_CAR_NAME);

        // Get all the carList where carName equals to UPDATED_CAR_NAME
        defaultCarShouldNotBeFound("carName.equals=" + UPDATED_CAR_NAME);
    }

    @Test
    @Transactional
    public void getAllCarsByCarNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carName not equals to DEFAULT_CAR_NAME
        defaultCarShouldNotBeFound("carName.notEquals=" + DEFAULT_CAR_NAME);

        // Get all the carList where carName not equals to UPDATED_CAR_NAME
        defaultCarShouldBeFound("carName.notEquals=" + UPDATED_CAR_NAME);
    }

    @Test
    @Transactional
    public void getAllCarsByCarNameIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carName in DEFAULT_CAR_NAME or UPDATED_CAR_NAME
        defaultCarShouldBeFound("carName.in=" + DEFAULT_CAR_NAME + "," + UPDATED_CAR_NAME);

        // Get all the carList where carName equals to UPDATED_CAR_NAME
        defaultCarShouldNotBeFound("carName.in=" + UPDATED_CAR_NAME);
    }

    @Test
    @Transactional
    public void getAllCarsByCarNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carName is not null
        defaultCarShouldBeFound("carName.specified=true");

        // Get all the carList where carName is null
        defaultCarShouldNotBeFound("carName.specified=false");
    }
                @Test
    @Transactional
    public void getAllCarsByCarNameContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carName contains DEFAULT_CAR_NAME
        defaultCarShouldBeFound("carName.contains=" + DEFAULT_CAR_NAME);

        // Get all the carList where carName contains UPDATED_CAR_NAME
        defaultCarShouldNotBeFound("carName.contains=" + UPDATED_CAR_NAME);
    }

    @Test
    @Transactional
    public void getAllCarsByCarNameNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carName does not contain DEFAULT_CAR_NAME
        defaultCarShouldNotBeFound("carName.doesNotContain=" + DEFAULT_CAR_NAME);

        // Get all the carList where carName does not contain UPDATED_CAR_NAME
        defaultCarShouldBeFound("carName.doesNotContain=" + UPDATED_CAR_NAME);
    }


    @Test
    @Transactional
    public void getAllCarsByCarModalIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carModal equals to DEFAULT_CAR_MODAL
        defaultCarShouldBeFound("carModal.equals=" + DEFAULT_CAR_MODAL);

        // Get all the carList where carModal equals to UPDATED_CAR_MODAL
        defaultCarShouldNotBeFound("carModal.equals=" + UPDATED_CAR_MODAL);
    }

    @Test
    @Transactional
    public void getAllCarsByCarModalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carModal not equals to DEFAULT_CAR_MODAL
        defaultCarShouldNotBeFound("carModal.notEquals=" + DEFAULT_CAR_MODAL);

        // Get all the carList where carModal not equals to UPDATED_CAR_MODAL
        defaultCarShouldBeFound("carModal.notEquals=" + UPDATED_CAR_MODAL);
    }

    @Test
    @Transactional
    public void getAllCarsByCarModalIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carModal in DEFAULT_CAR_MODAL or UPDATED_CAR_MODAL
        defaultCarShouldBeFound("carModal.in=" + DEFAULT_CAR_MODAL + "," + UPDATED_CAR_MODAL);

        // Get all the carList where carModal equals to UPDATED_CAR_MODAL
        defaultCarShouldNotBeFound("carModal.in=" + UPDATED_CAR_MODAL);
    }

    @Test
    @Transactional
    public void getAllCarsByCarModalIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carModal is not null
        defaultCarShouldBeFound("carModal.specified=true");

        // Get all the carList where carModal is null
        defaultCarShouldNotBeFound("carModal.specified=false");
    }
                @Test
    @Transactional
    public void getAllCarsByCarModalContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carModal contains DEFAULT_CAR_MODAL
        defaultCarShouldBeFound("carModal.contains=" + DEFAULT_CAR_MODAL);

        // Get all the carList where carModal contains UPDATED_CAR_MODAL
        defaultCarShouldNotBeFound("carModal.contains=" + UPDATED_CAR_MODAL);
    }

    @Test
    @Transactional
    public void getAllCarsByCarModalNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where carModal does not contain DEFAULT_CAR_MODAL
        defaultCarShouldNotBeFound("carModal.doesNotContain=" + DEFAULT_CAR_MODAL);

        // Get all the carList where carModal does not contain UPDATED_CAR_MODAL
        defaultCarShouldBeFound("carModal.doesNotContain=" + UPDATED_CAR_MODAL);
    }


    @Test
    @Transactional
    public void getAllCarsByBrandNameIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where brandName equals to DEFAULT_BRAND_NAME
        defaultCarShouldBeFound("brandName.equals=" + DEFAULT_BRAND_NAME);

        // Get all the carList where brandName equals to UPDATED_BRAND_NAME
        defaultCarShouldNotBeFound("brandName.equals=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    public void getAllCarsByBrandNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where brandName not equals to DEFAULT_BRAND_NAME
        defaultCarShouldNotBeFound("brandName.notEquals=" + DEFAULT_BRAND_NAME);

        // Get all the carList where brandName not equals to UPDATED_BRAND_NAME
        defaultCarShouldBeFound("brandName.notEquals=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    public void getAllCarsByBrandNameIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where brandName in DEFAULT_BRAND_NAME or UPDATED_BRAND_NAME
        defaultCarShouldBeFound("brandName.in=" + DEFAULT_BRAND_NAME + "," + UPDATED_BRAND_NAME);

        // Get all the carList where brandName equals to UPDATED_BRAND_NAME
        defaultCarShouldNotBeFound("brandName.in=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    public void getAllCarsByBrandNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where brandName is not null
        defaultCarShouldBeFound("brandName.specified=true");

        // Get all the carList where brandName is null
        defaultCarShouldNotBeFound("brandName.specified=false");
    }
                @Test
    @Transactional
    public void getAllCarsByBrandNameContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where brandName contains DEFAULT_BRAND_NAME
        defaultCarShouldBeFound("brandName.contains=" + DEFAULT_BRAND_NAME);

        // Get all the carList where brandName contains UPDATED_BRAND_NAME
        defaultCarShouldNotBeFound("brandName.contains=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    public void getAllCarsByBrandNameNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where brandName does not contain DEFAULT_BRAND_NAME
        defaultCarShouldNotBeFound("brandName.doesNotContain=" + DEFAULT_BRAND_NAME);

        // Get all the carList where brandName does not contain UPDATED_BRAND_NAME
        defaultCarShouldBeFound("brandName.doesNotContain=" + UPDATED_BRAND_NAME);
    }


    @Test
    @Transactional
    public void getAllCarsByEnginTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where enginType equals to DEFAULT_ENGIN_TYPE
        defaultCarShouldBeFound("enginType.equals=" + DEFAULT_ENGIN_TYPE);

        // Get all the carList where enginType equals to UPDATED_ENGIN_TYPE
        defaultCarShouldNotBeFound("enginType.equals=" + UPDATED_ENGIN_TYPE);
    }

    @Test
    @Transactional
    public void getAllCarsByEnginTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where enginType not equals to DEFAULT_ENGIN_TYPE
        defaultCarShouldNotBeFound("enginType.notEquals=" + DEFAULT_ENGIN_TYPE);

        // Get all the carList where enginType not equals to UPDATED_ENGIN_TYPE
        defaultCarShouldBeFound("enginType.notEquals=" + UPDATED_ENGIN_TYPE);
    }

    @Test
    @Transactional
    public void getAllCarsByEnginTypeIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where enginType in DEFAULT_ENGIN_TYPE or UPDATED_ENGIN_TYPE
        defaultCarShouldBeFound("enginType.in=" + DEFAULT_ENGIN_TYPE + "," + UPDATED_ENGIN_TYPE);

        // Get all the carList where enginType equals to UPDATED_ENGIN_TYPE
        defaultCarShouldNotBeFound("enginType.in=" + UPDATED_ENGIN_TYPE);
    }

    @Test
    @Transactional
    public void getAllCarsByEnginTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where enginType is not null
        defaultCarShouldBeFound("enginType.specified=true");

        // Get all the carList where enginType is null
        defaultCarShouldNotBeFound("enginType.specified=false");
    }
                @Test
    @Transactional
    public void getAllCarsByEnginTypeContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where enginType contains DEFAULT_ENGIN_TYPE
        defaultCarShouldBeFound("enginType.contains=" + DEFAULT_ENGIN_TYPE);

        // Get all the carList where enginType contains UPDATED_ENGIN_TYPE
        defaultCarShouldNotBeFound("enginType.contains=" + UPDATED_ENGIN_TYPE);
    }

    @Test
    @Transactional
    public void getAllCarsByEnginTypeNotContainsSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where enginType does not contain DEFAULT_ENGIN_TYPE
        defaultCarShouldNotBeFound("enginType.doesNotContain=" + DEFAULT_ENGIN_TYPE);

        // Get all the carList where enginType does not contain UPDATED_ENGIN_TYPE
        defaultCarShouldBeFound("enginType.doesNotContain=" + UPDATED_ENGIN_TYPE);
    }


    @Test
    @Transactional
    public void getAllCarsByEngineNoIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where engineNo equals to DEFAULT_ENGINE_NO
        defaultCarShouldBeFound("engineNo.equals=" + DEFAULT_ENGINE_NO);

        // Get all the carList where engineNo equals to UPDATED_ENGINE_NO
        defaultCarShouldNotBeFound("engineNo.equals=" + UPDATED_ENGINE_NO);
    }

    @Test
    @Transactional
    public void getAllCarsByEngineNoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where engineNo not equals to DEFAULT_ENGINE_NO
        defaultCarShouldNotBeFound("engineNo.notEquals=" + DEFAULT_ENGINE_NO);

        // Get all the carList where engineNo not equals to UPDATED_ENGINE_NO
        defaultCarShouldBeFound("engineNo.notEquals=" + UPDATED_ENGINE_NO);
    }

    @Test
    @Transactional
    public void getAllCarsByEngineNoIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where engineNo in DEFAULT_ENGINE_NO or UPDATED_ENGINE_NO
        defaultCarShouldBeFound("engineNo.in=" + DEFAULT_ENGINE_NO + "," + UPDATED_ENGINE_NO);

        // Get all the carList where engineNo equals to UPDATED_ENGINE_NO
        defaultCarShouldNotBeFound("engineNo.in=" + UPDATED_ENGINE_NO);
    }

    @Test
    @Transactional
    public void getAllCarsByEngineNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where engineNo is not null
        defaultCarShouldBeFound("engineNo.specified=true");

        // Get all the carList where engineNo is null
        defaultCarShouldNotBeFound("engineNo.specified=false");
    }

    @Test
    @Transactional
    public void getAllCarsByEngineNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where engineNo is greater than or equal to DEFAULT_ENGINE_NO
        defaultCarShouldBeFound("engineNo.greaterThanOrEqual=" + DEFAULT_ENGINE_NO);

        // Get all the carList where engineNo is greater than or equal to UPDATED_ENGINE_NO
        defaultCarShouldNotBeFound("engineNo.greaterThanOrEqual=" + UPDATED_ENGINE_NO);
    }

    @Test
    @Transactional
    public void getAllCarsByEngineNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where engineNo is less than or equal to DEFAULT_ENGINE_NO
        defaultCarShouldBeFound("engineNo.lessThanOrEqual=" + DEFAULT_ENGINE_NO);

        // Get all the carList where engineNo is less than or equal to SMALLER_ENGINE_NO
        defaultCarShouldNotBeFound("engineNo.lessThanOrEqual=" + SMALLER_ENGINE_NO);
    }

    @Test
    @Transactional
    public void getAllCarsByEngineNoIsLessThanSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where engineNo is less than DEFAULT_ENGINE_NO
        defaultCarShouldNotBeFound("engineNo.lessThan=" + DEFAULT_ENGINE_NO);

        // Get all the carList where engineNo is less than UPDATED_ENGINE_NO
        defaultCarShouldBeFound("engineNo.lessThan=" + UPDATED_ENGINE_NO);
    }

    @Test
    @Transactional
    public void getAllCarsByEngineNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where engineNo is greater than DEFAULT_ENGINE_NO
        defaultCarShouldNotBeFound("engineNo.greaterThan=" + DEFAULT_ENGINE_NO);

        // Get all the carList where engineNo is greater than SMALLER_ENGINE_NO
        defaultCarShouldBeFound("engineNo.greaterThan=" + SMALLER_ENGINE_NO);
    }


    @Test
    @Transactional
    public void getAllCarsByManufactureDateIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufactureDate equals to DEFAULT_MANUFACTURE_DATE
        defaultCarShouldBeFound("manufactureDate.equals=" + DEFAULT_MANUFACTURE_DATE);

        // Get all the carList where manufactureDate equals to UPDATED_MANUFACTURE_DATE
        defaultCarShouldNotBeFound("manufactureDate.equals=" + UPDATED_MANUFACTURE_DATE);
    }

    @Test
    @Transactional
    public void getAllCarsByManufactureDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufactureDate not equals to DEFAULT_MANUFACTURE_DATE
        defaultCarShouldNotBeFound("manufactureDate.notEquals=" + DEFAULT_MANUFACTURE_DATE);

        // Get all the carList where manufactureDate not equals to UPDATED_MANUFACTURE_DATE
        defaultCarShouldBeFound("manufactureDate.notEquals=" + UPDATED_MANUFACTURE_DATE);
    }

    @Test
    @Transactional
    public void getAllCarsByManufactureDateIsInShouldWork() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufactureDate in DEFAULT_MANUFACTURE_DATE or UPDATED_MANUFACTURE_DATE
        defaultCarShouldBeFound("manufactureDate.in=" + DEFAULT_MANUFACTURE_DATE + "," + UPDATED_MANUFACTURE_DATE);

        // Get all the carList where manufactureDate equals to UPDATED_MANUFACTURE_DATE
        defaultCarShouldNotBeFound("manufactureDate.in=" + UPDATED_MANUFACTURE_DATE);
    }

    @Test
    @Transactional
    public void getAllCarsByManufactureDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufactureDate is not null
        defaultCarShouldBeFound("manufactureDate.specified=true");

        // Get all the carList where manufactureDate is null
        defaultCarShouldNotBeFound("manufactureDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCarsByManufactureDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufactureDate is greater than or equal to DEFAULT_MANUFACTURE_DATE
        defaultCarShouldBeFound("manufactureDate.greaterThanOrEqual=" + DEFAULT_MANUFACTURE_DATE);

        // Get all the carList where manufactureDate is greater than or equal to UPDATED_MANUFACTURE_DATE
        defaultCarShouldNotBeFound("manufactureDate.greaterThanOrEqual=" + UPDATED_MANUFACTURE_DATE);
    }

    @Test
    @Transactional
    public void getAllCarsByManufactureDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufactureDate is less than or equal to DEFAULT_MANUFACTURE_DATE
        defaultCarShouldBeFound("manufactureDate.lessThanOrEqual=" + DEFAULT_MANUFACTURE_DATE);

        // Get all the carList where manufactureDate is less than or equal to SMALLER_MANUFACTURE_DATE
        defaultCarShouldNotBeFound("manufactureDate.lessThanOrEqual=" + SMALLER_MANUFACTURE_DATE);
    }

    @Test
    @Transactional
    public void getAllCarsByManufactureDateIsLessThanSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufactureDate is less than DEFAULT_MANUFACTURE_DATE
        defaultCarShouldNotBeFound("manufactureDate.lessThan=" + DEFAULT_MANUFACTURE_DATE);

        // Get all the carList where manufactureDate is less than UPDATED_MANUFACTURE_DATE
        defaultCarShouldBeFound("manufactureDate.lessThan=" + UPDATED_MANUFACTURE_DATE);
    }

    @Test
    @Transactional
    public void getAllCarsByManufactureDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);

        // Get all the carList where manufactureDate is greater than DEFAULT_MANUFACTURE_DATE
        defaultCarShouldNotBeFound("manufactureDate.greaterThan=" + DEFAULT_MANUFACTURE_DATE);

        // Get all the carList where manufactureDate is greater than SMALLER_MANUFACTURE_DATE
        defaultCarShouldBeFound("manufactureDate.greaterThan=" + SMALLER_MANUFACTURE_DATE);
    }


    @Test
    @Transactional
    public void getAllCarsByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        carRepository.saveAndFlush(car);
        Employee employee = EmployeeResourceIT.createEntity(em);
        em.persist(employee);
        em.flush();
        car.setEmployee(employee);
        carRepository.saveAndFlush(car);
        Long employeeId = employee.getId();

        // Get all the carList where employee equals to employeeId
        defaultCarShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the carList where employee equals to employeeId + 1
        defaultCarShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCarShouldBeFound(String filter) throws Exception {
        restCarMockMvc.perform(get("/api/cars?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
            .andExpect(jsonPath("$.[*].carName").value(hasItem(DEFAULT_CAR_NAME)))
            .andExpect(jsonPath("$.[*].carModal").value(hasItem(DEFAULT_CAR_MODAL)))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].enginType").value(hasItem(DEFAULT_ENGIN_TYPE)))
            .andExpect(jsonPath("$.[*].engineNo").value(hasItem(DEFAULT_ENGINE_NO)))
            .andExpect(jsonPath("$.[*].manufactureDate").value(hasItem(sameInstant(DEFAULT_MANUFACTURE_DATE))));

        // Check, that the count call also returns 1
        restCarMockMvc.perform(get("/api/cars/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCarShouldNotBeFound(String filter) throws Exception {
        restCarMockMvc.perform(get("/api/cars?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCarMockMvc.perform(get("/api/cars/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCar() throws Exception {
        // Get the car
        restCarMockMvc.perform(get("/api/cars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCar() throws Exception {
        // Initialize the database
        carService.save(car);

        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Update the car
        Car updatedCar = carRepository.findById(car.getId()).get();
        // Disconnect from session so that the updates on updatedCar are not directly saved in db
        em.detach(updatedCar);
        updatedCar
            .carName(UPDATED_CAR_NAME)
            .carModal(UPDATED_CAR_MODAL)
            .brandName(UPDATED_BRAND_NAME)
            .enginType(UPDATED_ENGIN_TYPE)
            .engineNo(UPDATED_ENGINE_NO)
            .manufactureDate(UPDATED_MANUFACTURE_DATE);

        restCarMockMvc.perform(put("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCar)))
            .andExpect(status().isOk());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getCarName()).isEqualTo(UPDATED_CAR_NAME);
        assertThat(testCar.getCarModal()).isEqualTo(UPDATED_CAR_MODAL);
        assertThat(testCar.getBrandName()).isEqualTo(UPDATED_BRAND_NAME);
        assertThat(testCar.getEnginType()).isEqualTo(UPDATED_ENGIN_TYPE);
        assertThat(testCar.getEngineNo()).isEqualTo(UPDATED_ENGINE_NO);
        assertThat(testCar.getManufactureDate()).isEqualTo(UPDATED_MANUFACTURE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCar() throws Exception {
        int databaseSizeBeforeUpdate = carRepository.findAll().size();

        // Create the Car

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarMockMvc.perform(put("/api/cars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(car)))
            .andExpect(status().isBadRequest());

        // Validate the Car in the database
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCar() throws Exception {
        // Initialize the database
        carService.save(car);

        int databaseSizeBeforeDelete = carRepository.findAll().size();

        // Delete the car
        restCarMockMvc.perform(delete("/api/cars/{id}", car.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Car> carList = carRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
