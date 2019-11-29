package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.entity.vo.VideoInfoForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author leishuai
 * @since 2019-11-20
 */
public interface VideoService extends IService<Video> {
    /**
     * 新增课时
     * @param videoInfoForm
     */
    void saveVideoInfoForm(VideoInfoForm videoInfoForm);

    /**
     * 根据id查询课时信息
     * @param id
     * @return
     */
    VideoInfoForm getVideoInfoFormById(String id);

    /**
     * 更新课时
     * @param videoInfoForm
     */
    void updateVideoInfoForm(VideoInfoForm videoInfoForm);

    /**
     * 删除课时对象
     * @param id
     */
    void removeVideoById(String id);
}
