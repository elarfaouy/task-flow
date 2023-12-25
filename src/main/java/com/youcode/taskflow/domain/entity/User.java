package com.youcode.taskflow.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private Integer jetons;

    @ManyToOne
    private Role role;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<JetonUsage> jetonUsages;

    @OneToMany(mappedBy = "assignTo")
    @ToString.Exclude
    private List<Task> tasks;
}
