package com.mateacademy.fruitstore;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class FruitStoreTest {
    private static final String FIRST_FILE_PATH = "firstFile.json";
    private static final String DATABASE_PATH = "dataBase.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private FruitStore store;
    private List<Fruit> fruits;

    @Before
    public void init() {
        store = new FruitStore();
        fruits = new ArrayList<>();
        Fruit apple = new Fruit(FruitType.APPLE, 10, 15, "28/03/19");
        Fruit kiwi = new Fruit(FruitType.KIWI, 20, 30, "28/03/19");
        Fruit orange = new Fruit(FruitType.ORANGE, 15, 20, "28/03/19");
        fruits.add(apple);
        fruits.add(kiwi);
        fruits.add(orange);

        try (FileOutputStream outputStream = new FileOutputStream(FIRST_FILE_PATH)) {
            MAPPER.writeValue(outputStream, fruits);
        } catch (IOException e) {
        }
    }

    @After
    public void clear() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(FIRST_FILE_PATH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.print("");
        try {
            writer = new PrintWriter(DATABASE_PATH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.print("");
        writer.close();
    }

    @Test
    public void shouldAddListOfFruitsToStorage() {
        store.addFruits(FIRST_FILE_PATH);
        assertTrue(store.getFruitsStorage().containsAll(fruits));
    }

    @Test
    public void shouldSaveStorageToJson() {
        store.addFruits(FIRST_FILE_PATH);
        store.save(DATABASE_PATH);
        List<Fruit> expected = new ArrayList<>();
        try (FileInputStream inputStream = new FileInputStream(DATABASE_PATH)) {
            expected = MAPPER.readValue(inputStream, new TypeReference<List<Fruit>>() {
            });
        } catch (IOException e) {
        }
        assertTrue(fruits.containsAll(expected));
    }

    @Test
    public void shouldLoadStorageFromJson() {
        store.addFruits(FIRST_FILE_PATH);
        try (FileOutputStream outputStream = new FileOutputStream(DATABASE_PATH)) {
            MAPPER.writeValue(outputStream, store.getFruitsStorage());
        } catch (IOException e) {

        }
        store.load(DATABASE_PATH);
        assertTrue(fruits.containsAll(store.getFruitsStorage()));
    }

    @Test
    public void shouldReturnListOfSpoiledFruitsOnTargetDate() {
        store.addFruits(FIRST_FILE_PATH);
        LocalDate date = LocalDate.of(2019, 4, 15);
        List<Fruit> spoiled = store.getSpoiledFruits(date);
        assertTrue(spoiled.contains(fruits.get(0)));
    }

    @Test
    public void shouldReturnListOfSpoiledFruitsOnTargetDateWithChosenType() {
        store.addFruits(FIRST_FILE_PATH);
        LocalDate date = LocalDate.of(2019, 4, 15);
        List<Fruit> spoiled = store.getSpoiledFruits(date, FruitType.APPLE);
        assertTrue(spoiled.contains(fruits.get(0)));
    }

    @Test
    public void shouldReturnListOfAvailableFruitsOnTargetDate() {
        store.addFruits(FIRST_FILE_PATH);
        LocalDate date = LocalDate.of(2019, 4, 15);
        List<Fruit> available = store.getAvailableFruits(date);
        int expectedSize = 2;
        assertEquals(available.size(), expectedSize);
        assertTrue(available.contains(fruits.get(1)));
        assertTrue(available.contains(fruits.get(2)));
    }

    @Test
    public void shouldReturnListOfAvailableFruitsOnTargetDateWithChosenType() {
        store.addFruits(FIRST_FILE_PATH);
        LocalDate date = LocalDate.of(2019, 4, 15);
        List<Fruit> available = store.getAvailableFruits(date, FruitType.ORANGE);
        int expectedSize = 1;
        assertEquals(available.size(), expectedSize);
        assertTrue(available.contains(fruits.get(2)));
    }

    @Test
    public void shouldReturnListOfAddedFruitsOnTargetDate() {
        store.addFruits(FIRST_FILE_PATH);
        LocalDate date = LocalDate.of(2019, 3, 28);
        List<Fruit> added = store.getAddedFruits(date);
        assertTrue(fruits.containsAll(added));
    }

    @Test
    public void shouldReturnListOfAddedFruitsOnTargetDateWithChosenType() {
        store.addFruits(FIRST_FILE_PATH);
        LocalDate date = LocalDate.of(2019, 3, 28);
        List<Fruit> added = store.getAddedFruits(date, FruitType.KIWI);
        assertEquals(added.get(0), fruits.get(1));
    }
}
