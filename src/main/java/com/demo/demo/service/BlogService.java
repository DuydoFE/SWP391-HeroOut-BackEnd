package com.demo.demo.service;

import com.demo.demo.entity.Blog;
import com.demo.demo.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public Blog getBlogById(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));
    }

    public Blog createBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    public Blog updateBlog(Long id, Blog updatedBlog) {
        Blog existing = getBlogById(id);

        existing.setTitle(updatedBlog.getTitle());
        existing.setContent(updatedBlog.getContent());
        existing.setDescription(updatedBlog.getDescription());
        existing.setCategory(updatedBlog.getCategory());
        existing.setAuthor(updatedBlog.getAuthor());
        existing.setReadTime(updatedBlog.getReadTime());
        existing.setViews(updatedBlog.getViews());
        existing.setDate(updatedBlog.getDate());
        existing.setTags(updatedBlog.getTags());

        return blogRepository.save(existing);
    }

    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
