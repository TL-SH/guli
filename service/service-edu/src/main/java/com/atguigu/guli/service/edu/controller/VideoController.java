package com.atguigu.guli.service.edu.controller;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.vo.VideoInfoForm;
import com.atguigu.guli.service.edu.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author leishuai
 * @since 2019-11-20
 */
@Api(description="课时管理")
@CrossOrigin //跨域
@RestController
@RequestMapping("/admin/edu/video")
public class VideoController {
    @Autowired
    private VideoService videoService;


    @ApiOperation(value = "新增课时")
    @PostMapping("save")
    public R save(
            @ApiParam(name = "videoInfoForm",value = "课时对象",required = true)
            @RequestBody VideoInfoForm videoInfoForm){

        this.videoService.saveVideoInfoForm(videoInfoForm);
        return R.ok().message("课时对象保存成功");
    }


    @ApiOperation(value = "根据id查询课时信息")
    @GetMapping("get/{id}")
    public R getVideoInfoFormById(
            @ApiParam(name = "id",value = "课时id",required = true)
            @PathVariable String id){
        VideoInfoForm videoInfoForm = this.videoService.getVideoInfoFormById(id);
        return R.ok().data("item",videoInfoForm);
    }


    @ApiOperation(value = "新增课时")
    @PutMapping("update")
    public R update(
            @ApiParam(name = "videoInfoForm",value = "课时对象",required = true)
            @RequestBody VideoInfoForm videoInfoForm){
        this.videoService.updateVideoInfoForm(videoInfoForm);
        return R.ok().message("课时对象更新成功");
    }

    @ApiOperation(value = "根据ID删除课时")
    @DeleteMapping("remove/{id}")
    public R removeById(
            @ApiParam(name = "id", value = "课时ID", required = true)
            @PathVariable String id){

        videoService.removeVideoById(id);
        return R.ok();
    }



}

