package in.astro.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "content", nullable = false) // Ensure the column name is correct
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id") // Ensure the column name is correct
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id") // Ensure the column name is correct
    private User user;

    @CreationTimestamp
    private Date commentDate;

}