package com.atguigu.guli.service.edu.mapper;

import com.atguigu.guli.service.edu.entity.CourseCollect;
import com.atguigu.guli.service.edu.entity.vo.CourseCollectVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 课程收藏 Mapper 接口
 * </p>
 *
 * @author leishuai
 * @since 2019-11-20
 */
@Repository
public interface CourseCollectMapper extends BaseMapper<CourseCollect> {
    /**
     * 分页
     * @param page
     * @param memberId
     * @return
     */
    IPage<CourseCollectVo> selectPage(
            Page<CourseCollectVo> page,
            @Param("memberId") String memberId);
}
