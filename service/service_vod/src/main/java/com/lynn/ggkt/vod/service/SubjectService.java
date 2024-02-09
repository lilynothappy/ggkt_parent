package com.lynn.ggkt.vod.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lynn.ggkt.model.vod.Subject;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author lynn
 * @since 2022-07-03
 */
public interface SubjectService extends IService<Subject> {


    List<Subject> selectSubjectList(Long id);

    void exportData(HttpServletResponse response);

    void importData(MultipartFile file);
}
