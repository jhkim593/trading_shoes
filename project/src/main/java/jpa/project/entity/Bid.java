package jpa.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Bid extends BaseTimeEntity{
    @Id
    private Long id;



}
