package com.youcode.taskflow.domain.entity;

import com.youcode.taskflow.domain.enums.JetonUsageAction;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jeton_usages")
public class JetonUsage {
    @Id
    @Column(name = "task_id", nullable = false)
    private Long id;
    @Enumerated(EnumType.STRING)
    private JetonUsageAction action;
    private LocalDate actionDate;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "task_id")
    @ToString.Exclude
    private Task task;

    @ManyToOne
    private User user;
}
