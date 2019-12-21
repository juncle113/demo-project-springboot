package com.cpto.dapp.common.helper;


import com.csvreader.CsvWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * CsvHelper
 *
 * @author sunli
 * @date 2019/02/20
 */
@Component
@Validated
public class CsvHelper {

    /**
     * 下载csv文件
     *
     * @param fileName 文件名
     * @param title    标题
     * @param dataList 数据
     * @return 下载数据
     * @throws IOException IO异常
     */
    public ResponseEntity download(String fileName, String[] title, List<String[]> dataList) throws IOException {

        /* 1.取得输出数据 */
        byte[] outputData = write(title, dataList);

        /* 2.设置输出信息 */
        HttpHeaders headers = new HttpHeaders();
        String downloadName = fileName;

        // 设置数据类型
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // 设置浏览器打开方式
        headers.setContentDispositionFormData("attachment", downloadName);

        return new ResponseEntity(outputData, headers, HttpStatus.OK);
    }

    /**
     * 取得输出数据
     *
     * @param title    标题
     * @param dataList 数据
     * @return 输出数据
     */
    private byte[] write(String[] title, List<String[]> dataList) throws IOException {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CsvWriter csvWriter = new CsvWriter(output, ',', StandardCharsets.UTF_8);

        // 输出标题行
        csvWriter.writeRecord(title);

        // 输出数据
        for (String[] data : dataList) {
            csvWriter.writeRecord(data);
        }

        csvWriter.close();
        output.close();

        return output.toByteArray();
    }
}