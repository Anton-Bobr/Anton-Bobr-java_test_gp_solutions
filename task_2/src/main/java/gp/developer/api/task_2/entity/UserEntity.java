package gp.developer.api.task_2.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Size;


@Entity
@Getter@Setter
@EqualsAndHashCode (exclude = {"id"})
@NoArgsConstructor
//@RequiredArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    @Size(min = 2, max = 20)
    private String name;

    @NonNull
    @Column(nullable = false)
    private String email;

    public UserEntity (String name, String email) {
        this.name = name;
        this.email = email;
    }
}
