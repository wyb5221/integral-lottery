package com.dachen.integral.controller;

import com.dachen.common.support.JSONMessage;
import com.dachen.integral.biz.service.impl.UserRecordServiceImpl;
import com.dachen.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @Author: wangyongbin
 * @Date: 2021/5/31 15:38
 * @Description:
 */
@Slf4j
@RestController
@Api(description = "抽奖相关操作controller")
@RequestMapping("/nologin")
public class NoLoginController {

    @Autowired
    private UserRecordServiceImpl userRecordService;


    @ApiOperation(value = "导出积分明细Excel", httpMethod = "GET")
    @GetMapping("/integral/exportExcel")
    public JSONMessage exportIntegralExcel(HttpServletResponse response) throws Exception {
        String fileName = "积分明细";

        String[] columnNames = new String[]{"时间", "userId", "手机号码", "姓名", "医院", "科室", "职称", "变更方式", "积分数量", "原因"};//列名
        String[] keys = new String[]{"createTime", "userId", "telephone", "name", "hospitalName", "departments", "title", "operateType", "integral", "bizName"};

        List<Map<String, Object>> list = userRecordService.exportIntegralInfo();
        try {
            writeData(response, fileName, columnNames, keys, list);
        } catch (IOException ex) {
            log.error("exportExcel异常:{}", ex);
            return JSONMessage.error(ex);
        }
        return JSONMessage.success();
    }

    @ApiOperation(value = "导出中奖明细Excel", httpMethod = "GET")
    @GetMapping("/lottery/exportExcel")
    public JSONMessage exportLotteryExcel(HttpServletResponse response) throws Exception {
        String fileName = "中奖明细";

        String[] columnNames = new String[]{"抽奖时间", "学币数量", "积分数量", "花费积分数量", "userId", "手机号码", "姓名", "医院", "科室", "职称", "原因"};//列名
        String[] keys = new String[]{"createTime", "coinCount", "integralCount", "integral", "userId", "telephone", "name", "hospitalName", "departments", "title", "taskName"};
        //查询导出的中奖明细
        List<Map<String, Object>> list = userRecordService.exportLotteryInfo();
        try {
            writeData(response, fileName, columnNames, keys, list);
        } catch (IOException ex) {
            log.error("exportExcel异常:{}", ex);
            return JSONMessage.error(ex);
        }
        return JSONMessage.success();
    }

    private void writeData(HttpServletResponse response, String fileName, String[] columnNames,
                           String[] keys, List<Map<String, Object>> list) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ExcelUtil.createWorkBook(list, keys, columnNames, fileName).write(os);
        byte[] content  = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);

        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + new String((fileName + ".xlsx").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (IOException e) {
            log.error("IOException:{}", e);
            throw e;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                }catch (IOException e){
                    log.error("finally.IOException:{}", e);
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                }catch (IOException e){
                    log.error("bos.IOException:{}", e);
                }
            }
        }
    }

}
