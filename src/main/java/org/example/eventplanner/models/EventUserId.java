package org.example.eventplanner.models;

import lombok.Data;
import java.io.Serializable;
import java.util.Objects;

@Data
public class EventUserId implements Serializable {
    private Long id_user;
    private Long id_event;

    public EventUserId() {}

    public EventUserId(Long id_user, Long id_event) {
        this.id_user = id_user;
        this.id_event = id_event;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public Long getId_event() {
        return id_event;
    }

    public void setId_event(Long id_event) {
        this.id_event = id_event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventUserId that = (EventUserId) o;
        return Objects.equals(id_user, that.id_user) && Objects.equals(id_event, that.id_event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_user, id_event);
    }
}
