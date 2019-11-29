package com.atguigu.guli.service.edu.controller;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseQueryVo;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author leishuai
 * @since 2019-11-20
 */
@Api(description="课程管理")
@CrossOrigin //跨域
@RestController
@RequestMapping("/admin/edu/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @ApiOperation(value = "保存课程信息")
    @PostMapping("/save-course-info")
    public R saveCourseInfo(
            @ApiParam(name = "courseInfoForm",value = "课程基本信息",required = true)
            @RequestBody CourseInfoForm courseInfoForm){
        String courseId = courseService.saveCourseInfo(courseInfoForm);
        return R.ok().data("courseId",courseId).message("保存成功");
    }


    @ApiOperation(value = "根据id查询课程")
    @GetMapping("course-info/{id}")
    public R getCourseInfoFormById(
            @ApiParam(name = "id",value = "课程的id",required = true)
            @PathVariable String id){
        CourseInfoForm courseInfoForm = this.courseService.getCourseInfoFormById(id);
        return R.ok().data("item",courseInfoForm);
    }


    @ApiOperation(value = "更新课程信息")
    @PutMapping("update-course-info")
    public R updateCourseInfo(
            @ApiParam(name = "courseInfoForm",value = "课程基本信息",required = true)
            @RequestBody CourseInfoForm courseInfoForm){
        courseService.updateCourseById(courseInfoForm);
        return R.ok().message("更新成功");
    }


    @ApiOperation(value = "分页课程列表")
    @GetMapping("{page}/{limit}")
    public R pageQuery(
            @ApiParam(name = "page",value = "当前页码",required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit",value = "每页的记条数",required = true)
            @PathVariable Long limit,
            @ApiParam(name = "courseQueryVo",value = "查询对象",required = false)
            CourseQueryVo courseQueryVo){
        // 封装分页查询条件
        Page<Course> pageParam = new Page<>(page, limit);

        this.courseService.selectPage(pageParam,courseQueryVo);
        // 获取所有的记录数
        List<Course> records = pageParam.getRecords();
        // 获取总条数
        long total = pageParam.getTotal();
        return R.ok().data("total",total).data("rows",records).message("课程列表分页查询成功");
    }

    @ApiOperation(value = "根据id删除课程信息")
    @DeleteMapping("{id}")
    public R removeById(
            @ApiParam(name = "id",value = "课程信息id",required = true)
            @PathVariable String id){

        this.courseService.removeCourseById(id);
        return R.ok().message("删除成功");
    }

    @ApiOperation(value = "根据课程id查询发布课程信息")
    @GetMapping("course-publish-info/{id}")
    public R getCoursePublicVoById(
            @ApiParam(name = "id",value = "课程信息id",required = true)
            @PathVariable String id){

        CoursePublishVo coursePublishVoById = this.courseService.getCoursePublishVoById(id);
        return R.ok().data("item",coursePublishVoById);
    }


    @ApiOperation(value = "根据id发布课程")
    @PutMapping("publish-course/{id}")
    public R publishCourseById(
            @ApiParam(name = "id", value = "课程ID", required = true)
            @PathVariable String id){
        courseService.publishCourseById(id);
        return R.ok();
    }
}

