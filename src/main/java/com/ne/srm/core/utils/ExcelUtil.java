package com.ne.srm.core.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

    public static List<String> headList = Arrays.asList(
            "SKU",
            "产品描述",
            "对公支付公司名称",
            "对公账户",
            "标签及说明书快递地址",
            "外箱尺寸(供应商包装完成时再次同其确认)",
            "发票类型",
            "含运含税价",
            "装箱率",
            "外箱毛重",
            "外箱材积",
            "外箱总数"
    );

    public static Workbook createHeTong(Map<String, List<Object>> data) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();

            //第一行
            Row row = sheet.createRow(0);
            for (int i = 0; i < headList.size(); i++) {
                row.createCell(i).setCellValue(headList.get(i));
            }


            return workbook;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int cellWidth(Double width){
        return Integer.parseInt(String.valueOf(width*256));
    }


}
