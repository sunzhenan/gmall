package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.vo.GroupVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.util.List;


/**
 * 属性分组
 *
 * @author chang
 * @email sunzhenan.june@gmail.com
 * @date 2020-08-27 16:56:25
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo queryGroupByPage(QueryCondition queryCondition, Long catId);

    GroupVO queryGroupWithAttrsByGid(Long gid);

    List<GroupVO> queryGroupWithAttrsByCid(Long cid);
}

