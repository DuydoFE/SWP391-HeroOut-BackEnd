package com.demo.demo.service;

import com.demo.demo.dto.BlogRequest;
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

    public Blog createBlog(BlogRequest request) {
        Blog blog = new Blog();
        mapRequestToBlog(request, blog);
        return blogRepository.save(blog);
    }

    public Blog updateBlog(Long id, BlogRequest request) {
        Blog existing = getBlogById(id);
        mapRequestToBlog(request, existing);
        return blogRepository.save(existing);
    }


    private void mapRequestToBlog(BlogRequest request, Blog blog) {
        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setDescription(request.getDescription());
        blog.setCategory(request.getCategory());
        blog.setAuthor(request.getAuthor());
        blog.setReadTime(request.getReadTime());
        blog.setViews(request.getViews());
        blog.setDate(request.getDate());
        blog.setTags(request.getTags());


        blog.setImage(request.getImage());
    }

    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
