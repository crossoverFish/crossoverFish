package com.tyu.common.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;

public class ExcelUtil {

    public static void downloadByVo(HttpServletResponse response, String fileName,
                                    Class cls, List dataList, Set<String> includeColumnFiledNames, Set<String> excludeColumnFiledNames)
            throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        LongestMatchColumnWidthStyleStrategy longestMatchColumnWidthStyleStrategy =
                new LongestMatchColumnWidthStyleStrategy();
        EasyExcel.write(response.getOutputStream(), cls)
                .sheet("sheet1")
                .registerConverter(new LongStringConverter())
                .registerWriteHandler(longestMatchColumnWidthStyleStrategy)
                .includeColumnFiledNames(includeColumnFiledNames)
                .excludeColumnFiledNames(excludeColumnFiledNames)
                .doWrite(dataList);
        response.flushBuffer();
    }

    public static void downloadByList(HttpServletResponse response, String fileName,
                                      List<List<String>> head, List dataList, Set<String> includeColumnFiledNames, Set<String> excludeColumnFiledNames)
            throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        LongestMatchColumnWidthStyleStrategy longestMatchColumnWidthStyleStrategy =
                new LongestMatchColumnWidthStyleStrategy();
        EasyExcel.write(response.getOutputStream()).
                head(head).sheet("sheet1")
                .registerConverter(new LongStringConverter())
                .registerWriteHandler(longestMatchColumnWidthStyleStrategy)
                .includeColumnFiledNames(includeColumnFiledNames)
                .excludeColumnFiledNames(excludeColumnFiledNames)
                .doWrite(dataList);
        response.flushBuffer();
    }

}
