package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author leishuai
 * @since 2019-11-20
 */
public interface CourseMapper extends BaseMapper<Course> {
    /**
     * 根据id查询课程发布信息
     * @param id
     * @return
     */
    CoursePublishVo selectCoursePublishVoById(String id);
}
