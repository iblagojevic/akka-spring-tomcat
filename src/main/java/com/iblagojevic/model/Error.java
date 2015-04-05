package com.iblagojevic.model;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name = "error")
public class Error implements Serializable {
    private static final long serialVersionUID = -5244054259667284341L;
    private Long id;
    private User user;
    private String message;
    private long errorTimestamp;

    public Error() {
    }

    public Error(Long id, User user, String message, long errorTimestamp) {
        this.id = id;
        this.user = user;
        this.message = message;
        this.errorTimestamp = errorTimestamp;
    }

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "message", nullable = false)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name = "error_timestamp", nullable = false)
    public long getErrorTimestamp() {
        return errorTimestamp;
    }

    public void setErrorTimestamp(long timestamp) {
        this.errorTimestamp = timestamp;
    }
}
