package gp.developer.api.task_3.entity;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(notes = "Developer ID", example = "1", required = false)
    private Long id;

    @Column(length = 50)
    @Size(min = 2, max = 50)
    @ApiModelProperty(notes = "Developer name", example = "user_1", required = true)
    private String name;

    @NonNull
    @Column(nullable = false)
    @ApiModelProperty(notes = "Developer email", example = "email_1", required = true)
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
