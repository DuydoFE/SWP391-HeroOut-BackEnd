package com.demo.demo.repository;

import com.demo.demo.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BlogRepository extends JpaRepository<Blog,Long> {
}
