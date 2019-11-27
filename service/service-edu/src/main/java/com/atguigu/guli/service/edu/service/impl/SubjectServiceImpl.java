package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.common.base.util.ExcelImportUtil;
import com.atguigu.guli.common.base.util.ExceptionUtils;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.atguigu.guli.service.edu.service.SubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author leishuai
 * @since 2019-11-20
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchImport(InputStream inputStream) throws Exception {
        // 创建工具类对象
        ExcelImportUtil exceptionUtils = new ExcelImportUtil(inputStream);
        // 获取工作表
        HSSFSheet sheet = exceptionUtils.getSheet();

        // 遍历数据据行
        for (Row rowData : sheet) {
            // 跳过标题行
            if (rowData.getRowNum() == 0) {
                continue;
            }
            // 获取一级分类
            Cell levelOneCell = rowData.getCell(0);
            String levelOneValue = exceptionUtils.getCellValue(levelOneCell).trim();
            if(levelOneCell==null || StringUtils.isEmpty(levelOneValue)){
                continue;
            }

            // 获取二级分类
            Cell levelTwoCell = rowData.getCell(1);
            String levelTwoValue = exceptionUtils.getCellValue(levelTwoCell).trim();
            if(levelTwoCell==null || StringUtils.isEmpty(levelTwoValue)){
                continue;
            }
            // 判断一级分类是否重复
            Subject subject = this.getTitle(levelOneValue);
            String parentId = null;
            if(subject==null){
                // 如果不存在,则存储在一级分类上
                Subject subjectLevelOne = new Subject();
                subjectLevelOne.setTitle(levelOneValue);
                baseMapper.insert(subjectLevelOne);
                parentId = subjectLevelOne.getId();
            }else {
                parentId = subject.getId();
            }
            // 判断二级分类是否重复
            Subject subSubject = this.getSubTitle(levelTwoValue, parentId);
            if(subSubject==null){
                // 如果不存在就存储二级分类
                Subject subjectLevelTwo = new Subject();
                subjectLevelTwo.setTitle(levelTwoValue);
                subjectLevelTwo.setParentId(parentId);
                baseMapper.insert(subjectLevelTwo);
            }
        }
    }

    @Override
    public List<SubjectVo> nestedList() {
        // 最终要得到的数据列表
        List<SubjectVo> subjectVoList = new ArrayList<>();

        // 查询所有记录
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort", "id");
        List<Subject> subjectList = baseMapper.selectList(queryWrapper);

        // 获取一级记录和二级记录
        List<Subject> subjectLevelOneList = new ArrayList<>();
        List<Subject> subjectLevelTwoList = new ArrayList<>();
        for (Subject subject : subjectList) {
            if(subject.getParentId().equals("0")){
                subjectLevelOneList.add(subject);
            }else{
                subjectLevelTwoList.add(subject);
            }
        }
        //遍历一级数据列表
        for (Subject subjectLevelOne : subjectLevelOneList) {

            //创建一级vo对象
            SubjectVo subjectVoLevelOne = new SubjectVo();
            BeanUtils.copyProperties(subjectLevelOne, subjectVoLevelOne);
            subjectVoList.add(subjectVoLevelOne);

            //填充二级类别
            List<SubjectVo> subjectVoLevelTwoList = new ArrayList<>();
            for (Subject subjectLevelTwo : subjectLevelTwoList) {
                if(subjectLevelTwo.getParentId().equals(subjectLevelOne.getId())){

                    //创建二级vo对象
                    SubjectVo subjectVoLevelTwo = new SubjectVo();
                    BeanUtils.copyProperties(subjectLevelTwo, subjectVoLevelTwo);
                    subjectVoLevelTwoList.add(subjectVoLevelTwo);
                }
            }
            subjectVoLevelOne.setChildren(subjectVoLevelTwoList);
        }
        return subjectVoList;
    }

    @Override
    public List<SubjectVo> nestedList2() {
        return baseMapper.selectNestedListByParentId("0");
    }

    /**
     * 判断一级类别是否存在
     * @param title
     * @return
     */
    private Subject getTitle(String title){
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",title);
        queryWrapper.eq("parent_id","0");
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 判断二级类别是否存在
     * @param title
     * @param parentId
     * @return
     */
    private Subject getSubTitle(String title,String parentId){

        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",title);
        queryWrapper.eq("parent_id",parentId);
        return baseMapper.selectOne(queryWrapper);
    }

}
