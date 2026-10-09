package ec.com.leodev.fundamentals.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import java.io.Serializable;

//import com.fasterxml.jackson.annotation.JsonBackReference;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "post")
public class Posts implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //identity para que no se siga el contador de otros registros
    @Column(name = "id_post", nullable = false, unique = true)
    private Long id;
    @Column(name = "description", length = 250)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id") //No necesario para el ejemplo del curso pero se puede explicar
    // El usuario ya contiene sus posts: si el post serializa a su usuario, el JSON se vuelve infinito
    @JsonIgnore
    private User user;

    public Posts(String description, User user) {
        this.description = description;
        this.user = user;
    }

}
