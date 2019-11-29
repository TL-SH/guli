package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseQueryVo;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author leishuai
 * @since 2019-11-20
 */
public interface CourseService extends IService<Course> {

    /**
     * 保存课程和详细详细信息
     * @param courseInfoForm
     * @return 返回的课程id
     */
    String saveCourseInfo(CourseInfoForm courseInfoForm);

    /**
     * 回显课程信息
     * @param id
     * @return
     */
    CourseInfoForm getCourseInfoFormById(String id);

    /**
     * 更新课程的信息
     * @param courseInfoForm
     */
    void updateCourseById(CourseInfoForm courseInfoForm);

    /**
     *  分页课程列表
     * @param pageParam
     * @param courseQueryVo
     */
    void selectPage(Page<Course> pageParam, CourseQueryVo courseQueryVo);

    /**
     * 删除课程
     * @param id
     */
    void removeCourseById(String id);


    /**
     * 根据课程id查询帆布课程信息
     * @param id
     * @return
     */
    CoursePublishVo getCoursePublishVoById(String id);


    /**
     * 发布课程信息
     * @param id
     */
    void publishCourseById(String id);


}
