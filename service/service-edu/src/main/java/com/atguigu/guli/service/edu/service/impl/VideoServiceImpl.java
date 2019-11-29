package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.entity.vo.VideoInfoForm;
import com.atguigu.guli.service.edu.mapper.VideoMapper;
import com.atguigu.guli.service.edu.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author leishuai
 * @since 2019-11-20
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Override
    public void saveVideoInfoForm(VideoInfoForm videoInfoForm) {
        Video video = new Video();
        BeanUtils.copyProperties(videoInfoForm,video);
        baseMapper.insert(video);
    }

    @Override
    public VideoInfoForm getVideoInfoFormById(String id) {
        VideoInfoForm videoInfoForm = new VideoInfoForm();
        Video video = baseMapper.selectById(id);
        BeanUtils.copyProperties(video,videoInfoForm);
        return videoInfoForm;
    }

    @Override
    public void updateVideoInfoForm(VideoInfoForm videoInfoForm) {
        Video video = new Video();
        BeanUtils.copyProperties(videoInfoForm,video);
        baseMapper.updateById(video);
    }

    @Override
    public void removeVideoById(String id) {
        // 删除视频资源

        // 根据id删除课时对象
        baseMapper.deleteById(id);
    }


}
