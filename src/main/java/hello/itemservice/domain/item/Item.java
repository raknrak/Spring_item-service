package hello.itemservice.domain.item;

import lombok.Data;

@Data
public class Item { // Item - 상품 객체
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item(){

    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
