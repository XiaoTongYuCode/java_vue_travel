package com.javahomework.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javahomework.common.QueryPageParam;
import com.javahomework.common.Result;
import com.javahomework.entity.Notice;
import com.javahomework.service.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author com
 * @since 2025-02-03
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private INoticeService iNoticeService;

    //新增
    @PostMapping("/add")
    public Result save(@RequestBody Notice notice) {
        return iNoticeService.save(notice) ? Result.suc() : Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result delete(@RequestParam String id) {
        return iNoticeService.removeById(id) ? Result.suc() : Result.fail();
    }

    //更新
    @PostMapping("/up")
    public Result update(@RequestBody Notice notice) {
        return iNoticeService.updateById(notice) ? Result.suc() : Result.fail();
    }


    //模糊查询
    @PostMapping("/list")
    public Result list(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String title = (String) param.get("title");

        Page<Notice> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Notice> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(title)) {
            lambdaQueryWrapper.like(Notice::getTitle, title);
        }

        // 按id倒叙排序
        lambdaQueryWrapper.orderByDesc(Notice::getId);

        // 获取分页结果
        IPage<Notice> noticePage = iNoticeService.page(page, lambdaQueryWrapper);
        List<Notice> NoticeList = noticePage.getRecords();
        List<Object> res = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Notice notice : NoticeList) {
            HashMap<String, Object> info = new HashMap<>();
            info.put("id", notice.getId());
            info.put("title", notice.getTitle());
            info.put("txt", notice.getTxt());
            // 使用 DateTimeFormatter 格式化 LocalDateTime
            String formattedDate = notice.getCreateTime().format(formatter);
            info.put("createTime", formattedDate); // 格式化时间戳
            res.add(info);
        }

        return Result.suc(res, noticePage.getTotal());
    }

}
