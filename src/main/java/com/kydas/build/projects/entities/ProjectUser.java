package com.kydas.build.projects.entities;

import com.kydas.build.projects.entities.embeddable.ProjectUserKey;
import com.kydas.build.users.User;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "project_users")
public class ProjectUser {

    @EmbeddedId
    private ProjectUserKey id;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String side;

    @Column(nullable = false)
    private Boolean isResponsible = false;
}
