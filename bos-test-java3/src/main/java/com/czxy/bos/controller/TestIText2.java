package com.czxy.bos.controller;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.FileOutputStream;

/**
 * Created by lenovo on 2018/10/11.
 */
public class TestIText2 {

    public static void main(String[] args) throws Exception {
        //1 创建文档
        Document document=new Document();


        //2 确定写入位置
        PdfWriter.getInstance(document,new FileOutputStream("3.pdf"));

        //3 打开
        document.open();

        // 向document 生成pdf表格
        Table table = new Table(3);//创建7列的表格
        table.setWidth(80); // 宽度
        table.setBorder(1); // 边框

        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); // 水平对齐方式
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式


       //4设置表头
        table.addCell(getCell("编号"));
        table.addCell(getCell("姓名"));
        table.addCell(getCell("爱好"));

        // 模拟数据
        for(int i=0;i<=10;i++){
            table.addCell(getCell("bh"+i));
            table.addCell(getCell("jack"+i));
            table.addCell(getCell("学习"+i));
        }

        document.add(table);
        //5关闭
        document.close();
    }

    public  static  Cell getCell(String content) throws  Exception{
        //支持中文编码
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font=new Font(bfChinese);

        Cell cell=new Cell(new Paragraph(content,font));
        //设置单元格背景色
        cell.setBackgroundColor(Color.PINK);

        return cell;
    }
}
