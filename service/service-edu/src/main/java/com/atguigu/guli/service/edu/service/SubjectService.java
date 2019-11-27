package com.atguigu.guli.service.edu.service;

import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author leishuai
 * @since 2019-11-20
 */
public interface SubjectService extends IService<Subject> {

    /**
     * 批量导入
     * @param inputStream
     * @throws Exception
     */
    void batchImport(InputStream inputStream) throws Exception;

    /**
     * 嵌套数据列表
     * @return
     */
    List<SubjectVo> nestedList();

    /**
     * 多条语句查询
     * @return
     */
    List<SubjectVo> nestedList2();
}
