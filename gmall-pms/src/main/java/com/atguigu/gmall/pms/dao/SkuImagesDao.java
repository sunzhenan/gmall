package com.atguigu.gmall.pms.dao;

import com.atguigu.gmall.pms.entity.SkuImagesEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * sku图片
 * 
 * @author chang
 * @email sunzhenan.june@gmail.com
 * @date 2020-08-27 16:56:25
 */
@Mapper
public interface SkuImagesDao extends BaseMapper<SkuImagesEntity> {

    List<String> querySkuImgsBySkuId(Long skuId);
}
