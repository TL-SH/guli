package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.CourseCollect;
import com.atguigu.guli.service.edu.entity.vo.CourseCollectVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 课程收藏 服务类
 * </p>
 *
 * @author leishuai
 * @since 2019-11-20
 */
public interface CourseCollectService extends IService<CourseCollect> {
    /**
     * 分页显示收藏的信息
     * @param pageParam
     * @param memberIdByJwtToken
     * @return
     */
    Map<String, Object> selectPage(Page<CourseCollectVo> pageParam, String memberIdByJwtToken);

    /**
     * 收藏课程
     * @param courseId
     * @param memberId
     */
    void saveCourseCollect(String courseId, String memberId);
}
