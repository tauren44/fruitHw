package com.mateacademy.fruitstore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fruit {
    private FruitType type;
    private int price;
    private int shelfLife;
    private String dateOfDelivery;

    public LocalDate convertStringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        return LocalDate.parse(date, formatter);
    }

    @JsonIgnore
    public LocalDate getDateOfSpoil() {
        LocalDate dateOfDelivery = convertStringToDate(getDateOfDelivery());
        return dateOfDelivery.plusDays(getShelfLife());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Fruit fruit = (Fruit) obj;
        return getType() == fruit.getType()
                && (shelfLife == fruit.shelfLife)
                && (price == fruit.price)
                && (dateOfDelivery.equals(fruit.getDateOfDelivery()));
    }
}
