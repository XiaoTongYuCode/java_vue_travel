package com.javahomework.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javahomework.common.QueryPageParam;
import com.javahomework.common.Result;
import com.javahomework.entity.Comment;
import com.javahomework.entity.Hotel;
import com.javahomework.entity.Scenic;
import com.javahomework.service.IHotelService;
import com.javahomework.service.IScenicService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author com
 * @since 2024-09-07
 */
@RestController
@RequestMapping("/hotel")
public class HotelController {
    @Autowired
    private IHotelService iHotelService;
    @Autowired
    private IScenicService iScenicService;

    @Autowired
    private HttpServletRequest request;

    //新增
    @PostMapping("/add")
    public Result save(@RequestBody Hotel hotel) {
        return iHotelService.save(hotel) ? Result.suc() : Result.fail();
    }

    //更新
    @PostMapping("/up")
    public Result update(@RequestBody Hotel hotel) {
        return iHotelService.updateById(hotel) ? Result.suc() : Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result delete(@RequestParam String id) {
        Hotel hotel = iHotelService.getById(id);
        String oldImagePath = hotel.getImg();
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
        return iHotelService.removeById(id) ? Result.suc() : Result.fail();
    }

    //模糊查询
    @PostMapping("/list")
    public Result list(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        String scenicName = (String) param.get("scenicName");

        Page<Hotel> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Hotel> lambdaQueryWrapper = new LambdaQueryWrapper();

        if (StringUtils.isNotBlank(name)) {
            lambdaQueryWrapper.like(Hotel::getName, name);
        }
        if (param.get("scenic_id") != null) {
            Integer scenic_id = Integer.parseInt((String) param.get("scenic_id"));
            lambdaQueryWrapper.eq(Hotel::getScenicId, scenic_id);
        }
        if (StringUtils.isNotBlank(scenicName)) {
            List<Scenic> scenicList = iScenicService.lambdaQuery().like(Scenic::getName, scenicName).list();
            if (!scenicList.isEmpty()) {
                Scenic scenic = scenicList.get(0);
                lambdaQueryWrapper.eq(Hotel::getScenicId, scenic.getId());
            }
        }

        // 按 id 倒序排列
        lambdaQueryWrapper.orderByDesc(Hotel::getId);
        IPage result = iHotelService.page(page, lambdaQueryWrapper);

        List<Hotel> records = result.getRecords();
        List<HashMap<String,Object>> maps = new ArrayList<>();
        for (Hotel record : records) {
            HashMap<String, Object> row = new HashMap<>();
            row.put("id",record.getId());
            row.put("scenicId",record.getScenicId());
            row.put("scenicName",iScenicService.getById(record.getScenicId()).getName());
            row.put("name",record.getName());
            row.put("txt",record.getTxt());
            row.put("img",record.getImg());
            row.put("pid",record.getPid());
            row.put("tickets",record.getTickets());
            row.put("distance",record.getDistance());
            maps.add(row);
        }
        return Result.suc(maps, result.getTotal());
    }


}
