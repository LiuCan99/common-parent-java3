package com.czxy.bos.elasticsearch.dao;

import com.czxy.bos.elasticsearch.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;



public interface BookRepository extends ElasticsearchRepository<Book,Integer> {


     // 标题进行等值查询

    public Book findByTitle(String title);


     // 模糊查询

    public List<Book> findByTitleLike(String title);


     // 模糊查询 + 分页

    public Page<Book> findByTitleLike(String title , Pageable pageable);

    //排序
    public List<Book> findByTitleLikeOrderByIdDesc(String title);

}
