package com.javahomework.controller;

import com.javahomework.common.QueryPageParam;
import com.javahomework.common.Result;
import com.javahomework.entity.OrderForm;
import com.javahomework.entity.ScenicType;
import com.javahomework.entity.User;
import com.javahomework.service.IScenicTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author com
 * @since 2024-09-07
 */
@RestController
@RequestMapping("/type")
public class ScenicTypeController {
    @Autowired
    private IScenicTypeService iScenicTypeService;

    @PostMapping("/list")
    public Result list() {
        return Result.suc(iScenicTypeService.list());
    }
    //新增
    @PostMapping("/add")
    public Result save(@RequestBody ScenicType scenicType) {
        return iScenicTypeService.save(scenicType) ? Result.suc() : Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result delete(@RequestParam String id) {
        return iScenicTypeService.removeById(id) ? Result.suc() : Result.fail();
    }

}
