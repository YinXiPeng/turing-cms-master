package com.bonc.turing.cms.manage.service.competition;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: cms
 * @Package: com.bonc.turing.cms.manage.service.competition
 * @ClassName: DiscussService
 * @Author: bxt
 * @Description:
 * @Date: 2019/7/17 15:09
 * @Version: 1.0
 */
public interface DiscussService {

     /**
      * 方法说明:  讨论列表
      * @date 2019/7/17 16:22
      * @author bxt
      * @version v 1.0.0
      */
    List<Map<String, Object>> getDiscussList(Pageable pageable, Map<String, Object> map);

     /**
      * 方法说明:  讨论列表总条数
      * @date 2019/7/17 16:22
      * @author bxt
      * @version v 1.0.0
      */
    int getRequirementListCount(Map<String, Object> map);

     /**
      * 方法说明:  置顶
      * @date 2019/7/17 16:22
      * @author bxt
      * @version v 1.0.0
      */
    void updateTopping(String id);

     /**
      * 方法说明:  取消置顶
      * @date 2019/7/17 16:23
      * @author bxt
      * @version v 1.0.0
      */
    void updateCacleTopping(String id);

     /**
      * 方法说明:  加精
      * @date 2019/7/17 16:23
      * @author bxt
      * @version v 1.0.0
      */
    void updateRefinement(String id);

     /**
      * 方法说明:  取消加精
      * @date 2019/7/17 16:23
      * @author bxt
      * @version v 1.0.0
      */
    void updateCacleRefinement(String id);

     /**
      * 方法说明:  显示
      * @date 2019/7/17 16:23
      * @author bxt
      * @version v 1.0.0
      */
    void updateShow(String id);

     /**
      * 方法说明:  隐藏
      * @date 2019/7/17 16:23
      * @author bxt
      * @version v 1.0.0
      */
    void updateHide(String id);

     /**
      * 方法说明:  删除
      * @date 2019/7/17 16:23
      * @author bxt
      * @version v 1.0.0
      */
    void deleteDiscuss(String id);
    void updateRemark(String id, JSONObject params);
}
