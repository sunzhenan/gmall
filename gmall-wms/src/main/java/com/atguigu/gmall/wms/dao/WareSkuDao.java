package com.atguigu.gmall.wms.dao;

import com.atguigu.gmall.wms.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author chang
 * @email sunzhenan.june@gmail.com
 * @date 2020-08-29 15:22:32
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	
}
