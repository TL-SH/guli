package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.util.JwtUtils;
import com.atguigu.guli.service.edu.entity.vo.CourseCollectVo;
import com.atguigu.guli.service.edu.service.CourseCollectService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author tanglei
 */
@CrossOrigin
@RestController
@RequestMapping("/api/edu/course-collect")
@Slf4j
public class ApiCourseCollectController {


    @Autowired
    private CourseCollectService courseCollectService;


    @ApiOperation(value = "获取课程收藏分页列表")
    @GetMapping("auth/{page}/{limit}")
    public R index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit,

            HttpServletRequest request) {
        Page<CourseCollectVo> pageParam = new Page<>(page, limit);
        Map<String, Object> map = courseCollectService.selectPage(pageParam, JwtUtils.getMemberIdByJwtToken(request));
        return R.ok().data(map);
    }


    @ApiOperation(value = "收藏课程")
    @PostMapping("auth/save/{courseId}")
    public R save(
            @ApiParam(name = "courseId", value = "课程id", required = true)
            @PathVariable String courseId,

            HttpServletRequest request) {
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        courseCollectService.saveCourseCollect(courseId, memberId);
        return R.ok();
    }

    @ApiOperation(value = "取消收藏课程")
    @DeleteMapping("auth/remove/{id}")
    public R remove(
            @ApiParam(name = "id", value = "收藏id", required = true)
            @PathVariable String id) {
        courseCollectService.removeById(id);
        return R.ok();
    }




}
