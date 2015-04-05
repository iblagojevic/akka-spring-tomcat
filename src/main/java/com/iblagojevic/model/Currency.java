package com.iblagojevic.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "currency")
public class Currency implements Serializable {
    private static final long serialVersionUID = -6131159486470222550L;
    private String id;
    private String name;

    public Currency() {
    }

    public Currency(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
