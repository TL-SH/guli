package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.util.JwtUtils;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.edu.client.OrderFeignClient;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseQueryVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author tanglei
 */
@CrossOrigin
@Api(description="课程")
@RestController
@RequestMapping("/api/edu/course")
public class ApiCourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ChapterService chapterService;


    @Autowired
    private OrderFeignClient orderFeignClient;

    @ApiOperation(value = "分页课程列表")
    @GetMapping(value = "page-list")
    public R pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @RequestParam Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @RequestParam Long limit,

            @ApiParam(name = "webCourseQueryVo", value = "查询对象", required = false)
                    WebCourseQueryVo webCourseQueryVo){

        Page<Course> pageParam = new Page<>(page, limit);
        Map<String,Object> map = courseService.webSelectPage(pageParam,webCourseQueryVo);

        return R.ok().data(map);
    }

    @ApiOperation(value = "根据ID查询课程")
    @GetMapping(value = "get/{courseId}")
    public R getById(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable String courseId){

        //查询课程信息和讲师信息
        WebCourseVo webCourseVo = courseService.selectWebCourseVoById(courseId);

        //查询当前课程的章节信息
        List<ChapterVo> chapterVoList = chapterService.nestedListForWeb(courseId);

        return R.ok().data("course", webCourseVo).data("chapterVoList", chapterVoList);
    }


    @ApiOperation(value = "根据课程id查询课程信息")
    @GetMapping(value = "inner/get-course-dto/{id}")
    public CourseDto getCourseDtoById(@PathVariable String id){
        CourseDto courseDto = courseService.getCourseDtoById(id);
        return courseDto;
    }

    @ApiOperation(value = "根据课程id更改销售量")
    @GetMapping(value = "inner/update-buy-count/{id}")
    public R updateBuyCountById(
            @ApiParam(name = "id", value = "课程id", required = true)
            @PathVariable String id){
        courseService.updateBuyCountById(id);
        return R.ok();
    }


    @ApiOperation(value = "根据id查询课程")
    @GetMapping("is-buy/{courseId}")
    public R isBuy(
            @ApiParam(name = "courseId", value = "课程ID", required = true)
            @PathVariable("courseId")String courseId, HttpServletRequest request){
        Boolean isBuy = false;
        if(JwtUtils.checkToken(request)){
            // 是否购买
            isBuy = orderFeignClient.isBuyByCourseId(JwtUtils.getMemberIdByJwtToken(request),courseId);
        }
        return R.ok().data("isBuy",isBuy);
    }


}
