package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter

public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


    //주인 : OrderItem
    //부하 : Order
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL
                , orphanRemoval = true, fetch = FetchType.LAZY) //order -> orderItem order가있음 이걸통해서 Order를 관리
    private List<OrderItem> orderItems = new ArrayList<>();

     private LocalDateTime regTime;

     private LocalDateTime updateTime;

     public void addOrderItem(OrderItem orderItem){
         orderItems.add(orderItem);
         orderItem.setOrder(this);
     }

     public static Order createOrder(Member member, List<OrderItem> orderItemList){
         Order order = new Order();
         order.setMember(member);
         for(OrderItem orderItem : orderItemList){
             order.addOrderItem(orderItem);
         }
         order.setOrderStatus(OrderStatus.ORDER);
         order.setOrderDate(LocalDateTime.now());
            return order;
     }

     public int getTotalPrice(){
         int totalPrice = 0;
         for(OrderItem orderItem : orderItems){
             totalPrice += orderItem.getTotalPrice();
         }
         return totalPrice;
     }

     public void cancelOrder(){
         this.orderStatus = OrderStatus.CANCEL;

         for(OrderItem orderItem : orderItems){
             orderItem.cancel();
         }
     }

}
