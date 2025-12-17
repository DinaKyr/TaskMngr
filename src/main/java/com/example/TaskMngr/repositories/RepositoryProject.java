package com.example.TaskMngr.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.TaskMngr.dto.ProjectStats;
import com.example.TaskMngr.models.Project;

@Repository
public interface RepositoryProject extends JpaRepository<Project,Long> {

    @Query("""
SELECT new com.example.TaskMngr.dto.ProjectStats(
    u.id,
    u.username,
    COUNT(p)
)
FROM Project p
JOIN p.users u
WHERE p.isComplete = true
GROUP BY u.id, u.username
""")
List<ProjectStats> countCompletedProjectsPerUser();


}