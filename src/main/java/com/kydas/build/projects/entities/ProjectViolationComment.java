package com.kydas.build.projects.entities;

import com.kydas.build.core.crud.BaseComment;
import com.kydas.build.files.File;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "project_violation_comments")
public class ProjectViolationComment extends BaseComment {

    @ManyToOne
    @JoinColumn(name = "violation_id", nullable = false)
    private ProjectViolation violation;

    @OneToMany
    @JoinTable(
            name = "project_violation_comment_files",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private List<File> files = new ArrayList<>();
}
