package ru.practicum.explore_with_me.comments.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore_with_me.event.models.Event;
import ru.practicum.explore_with_me.user.models.User;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "event")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "commentator")
    private User commentator;
    private String text;
    @Enumerated(EnumType.STRING)
    private Moderation moderation;
    @Column(name = "created_At")
    private LocalDateTime createdAt;
}