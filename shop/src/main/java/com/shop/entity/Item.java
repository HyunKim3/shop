package com.shop.entity;


import com.shop.constant.ItemSellStatus;
import com.shop.constant.ItemValue;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString

public class Item extends BaseEntity{
    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO) //자동으로 1씩 증가
    private Long id; //상품코드

    @Column(nullable = false, length = 50)
    private String itemNm; // 상품명

    @Enumerated(EnumType.STRING)
    private ItemValue itemValue; //상품종류

    @Column(name = "price", nullable = false)
    private int price; // 가격


    @Column(nullable = false)
    private int stockNumber; //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상태

    // private LocalDateTime regTime;

    // private LocalDateTime updateTime;

    @ManyToMany
    @JoinTable(
            name = "member_item",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Member> member;

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
        this.itemValue = itemFormDto.getItemValue();

    }

    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber; // 10, 5 / 10, 20
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족합니다.(현재 재고 수량: "+this.stockNumber+")");
        }
        this.stockNumber = restStock; // 5
    }

    public void addStock(int stockNumber){

        this.stockNumber += stockNumber;
    }
}
