package com.shop.project.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.action.internal.OrphanRemovalAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name="users", uniqueConstraints ={@UniqueConstraint(columnNames = "username"),@UniqueConstraint(columnNames = "email")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "user_id")
    private Long userId;
    @NotBlank
    @Size(max = 20)
    @Column(name= "username")
    private String username;
    @NotBlank
    @Size(max = 20)
    @Column(name= "password")
    private String password;
    @NotBlank
    @Size(max = 50)
    @Column(name= "email")
    private String email;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    //PERSIST: 儲存 User 時，如果裡面的 Role 還沒存在，會一起存進資料庫。
    //MERGE: 更新 User 時，會自動同步更新 roles 資料。
    @Setter
    @Getter
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    //多對多用joinTable
    //「我想用一張名字叫做 user_role 的中介表來記錄 User 跟 Role 的關聯。」
    //這張表裡面會有兩個欄位：
    //user_id：連到 User 表的主鍵
    //role_id：連到 Role 表的主鍵
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    //NullPointerException
    //為什麼初始化為 new HashSet<>() 可以避免這個問題？
    private Set<Role> roles = new HashSet<>();

    @Getter
    @Setter
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_address",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id"))
    private List<Address> addresses = new ArrayList<>();

    //如果某個賣家user刪掉了 他登記的product也會被刪除掉
    @ToString.Exclude
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Set<Product> products;



}
