package gp.developer.api.task_2.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Size;


@Entity
@Getter
@Setter
@EqualsAndHashCode (exclude = {"id"})
@NoArgsConstructor
public class DeveloperEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    @Size(min = 2, max = 50)
    private String name;

    @NonNull
    @Column(nullable = false)
    private String email;

    public DeveloperEntity(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public DeveloperEntity(String name, String email, long id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }
}
