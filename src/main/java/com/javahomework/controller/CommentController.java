package com.javahomework.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javahomework.common.QueryPageParam;
import com.javahomework.common.Result;
import com.javahomework.entity.Comment;
import com.javahomework.entity.OrderForm;
import com.javahomework.entity.Scenic;
import com.javahomework.service.ICommentService;
import com.javahomework.service.IScenicService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author com
 * @since 2024-09-07
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private ICommentService iCommentService;
    @Autowired
    private IScenicService iScenicService;

    @Autowired
    private HttpServletRequest request;

    //新增
    @PostMapping("/add")
    public Result save(@RequestBody Comment comment) {
        comment.setTime(System.currentTimeMillis() / 1000);
        return iCommentService.save(comment) ? Result.suc() : Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result delete(@RequestParam String id) {
        return iCommentService.removeById(id) ? Result.suc() : Result.fail();
    }

    //模糊查询
    @PostMapping("/list")
    public Result list(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String txt = (String) param.get("txt");

        Page<Comment> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper();

        if (StringUtils.isNotBlank(txt)) {
            lambdaQueryWrapper.like(Comment::getTxt, txt);
        }

        // 按 id 倒序排列
        lambdaQueryWrapper.orderByDesc(Comment::getId);

        IPage<Comment> result = iCommentService.page(page, lambdaQueryWrapper);

        List<HashMap> res = new ArrayList<>();
        for (Comment comment : result.getRecords()) {
            Scenic scenic = iScenicService.getById(comment.getScenicId());
            // 创建一个新的 HashMap
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", comment.getId());
            map.put("no", comment.getNo());
            map.put("name", comment.getName());
            map.put("txt", comment.getTxt());
            map.put("time", comment.getTime());

            map.put("scenicId", scenic.getId());
            map.put("scenicName", scenic.getName());

            res.add(map);
        }

        return Result.suc(res, result.getTotal());
    }

}
