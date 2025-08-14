package com.example.TaskMngr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TaskMngr.models.Project;

@Repository
public interface RepositoryProject extends JpaRepository<Project,Long> {

}