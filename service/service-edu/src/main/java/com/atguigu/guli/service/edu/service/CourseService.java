package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

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

    /**
     * 分页查询课程类表
     * @param pageParam
     * @param webCourseQueryVo
     * @return
     */
    Map<String, Object> webSelectPage(Page<Course> pageParam, WebCourseQueryVo webCourseQueryVo);

    /**
     * 获取课程信息并更新浏览量
     * @param id
     * @return
     */
    WebCourseVo selectWebCourseVoById(String id);


    /**
     * 根据课程id课程信息
     * @param id
     * @return
     */
    CourseDto getCourseDtoById(String id);

    /**
     * 根据课程id 修改销售数量
     * @param id
     */
    void updateBuyCountById(String id);

}
