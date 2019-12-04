package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.vo.TeacherQueryVo;
import com.atguigu.guli.service.edu.mapper.CourseMapper;
import com.atguigu.guli.service.edu.mapper.TeacherMapper;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author leishuai
 * @since 2019-11-20
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {


    @Autowired
    private CourseMapper courseMapper;

    @Override
    public IPage<Teacher> selectPage(Page<Teacher> pageParam, TeacherQueryVo teacherQueryVo) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");

        if(teacherQueryVo==null){
            return baseMapper.selectPage(pageParam,queryWrapper);
        }

        String name = teacherQueryVo.getName();
        Integer level = teacherQueryVo.getLevel();
        String joinDateBegin = teacherQueryVo.getJoinDateBegin();
        String joinDateEnd = teacherQueryVo.getJoinDateEnd();

        if(!StringUtils.isEmpty(name)){
            queryWrapper.eq("name",name);
        }
        if (level!=null){
            queryWrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(joinDateBegin)){
            queryWrapper.ge("join_date",joinDateBegin);
        }
        if(!StringUtils.isEmpty(joinDateEnd)){
            queryWrapper.ge("join_date",joinDateEnd);
        }
        return baseMapper.selectPage(pageParam,queryWrapper);
    }

    @Override
    public List<Map<String, Object>> selectNameListByKey(String key) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name");
        queryWrapper.likeRight("name",key);

        List<Map<String, Object>> list = this.baseMapper.selectMaps(queryWrapper);
        if(!CollectionUtils.isEmpty(list)){
            return list;
        }
        return null;
    }

    @Override
    public Map<String, Object> webSelectPage(Page<Teacher> pageParam) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        baseMapper.selectPage(pageParam,queryWrapper);
        List<Teacher> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        long current = pageParam.getCurrent();
        long size = pageParam.getSize();
        long pages = pageParam.getPages();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
    }

    @Override
    public Map<String, Object> selectTeacherInfoById(String id) {
        // 获取讲师的信息
        Teacher teacher = baseMapper.selectById(id);

        // 根据讲师id获取课程的信息
        List<Course> courseList = courseMapper.selectList(new QueryWrapper<Course>().eq("teacher_id", id));
        Map<String, Object> map = new HashMap<>();
        map.put("teacher",teacher);
        map.put("courseList",courseList);
        return map;
    }
}
