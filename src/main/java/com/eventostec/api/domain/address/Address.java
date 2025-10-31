package com.eventostec.api.domain.address;

import com.eventostec.api.domain.event.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
// @Entity marca a classe como uma entidade JPA — será mapeada para uma tabela do banco.
@Table(name = "address")
// @Table define (opcionalmente) o nome da tabela no banco onde a entidade será persistida.
@Setter
// @Setter (Lombok) gera automaticamente os métodos setXxx() para todos os campos.
@Getter
// @Getter (Lombok) gera automaticamente os métodos getXxx() para todos os campos.
@AllArgsConstructor
// @AllArgsConstructor (Lombok) cria um construtor com todos os campos como parâmetros.
@NoArgsConstructor
// @NoArgsConstructor (Lombok) cria um construtor sem parâmetros (necessário para JPA).
public class Address {
    @Id
    @GeneratedValue
    private UUID id;

    private String city;
    private String uf;

    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;

}
