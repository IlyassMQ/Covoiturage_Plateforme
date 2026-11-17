package org.example.covoiturage_plateforme.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "admin")
public class Admin extends User{

    public Admin(String firstName, String email, String lastName, String phone) {
        super(firstName, email, lastName, phone);
    }
}
