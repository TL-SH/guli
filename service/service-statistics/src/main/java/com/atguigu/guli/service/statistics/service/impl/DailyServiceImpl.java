package com.atguigu.guli.service.statistics.service.impl;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.statistics.client.UcenterClient;
import com.atguigu.guli.service.statistics.entity.Daily;
import com.atguigu.guli.service.statistics.mapper.DailyMapper;
import com.atguigu.guli.service.statistics.service.DailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author leishuai
 * @since 2019-12-04
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements DailyService {

    @Autowired
    private RestTemplate restTemplate;

    //@Override
    public void createStatisticsByDay2(String day) {
        // 如果当日统计记录已存在,则删除从新统计
        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_calculated",day);
        baseMapper.delete(queryWrapper);

        // 生成统计记录
        R r = restTemplate.getForObject(
                "http://guli-ucenter/admin/ucenter/member/count-register/{day}",
                R.class,
                day);
        Integer registerNum = (Integer) r.getData().get("countRegister");
        Daily daily = new Daily();
        daily.setDateCalculated(day);
        daily.setRegisterNum(registerNum);
        daily.setLoginNum(RandomUtils.nextInt(100, 200));
        daily.setCourseNum(RandomUtils.nextInt(100, 200));
        daily.setVideoViewNum(RandomUtils.nextInt(100, 200));

        baseMapper.insert(daily);
    }

    @Autowired
    private UcenterClient ucenterClient;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createStatisticsByDay(String day) {
        // 如果当日统计记录已存在,则删除从新统计
        //如果重复，则删除记录重新统计
        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("date_calculated", day);
        baseMapper.delete(queryWrapper);


        R r = ucenterClient.countRegister(day);
        Integer registerNum = (Integer)r.getData().get("countRegister");

        Daily daily = new Daily();
        daily.setDateCalculated(day);
        daily.setRegisterNum(registerNum);
        daily.setLoginNum(RandomUtils.nextInt(100, 200));
        daily.setCourseNum(RandomUtils.nextInt(100, 200));
        daily.setVideoViewNum(RandomUtils.nextInt(100, 200));

        baseMapper.insert(daily);
    }

    @Override
    public Map<String, Object> getChartData(String begin, String end, String type) {
        Map<String, Object> map = new HashMap<>();

        List<String> xList = new ArrayList<>(); // 日期列表
        List<Integer> yList = new ArrayList<>(); // 数据类表
        QueryWrapper<Daily> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(type,"date_calculated");
        queryWrapper.between("date_calculated",begin,end);

        List<Map<String, Object>> mapsData = baseMapper.selectMaps(queryWrapper);

        mapsData.forEach(data -> {
            String dateCalculated = (String)data.get("date_calculated");
            xList.add(dateCalculated);
            Integer count = (Integer) data.get(type);
            yList.add(count);
        });
        map.put("xData",xList);
        map.put("yData",yList);

        return map;
    }
}
