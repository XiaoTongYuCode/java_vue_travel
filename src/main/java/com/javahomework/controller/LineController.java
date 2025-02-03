package com.javahomework.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javahomework.common.QueryPageParam;
import com.javahomework.common.Result;
import com.javahomework.entity.Comment;
import com.javahomework.entity.Line;
import com.javahomework.entity.User;
import com.javahomework.service.ICommentService;
import com.javahomework.service.ILineService;
import com.javahomework.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author com
 * @since 2024-09-09
 */
@RestController
@RequestMapping("/line")
public class LineController {

    @Autowired
    private ILineService ilineService;
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICommentService iCommentService;

    @Autowired
    private HttpServletRequest request;

    //新增
    @PostMapping("/add")
    public Result save(@RequestBody Line line) {
        return ilineService.save(line) ? Result.suc() : Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result delete(@RequestParam String id) {
        Line line = ilineService.getById(id);
        String oldImagePath = line.getImg();
        if (oldImagePath != null) {
            String url=System.getProperty("user.dir");//获取项目绝对路径
            oldImagePath = oldImagePath.replace(request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath()), url);
            File oldFile = new File(oldImagePath);
            if (oldFile.exists()) {
                oldFile.delete();
            } else {
                System.out.println("图片删除失败 路径: " + oldImagePath);
            }
        }
        return ilineService.removeById(id) ? Result.suc() : Result.fail();
    }

    //更新
    @PostMapping("/up")
    public Result update(@RequestBody Line line) {
        return ilineService.updateById(line) ? Result.suc() : Result.fail();
    }

    //模糊查询
    @PostMapping("/list")
    public Result list(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");

        Page<Line> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Line> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(name)) {
            lambdaQueryWrapper.like(Line::getName, name);
        }

        // 获取分页结果
        IPage<Line> linePage = ilineService.page(page, lambdaQueryWrapper);
        List<Line> lineList = linePage.getRecords();
        List<Object> res = new ArrayList<>();
        for (Line line : lineList) {
            HashMap<String, Object> info = new HashMap<>();
            List<Comment> commentList = iCommentService.lambdaQuery()
                    .eq(Comment::getType, "攻略")
                    .eq(Comment::getScenicId, line.getId())
                    .list();
            info.put("id", line.getId());
            info.put("no", line.getNo());
            info.put("userName", iUserService.getById(line.getNo()).getName());
            info.put("name", line.getName());
            info.put("txt", line.getTxt());
            info.put("img", line.getImg());
            info.put("tickets", line.getTickets());
            info.put("commentList", commentList);
            res.add(info);
        }

        return Result.suc(res, linePage.getTotal());
    }

}
