package com.trendyol.shipment;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Basket {

    private List<Product> products;

    public ShipmentSize getShipmentSize() {
        Map<ShipmentSize, Long> shipmentCounts = products.stream()
                .collect(Collectors.groupingBy(Product::getSize, Collectors.counting()));

        long totalCount = products.size();


        if (totalCount < 3) {
            return products.stream()
                    .map(Product::getSize)
                    .max(Comparator.naturalOrder())
                    .orElse(ShipmentSize.SMALL);
        }

        long maxCount = shipmentCounts.values().stream().mapToLong(Long::longValue).max().orElse(0);

        if (maxCount >= 3) {
            ShipmentSize maxCountSize = shipmentCounts.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue() == maxCount)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);

            if (maxCountSize == ShipmentSize.X_LARGE) {
                return ShipmentSize.X_LARGE;
            }

            return increaseShipmentSize(maxCountSize);
        } else {
            return products.stream()
                    .map(Product::getSize)
                    .max(Comparator.naturalOrder())
                    .orElse(ShipmentSize.SMALL);
        }


    }

    private ShipmentSize increaseShipmentSize(ShipmentSize size) {
        switch (size) {
            case SMALL:
                return ShipmentSize.MEDIUM;
            case MEDIUM:
                return ShipmentSize.LARGE;
            case LARGE:
                return ShipmentSize.X_LARGE;
            default:
                return size;
        }

    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
