package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.edu.client.OssClient;
import com.atguigu.guli.service.edu.client.VodClient;
import com.atguigu.guli.service.edu.entity.*;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.CoursePublishVo;
import com.atguigu.guli.service.edu.entity.vo.CourseQueryVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseQueryVo;
import com.atguigu.guli.service.edu.entity.vo.WebCourseVo;
import com.atguigu.guli.service.edu.mapper.*;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author leishuai
 * @since 2019-11-20
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseDescriptionMapper courseDescriptionMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private OssClient ossClient;

    @Autowired
    private VideoMapper videoMapper;


    @Autowired
    private VodClient vodClient;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {
        // 保存课程的基本信息
        Course course = new Course();
        course.setStatus(Course.COURSE_DRAFT);
        BeanUtils.copyProperties(courseInfoForm,course);
        baseMapper.insert(course);

        // 保存课程的描述信息
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        courseDescriptionMapper.insert(courseDescription);
        return course.getId();
    }

    @Override
    public CourseInfoForm getCourseInfoFormById(String id) {
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        // 查询课程的信息
        Course course = baseMapper.selectById(id);
        // 查询课程描述的信息
        CourseDescription courseDescription = courseDescriptionMapper.selectById(id);
        BeanUtils.copyProperties(course,courseInfoForm);
        courseInfoForm.setDescription(courseDescription.getDescription());

        return courseInfoForm;
    }

    @Override
    public void updateCourseById(CourseInfoForm courseInfoForm) {
        // 更新课程的基本信息
        Course course = new Course();
        BeanUtils.copyProperties(courseInfoForm,course);
        baseMapper.updateById(course);

        // 更新课程的描述信息
        CourseDescription courseDescription = new CourseDescription();
        //BeanUtils.copyProperties(courseInfoForm,courseDescription);
        courseDescription.setId(course.getId());
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescriptionMapper.updateById(courseDescription);
    }

    @Override
    public void selectPage(Page<Course> pageParam, CourseQueryVo courseQueryVo) {
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("gmt_create");

        if(courseQueryVo==null){
            baseMapper.selectPage(pageParam,queryWrapper);
            return;
        }
        String title = courseQueryVo.getTitle();
        String teacherId = courseQueryVo.getTeacherId();
        String subjectId = courseQueryVo.getSubjectId();
        String subjectParentId = courseQueryVo.getSubjectParentId();

        if(!StringUtils.isEmpty(title)){
            queryWrapper.like("title",title);
        }

        if(!StringUtils.isEmpty(teacherId)){
            queryWrapper.eq("teacher_id",teacherId);
        }

        if(!StringUtils.isEmpty(subjectId)){
            queryWrapper.eq("subject_id",subjectId);
        }

        if(!StringUtils.isEmpty(subjectParentId)){
            queryWrapper.eq("subject_parent_id",subjectParentId);
        }
        baseMapper.selectPage(pageParam,queryWrapper);
    }

    @Override
    public void removeCourseById(String id) {

        //删除关联数据

        //删除oss中的图片文件
        //在此处调用oss中的删除图片文件的接口
        Course course = baseMapper.selectById(id);
        String cover = course.getCover();
        ossClient.removeFile(cover);

        // 删除阿里云视频id
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("video_source_id");
        queryWrapper.eq("course_id",id);
        List<Map<String, Object>> maps = videoMapper.selectMaps(queryWrapper);

        List<String> videoSourceIdList = maps.stream().map(map -> {
            String videoSourceId = (String) map.get("video_source_id");
            return videoSourceId;
        }).collect(Collectors.toList());

        vodClient.removeVideoByIdList(videoSourceIdList);


        //根据id删除所有视频
        QueryWrapper<Video> queryWrapperVideo = new QueryWrapper<>();
        queryWrapperVideo.eq("course_id", id);
        videoMapper.delete(queryWrapperVideo);

        //根据id删除所有章节
        QueryWrapper<Chapter> queryWrapperChapter = new QueryWrapper<>();
        queryWrapperChapter.eq("course_id", id);
        chapterMapper.delete(queryWrapperChapter);

        //删除课程详情
        courseDescriptionMapper.deleteById(id);

        //根据id删除课程
        baseMapper.deleteById(id);

    }

    @Override
    public CoursePublishVo getCoursePublishVoById(String id) {
        return baseMapper.selectCoursePublishVoById(id);
    }

    @Override
    public void publishCourseById(String id) {
        Course course = new Course();
        course.setId(id);
        course.setStatus(Course.COURSE_NORMAL);
        baseMapper.updateById(course);
    }

    @Override
    public Map<String, Object> webSelectPage(Page<Course> pageParam, WebCourseQueryVo webCourseQueryVo) {
        // 创建QueryWrapper对象
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();

        // 组装查询条件
        if(!StringUtils.isEmpty(webCourseQueryVo.getSubjectParentId())){
            queryWrapper.eq("subject_parent_id",webCourseQueryVo.getSubjectParentId());
        }

        if(!StringUtils.isEmpty(webCourseQueryVo.getSubjectId())){
            queryWrapper.eq("subject_id",webCourseQueryVo.getSubjectId());
        }

        if(!StringUtils.isEmpty(webCourseQueryVo.getBuyCountSort())){
            queryWrapper.orderByDesc("buy_count");
        }else if(!StringUtils.isEmpty(webCourseQueryVo.getGmtCreateSort())){
            queryWrapper.orderByDesc("gmt_create");
        }else if(!StringUtils.isEmpty(webCourseQueryVo.getPriceSort())){
            queryWrapper.orderByDesc("price");
        }else {
            queryWrapper.orderByDesc("id");
        }

        // 执行查询
        baseMapper.selectPage(pageParam,queryWrapper);
        // 组装结果
        List<Course> records = pageParam.getRecords();
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
    public WebCourseVo selectWebCourseVoById(String id) {
        // 获取课程浏览信息
        Course course = baseMapper.selectById(id);
        course.setViewCount(course.getViewCount()+1);
        // 跟新课程浏览信息
        baseMapper.updateById(course);
        // 获取课程信息
        return baseMapper.selectWebCourseVoById(id);
    }

    @Override
    public CourseDto getCourseDtoById(String id) {
        Course course = baseMapper.selectById(id);
        CourseDto courseDto = new CourseDto();
        BeanUtils.copyProperties(course,courseDto);
        Teacher teacher = teacherMapper.selectById(course.getTeacherId());
        courseDto.setTeacherName(teacher.getName());
        return courseDto;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateBuyCountById(String id) {
        Course course = baseMapper.selectById(id);
        long count = course.getBuyCount()+1;
        course.setBuyCount(count);
        baseMapper.updateById(course);
    }
}










