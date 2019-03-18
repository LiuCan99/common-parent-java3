package com.czxy.bos.elasticsearch;

/**
 * Created by 音老怪 on 2018/9/27.
 */
import com.czxy.bos.TestApplication;
import com.czxy.bos.elasticsearch.domain.Book;
import com.czxy.bos.elasticsearch.service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestApplication.class)

public class TestElasticsearchService {
    @Resource
    private BookService bookService;

    @Test
    public void save(){
        Book book = new Book();
        book.setId(1);
        book.setContent("dsd");
        book.setTitle("哈哈哈");
        bookService.save(book);
        System.out.println("保存数据");
    }
    @Test
    public void testFindAll(){
        Page<Book> page = this.bookService.findAll(2,2);

        System.out.println("总分页：" +  page.getTotalPages());
        System.out.println("总个数：" + page.getTotalElements());
        System.out.println("每页显示个数：" +  page.getSize());

        List<Book> list = page.getContent();
        System.out.println(list.size());
    }

    @Test
    public void testFind1(){

        Book book = bookService.findByTtile("Java入门");

        System.out.println(book);
    }

    @Test
    public void testFind2(){

        List<Book> list = bookService.findByTitleLike("Java");

        for (Book book: list ) {
            System.out.println(book);
        }
    }

    @Test
    public void testFind3(){

        Page<Book> list = bookService.findByTitleLike("Java",1,2);

        for (Book book: list.getContent() ) {
            System.out.println(book);
        }
    }

     @Test
    public void testFind4(){
        List<Book> list = bookService.findByTitleLikeOrderByIdDesc("java");
        for (Book book : list){
            System.out.println(book);

        }
     }

     @Test
    public void test(){
        Page<Book> bookPage = this.bookService.searchQuery(1,2);


     }

}
