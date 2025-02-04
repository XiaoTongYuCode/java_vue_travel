package com.javahomework.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javahomework.common.QueryPageParam;
import com.javahomework.common.Result;
import com.javahomework.entity.*;
import com.javahomework.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author com
 * @since 2024-09-07
 */
@RestController
@RequestMapping("/scenic")
public class ScenicController {
    @Autowired
    private IScenicService iScenicService;
    @Autowired
    private IScenicTypeService iScenicTypeService;
    @Autowired
    private ICommentService iCommentService;
    @Autowired
    private INoticeService iNoticeService;
    @Autowired
    private IHotelService iHotelService;

    @Autowired
    private HttpServletRequest request;

    //新增
    @PostMapping("/add")
    public Result save(@RequestBody Scenic scenic) {
        return iScenicService.save(scenic) ? Result.suc() : Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result delete(@RequestParam String id) {
        Scenic scenic = iScenicService.getById(id);
        String oldImagePath = scenic.getImg();
        if (oldImagePath != null) {
            String url = System.getProperty("user.dir");//获取项目绝对路径
            oldImagePath = oldImagePath.replace(request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath()), url);
            File oldFile = new File(oldImagePath);
            if (oldFile.exists()) {
                oldFile.delete();
            } else {
                System.out.println("路线图片删除失败 路径: " + oldImagePath);
            }
        }
        return iScenicService.removeById(id) ? Result.suc() : Result.fail();
    }

    //更新
    @PostMapping("/up")
    public Result update(@RequestBody Scenic scenic) {
        Scenic old_scenic = iScenicService.getById(scenic.getId());
        // 价格变动公告添加
        if (!Objects.equals(old_scenic.getTickets(), scenic.getTickets())) {
            Notice notice = new Notice();
            notice.setTitle("景区价格变动");
            notice.setTxt("景区【"+scenic.getName()+"】门票现已调整为 "+scenic.getTickets()+" 元/张");
            iNoticeService.save(notice);
        }

        return iScenicService.updateById(scenic) ? Result.suc() : Result.fail();
    }


    //模糊查询
    @PostMapping("/list")
    public Result list(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        String type = (String) param.get("type");
        if (param.get("id") != null) {
            Integer id = Integer.parseInt((String) param.get("id"));
            Scenic serviceById = iScenicService.getById(id);
            List<Hotel> hotelList = iHotelService.lambdaQuery().eq(Hotel::getScenicId, id).list();
            List<Comment> commentList = iCommentService.lambdaQuery()
                    .eq(Comment::getType, "景区")
                    .eq(Comment::getScenicId, id)
                    .list();
            HashMap<String, Object> res = new HashMap<>();
            res.put("id", serviceById.getId());
            res.put("name", serviceById.getName());
            res.put("type", serviceById.getType());
            res.put("details", serviceById.getDetails());
            res.put("tickets", serviceById.getTickets());
            res.put("img", serviceById.getImg());
            res.put("hotel", hotelList);
            res.put("comment", commentList);

            return Result.suc(res);
        }

        Page<Scenic> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Scenic> lambdaQueryWrapper = new LambdaQueryWrapper();

        if (StringUtils.isNotBlank(name)) {
            lambdaQueryWrapper.like(Scenic::getName, name);
        }
        if (type != null && !type.isEmpty()) {
            lambdaQueryWrapper.like(Scenic::getType, type);
        }

        IPage result = iScenicService.page(page, lambdaQueryWrapper);

        HashMap<Object, Object> res = new HashMap<>();
        res.put("scenic", result.getRecords());
        res.put("type", iScenicTypeService.list());

        return Result.suc(res, result.getTotal());
    }


}
