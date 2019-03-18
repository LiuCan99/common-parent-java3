package com.czxy.bos.controller.print;

import com.czxy.bos.domain.report.HighChart;
import com.czxy.bos.domain.take_delivery.WayBill;
import com.czxy.bos.service.take_delivery.WayBillService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;


@Controller             //注意：不能使用@RestController，报表需要下载等操作。
@RequestMapping("/report")
public class ReportController {

    @Resource
    private WayBillService wayBillService;

    @GetMapping("/exportPdf")
    public void exportPdf(HttpServletResponse response ) throws Exception {
        //1 设置响应头，通知浏览器不要解析，直接下载
        String filename = new String("运单数据".getBytes(),"ISO-8859-1");
        // 固定的响应头（content-disposition），用于下载的
        // 值大部分格式固定的， attachment;filename=文件名
        response.setHeader("content-disposition","attachment;filename="+filename+".pdf");

        //2 查询所有的运单数据
        List<WayBill> list = wayBillService.findAll();

        //3 生成pdf文档，并将pdf文档的内容响应给浏览器。
        //3.1 创建文档
        Document document = new Document();
        //3.2 通过实例，确定dpf保存位置
        PdfWriter.getInstance( document , response.getOutputStream() );
        //3.3 打开
        document.open();
        //3.4 写数据
        document.add( getTable(list) );
        //3.5 关闭
        document.close();

    }

    /**
     * 生成表格
     * @param list
     * @return
     * @throws Exception
     */
    private Table getTable(List<WayBill> list) throws Exception {
        //1 创建表格
        Table table = new Table(7);
        //2 基本设置
        // 边框宽度、对其方式）
        table.setBorder(1);
        // 水平对齐方式
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        // 垂直对齐方式
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);

        //3 表头
        table.addCell(buildCell("运单号"));
        table.addCell(buildCell("寄件人"));
        table.addCell(buildCell("寄件人电话"));
        table.addCell(buildCell("寄件人地址"));
        table.addCell(buildCell("收件人"));
        table.addCell(buildCell("收件人电话"));
        table.addCell(buildCell("收件人地址"));

        //4 内容
        for(WayBill wayBill : list){
            table.addCell(buildCell(wayBill.getWayBillNum()));
            table.addCell(buildCell(wayBill.getSendName()));
            table.addCell(buildCell(wayBill.getSendMobile()));
            table.addCell(buildCell(wayBill.getSendAddress()));
            table.addCell(buildCell(wayBill.getRecName()));
            table.addCell(buildCell(wayBill.getRecMobile()));
            table.addCell(buildCell(wayBill.getRecAddress()));
        }


        return table;
    }

    /**
     * 生成单元格
     * @param content
     * @return
     * @throws Exception
     */
    private Cell buildCell(String content) throws Exception {
        //支持中文
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font = new Font(bfChinese , 10);
        //
        Cell cell = new Cell(new Paragraph(content, font));
        //设置单元格背景色
        cell.setBackgroundColor(Color.PINK);

        return cell;
    }

    @GetMapping("/exportHighcharts")
    public ResponseEntity<List<HighChart>> exportHighcharts(){
        //查询
        List<HighChart> list = this.wayBillService.chartWayBill();
        //封装
        return new ResponseEntity<>( list , HttpStatus.OK);
    }


}
