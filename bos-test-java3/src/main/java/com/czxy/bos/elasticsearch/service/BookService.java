package com.czxy.bos.elasticsearch.service;

import com.czxy.bos.elasticsearch.dao.BookRepository;
import com.czxy.bos.elasticsearch.domain.Book;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;



@Service
@Transactional
public class BookService {

    @Resource
    private BookRepository bookRepository;

    public void save(Book book){
        this.bookRepository.save(book);
    }

    public Page<Book> findAll(int page , int rows){
        Pageable pageable = PageRequest.of( page-1,rows);
        return this.bookRepository.findAll(pageable);
    }

    public Book findByTtile(String title){
        return this.bookRepository.findByTitle(title);
    }

    public List<Book> findByTitleLike(String title){
        return this.bookRepository.findByTitleLike(title);
    }

    public Page<Book> findByTitleLike(String title,int page, int size){
        return this.bookRepository.findByTitleLike(title , PageRequest.of(page-1,size));
    }

    //排序
    public List<Book> findByTitleLikeOrderByIdDesc(String title){
        return  this.bookRepository.findByTitleLikeOrderByIdDesc(title);
    }

    //多条件查询
    public Page<Book> searchQuery(Integer page,Integer rows){
        //可以将多个条件    组合在一起   进行查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //封装查询信息
        SearchQuery searchQuery = new NativeSearchQuery(queryBuilder);  //2.1 查询条件
        return this.bookRepository.search( searchQuery );
    }
}
