package com.atguigu.guli.service.statistics.controller;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.statistics.service.DailyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javafx.scene.chart.ValueAxis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author leishuai
 * @since 2019-12-04
 */
@Api(description="统计分析管理")
@CrossOrigin
@RestController
@RequestMapping("/admin/statistics/daily")
public class DailyController {

    @Autowired
    private DailyService dailyService;

    @ApiOperation(value = "生成统计记录",notes = "生成统计日志")
    @PostMapping("{day}")
    public R createStatisticsByDate(
            @ApiParam(name = "day",value = "统计日期",required = true)
            @PathVariable("day") String day){
        dailyService.createStatisticsByDay(day);
        return R.ok();
    }




    @GetMapping("show-chart/{begin}/{end}/{type}")
    public R showChart(@PathVariable String begin,@PathVariable String end,@PathVariable String type){
        Map<String, Object> map = dailyService.getChartData(begin, end, type);
        return R.ok().data(map);
    }


}

