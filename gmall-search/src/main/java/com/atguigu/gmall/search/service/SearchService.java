package com.atguigu.gmall.search.service;

import com.atguigu.gmall.search.pojo.SearchParam;
import com.atguigu.gmall.search.pojo.SearchResponseVO;
//import com.atguigu.gmall.search.vo.SearchResponseVO;

import java.io.IOException;

public interface SearchService {
    /**
     * 商品搜索
     * @param searchParam
     * @return
     */
    SearchResponseVO search(SearchParam searchParam) throws IOException;
}
