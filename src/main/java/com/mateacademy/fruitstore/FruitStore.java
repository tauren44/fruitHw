package com.mateacademy.fruitstore;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FruitStore {
    private static final Logger LOGGER = Logger.getLogger(FruitStore.class);
    private List<Fruit> fruitsStorage = new ArrayList<>();
    private ObjectMapper mapper = new ObjectMapper();

    public FruitStore() {

    }

    public void addFruits(String pathToJsonFile) {
        List<Fruit> supply;
        try (FileInputStream inputStream = new FileInputStream(pathToJsonFile)) {
            supply = mapper.readValue(inputStream, new TypeReference<List<Fruit>>() {
            });
            fruitsStorage.addAll(supply);
        } catch (IOException e) {
            LOGGER.info("Exception thrown: ", e);
        }
    }

    public void save(String pathToJsonFile) {
        try (FileOutputStream outputStream = new FileOutputStream(pathToJsonFile)) {
            mapper.writeValue(outputStream, fruitsStorage);
        } catch (IOException e) {
            LOGGER.info("Exception thrown: ", e);
        }
    }

    public void load(String pathToJsonFile) {
        fruitsStorage.clear();
        try (FileInputStream inputStream = new FileInputStream(pathToJsonFile)) {
            fruitsStorage = mapper.readValue(inputStream, new TypeReference<List<Fruit>>() {
            });
        } catch (IOException e) {
            LOGGER.info("Exception thrown: ", e);
        }
    }

    public List<Fruit> getSpoiledFruits(LocalDate targetDate) {
        return filterByDate(targetDate, true);
    }

    public List<Fruit> getSpoiledFruits(LocalDate targetDate, FruitType type) {
        return filterByType(type, getSpoiledFruits(targetDate));
    }

    public List<Fruit> getAvailableFruits(LocalDate targetDate) {
        return filterByDate(targetDate, false);
    }

    public List<Fruit> getAvailableFruits(LocalDate targetDate, FruitType type) {
        return filterByType(type, getAvailableFruits(targetDate));
    }

    public List<Fruit> getAddedFruits(LocalDate date) {
        List<Fruit> listOfAddedFruits = new ArrayList<>();
        fruitsStorage.forEach(fruit -> {
            LocalDate dateOfDelivery = fruit.convertStringToDate(fruit.getDateOfDelivery());
            if (dateOfDelivery.isEqual(date)) {
                listOfAddedFruits.add(fruit);
            }
        });
        return listOfAddedFruits;
    }

    public List<Fruit> getAddedFruits(LocalDate date, FruitType type) {
        return filterByType(type, getAddedFruits(date));
    }

    private List<Fruit> filterByDate(LocalDate targetDate, boolean spoiled) {
        List<Fruit> listOfFruits = new ArrayList<>();
        fruitsStorage.forEach(fruit -> {
            LocalDate dateOfSpoil = fruit.getDateOfSpoil();
            if (spoiled) {
                if (targetDate.isAfter(dateOfSpoil)) {
                    listOfFruits.add(fruit);
                }
            } else {
                if (targetDate.isBefore(dateOfSpoil) || targetDate.isEqual(dateOfSpoil)) {
                    listOfFruits.add(fruit);
                }
            }
        });

        return listOfFruits;
    }

    private List<Fruit> filterByType(FruitType type, List<Fruit> list) {
        List<Fruit> listOfFruits = list.stream().filter(fruit -> fruit.getType()
                .equals(type)).collect(Collectors.toList());
        return listOfFruits;
    }

    public List<Fruit> getFruitsStorage() {
        return fruitsStorage;
    }
}
