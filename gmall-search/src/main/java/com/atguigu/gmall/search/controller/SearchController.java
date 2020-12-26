package com.atguigu.gmall.search.controller;

import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.search.pojo.SearchParam;
import com.atguigu.gmall.search.pojo.SearchResponseVO;
import com.atguigu.gmall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    private SearchService searchService;

//    @PostMapping("page")
    @GetMapping
    public Resp<SearchResponseVO> search(SearchParam searchParam) throws IOException {
        System.out.println("test search");
        SearchResponseVO responseVO = searchService.search(searchParam);
        return Resp.ok(responseVO);
    }

}
