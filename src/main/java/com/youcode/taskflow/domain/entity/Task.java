package com.youcode.taskflow.domain.entity;

import com.youcode.taskflow.domain.enums.TaskPriority;
import com.youcode.taskflow.domain.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    private LocalDate assignDate;
    private LocalDate dueDate;

    @ManyToOne
    private User assignTo;

    @ManyToOne
    private User createdBy;

    @OneToOne(mappedBy = "task")
    @PrimaryKeyJoinColumn
    private JetonUsage jetonUsage;

    @ManyToMany(mappedBy = "tasks")
    @ToString.Exclude
    private List<Tag> tags;
}
