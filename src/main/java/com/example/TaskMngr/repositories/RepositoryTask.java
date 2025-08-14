package com.example.TaskMngr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TaskMngr.models.Task;

@Repository
public interface RepositoryTask extends JpaRepository<Task,Long> {

}