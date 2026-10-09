package ec.com.leodev.fundamentals.entity;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user", nullable = false, unique = true)
    private Long id;
    @Column(length = 50)
    private String name;
    @Column(length = 50, nullable = false, unique = true)
    private String email;
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    //@JsonManagedReference
    private List<Posts> posts = new ArrayList<>();

    public User(String name, String email, LocalDate birthDate) {
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
                ", posts=" + posts +
                '}';
    }
}
