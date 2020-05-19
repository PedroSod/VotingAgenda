package com.votingAgenda.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Agenda implements Serializable {


    private static final long serialVersionUID = 8827127855599510984L;
    @Id
    private String id;
    private String title;
    private String description;
}
