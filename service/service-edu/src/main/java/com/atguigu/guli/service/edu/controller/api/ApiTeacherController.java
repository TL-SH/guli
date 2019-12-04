package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.service.CourseService;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author tanglei
 */

@Api(description = "讲师")
@CrossOrigin
@RestController
@RequestMapping("/api/edu/teacher")
public class ApiTeacherController {
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private CourseService courseService;

    @ApiOperation(value = "所有讲师列表",notes = "返回所有讲师列表")
    @GetMapping("list")
    public R listAll(){
        List<Teacher> list = this.teacherService.list(null);
        return R.ok().data("items",list).message("获取讲师列表列表成功");
    }

    @ApiOperation(value = "获取讲师")
    @GetMapping("get/{id}")
    public R get(
            @ApiParam(name = "id", value = "讲师ID", required = true)
            @PathVariable String id) {
        Map<String, Object> map = teacherService.selectTeacherInfoById(id);
        return R.ok().data(map);
    }



    @ApiOperation(value = "分页讲师列表")
    @GetMapping("page-list")
    public R pageList(
            @ApiParam(name = "page",value = "当前页码",required = true)
            @RequestParam("page")Long page,
            @ApiParam(name = "limit",value = "每页记录数",required = true)
            @RequestParam("limit")Long limit){

        Page<Teacher> pageParam = new Page<>(page,limit);
        Map<String,Object> map = this.teacherService.webSelectPage(pageParam);

        return R.ok().data(map);
    }




}
