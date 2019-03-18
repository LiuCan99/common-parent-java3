package com.czxy.bos.controller;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by lenovo on 2018/10/11.
 */
public class TestIText {

    public static void main(String[] args) throws Exception {
        //1 创建文档
        Document document=new Document();


        //2 确定写入位置
        PdfWriter.getInstance(document,new FileOutputStream("2.pdf"));


        //3 打开
        document.open();


        //支持中文编码
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        //4写入数据
        document.add(new Paragraph("传智学院",new Font(bfChinese)));

        //5关闭
        document.close();
    }
}
