package com.atguigu.guli.service.statistics.service;

import com.atguigu.guli.service.statistics.entity.Daily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author leishuai
 * @since 2019-12-04
 */
public interface DailyService extends IService<Daily> {

    /**
     *
     * @param day
     */
    void createStatisticsByDay(String day);

    /**
     * 统计图表
     * @param begin
     * @param end
     * @param type
     * @return
     */
    Map<String, Object> getChartData(String begin, String end, String type);
}
