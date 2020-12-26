package com.atguigu.gmall.search;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.search.feign.GmallPmsClient;
import com.atguigu.gmall.search.feign.GmallWmsClient;
import com.atguigu.gmall.search.pojo.Goods;
import com.atguigu.gmall.search.pojo.SearchAttr;
import com.atguigu.gmall.search.repository.GoodsRepository;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class GmallSearchApplicationTests {

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GmallPmsClient pmsClient;
    @Autowired
    private GmallWmsClient wmsClient;

    @Test
    void contextLoads() {
        this.restTemplate.createIndex(Goods.class);
        this.restTemplate.putMapping(Goods.class);
    }

    @Test
    void importData() {
        Long pageNum = 1l;
        Long pageSize = 100l;

        do {
            //分页查询spu
            QueryCondition queryCondition = new QueryCondition();
            queryCondition.setPage(pageNum);
            queryCondition.setLimit(pageSize);
            Resp<List<SpuInfoEntity>> spuResp = pmsClient.querySpusByPage(queryCondition);
            List<SpuInfoEntity> spus = spuResp.getData();

            //遍历spu 查询sku
            spus.forEach(spuInfoEntity -> {
                Resp<List<SkuInfoEntity>> skusResp = pmsClient.querySkuBySpuId(spuInfoEntity.getId());
                List<SkuInfoEntity> skuInfoEntities = skusResp.getData();
                if (!CollectionUtils.isEmpty(skuInfoEntities)) {
                    //吧sku转换为 goods
                    List<Goods> goodsEntityList = skuInfoEntities.stream().map(skuInfoEntity -> {
                        Goods goodsEntity = new Goods();
                        //查询搜索属性及值
                        Resp<List<ProductAttrValueEntity>> attrResp = pmsClient.querySearchAttrValueBySpuId(spuInfoEntity.getId());
                        List<ProductAttrValueEntity> attrValueEntities = attrResp.getData();
                        if (!CollectionUtils.isEmpty(attrValueEntities)) {
                            List<SearchAttr> searchAttrs = attrValueEntities.stream().map(productAttrValueEntity -> {
                                SearchAttr searchAttr = new SearchAttr();
                                searchAttr.setAttrId(productAttrValueEntity.getAttrId());
                                searchAttr.setAttrName(productAttrValueEntity.getAttrName());
                                searchAttr.setAttrValue(productAttrValueEntity.getAttrValue());
                                return searchAttr;
                            }).collect(Collectors.toList());
                            goodsEntity.setAttrs(searchAttrs);
                        }
                        //查询品牌

                        Resp<BrandEntity> brandEntityResp = pmsClient.queryBrandById(skuInfoEntity.getBrandId());
                        BrandEntity brandEntity = brandEntityResp.getData();
                        if (brandEntity != null) {
                            goodsEntity.setBrandId(skuInfoEntity.getBrandId());
                            goodsEntity.setBrandName(brandEntity.getName());
                        }
                        //查询分类
                        Resp<CategoryEntity> categoryEntityResp = pmsClient.queryCategoryById(skuInfoEntity.getCatalogId());
                        CategoryEntity categoryEntity = categoryEntityResp.getData();
                        if (categoryEntity != null) {
                            goodsEntity.setCategoryId(skuInfoEntity.getCatalogId());
                            goodsEntity.setCategoryName(categoryEntity.getName());
                        }

                        goodsEntity.setCreateTime(spuInfoEntity.getCreateTime());
                        goodsEntity.setPic(skuInfoEntity.getSkuDefaultImg());

                        //查询skuImage 设置查询结果图片
                        Resp<String> skuImgs = pmsClient.querySkuImgsBySkuId(skuInfoEntity.getSkuId());
                        //接口返回  用 ，分隔的字符串  前端需要用，号解析
                        goodsEntity.setPic(skuImgs.getData());
//                        skuInfoEntity.geti
                        //设置价格
                        goodsEntity.setPrice(skuInfoEntity.getPrice().doubleValue());
                        goodsEntity.setSale(0l);
                        goodsEntity.setSkuId(skuInfoEntity.getSkuId());
                        //查询库存
                        Resp<List<WareSkuEntity>> listResp = wmsClient.queryWareSkusBySkuId(skuInfoEntity.getSkuId());
                        List<WareSkuEntity> data = listResp.getData();
                        if (!CollectionUtils.isEmpty(data)) {
                            boolean flag = data.stream().anyMatch(wareSkuEntity -> wareSkuEntity.getStock() > 0);
                            goodsEntity.setStore(flag);
                        }else {
                            goodsEntity.setStore(false);
                        }
                        goodsEntity.setTitle(skuInfoEntity.getSkuTitle());


                        return goodsEntity;
                    }).collect(Collectors.toList());
                    goodsRepository.saveAll(goodsEntityList);
                }

            });

            pageSize = (long)spus.size();
            pageNum++;

        }while (pageSize == 100);
    }

}
