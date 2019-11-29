package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
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
public interface ChapterService extends IService<Chapter> {
    /**
     * 根据id删除章节信息
     * @param id
     */
    void removeChapterById(String id);

    /**
     * 嵌套章节数据查询
     * @param courseId
     * @return
     */
    List<ChapterVo> nestedList(String courseId);
}
