package com.atguigu.gmall.pms.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;

import com.atguigu.gmall.pms.dao.SkuImagesDao;
import com.atguigu.gmall.pms.entity.SkuImagesEntity;
import com.atguigu.gmall.pms.service.SkuImagesService;
import org.springframework.util.CollectionUtils;


@Service("skuImagesService")
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesDao, SkuImagesEntity> implements SkuImagesService {

    @Autowired
    private SkuImagesDao skuImagesDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SkuImagesEntity> page = this.page(
                new Query<SkuImagesEntity>().getPage(params),
                new QueryWrapper<SkuImagesEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public String querySkuImgsBySkuId(Long skuId) {

        List<String> skuImgs = skuImagesDao.querySkuImgsBySkuId(skuId);
        skuImgs.forEach(System.out::println);
        if (!CollectionUtils.isEmpty(skuImgs)) {
            return StringUtils.join(skuImgs, ",");
        }
        return "";
    }

}