package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.vo.TeacherQueryVo;
import com.atguigu.guli.service.edu.mapper.TeacherMapper;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
}
