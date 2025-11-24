package org.example.eventplanner.models;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Data
public class EventUserId implements Serializable {

    private Long idUser;
    private Long idEvent;

    public EventUserId() {}

    public EventUserId(Long idUser, Long idEvent) {
        this.idUser = idUser;
        this.idEvent = idEvent;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventUserId that = (EventUserId) o;
        return Objects.equals(idUser, that.idUser) && Objects.equals(idEvent, that.idEvent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, idEvent);
    }
}
