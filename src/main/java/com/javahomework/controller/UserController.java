package com.javahomework.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javahomework.common.QueryPageParam;
import com.javahomework.common.Result;
import com.javahomework.entity.User;
import com.javahomework.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author javahomework
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/hi")
    @ResponseBody
    public String hi() {
        return "<div style=\"width: 100%;text-align: center;\"><h1 >感谢您的授权，您现在可以关闭此页面了</h1></div>";
    }

//    @GetMapping("/listAll")
//    public List<User> list() {
//        return iUserService.listAll();
//    }

    //新增
    @PostMapping("/add")
    public Result save(@RequestBody User user) {
        return iUserService.save(user) ? Result.suc() : Result.fail();
    }

    //更新
    @PostMapping("/up")
    public Result update(@RequestBody User user) {
        return iUserService.updateById(user) ? Result.suc() : Result.fail();
    }

    //个体查询
    @GetMapping("/only")
    public Result only(@RequestParam String no) {
        User foundUser = iUserService.getById(no);
        return foundUser != null ? Result.fail(foundUser) : Result.suc();
    }

    //删除
    @GetMapping("/delete")
    public Result delete(@RequestParam String no) {

//        iUserService.delFile(id);
        return iUserService.removeById(no) ? Result.suc() : Result.fail();
    }

    //登陆
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        List<User> list = iUserService.lambdaQuery()
                .eq(User::getNo, user.getNo())
                .eq(User::getPassword, user.getPassword()).list();

        if (!list.isEmpty()){
            User user1 = list.get(0);
            HashMap res = new HashMap();
            res.put("user",user1);
            return Result.suc(res);
        }
        return Result.fail();
    }

    //模糊查询
    @PostMapping("/list")
    public Result listPageN(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");

        Page<User> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();

        if (StringUtils.isNotBlank(name)) {
            lambdaQueryWrapper.like(User::getName, name);
        }

        IPage result = iUserService.page(page, lambdaQueryWrapper);

        return Result.suc(result.getRecords(), result.getTotal());
    }

    @PostMapping("/addMoney")
    public Result addMoney(@RequestBody HashMap hashMap) {
        Integer num = (Integer) hashMap.get("num");
        String no = (String) hashMap.get("no");

        User user = iUserService.getById(no);

        if (user == null) {
            return Result.fail("User not found");
        }

        user.setMoney(user.getMoney() + num);
        iUserService.updateById(user);

        return Result.suc();

    }

    //    //头像
//    @RequestMapping("/up/{userId}")
//    public Result upload(@RequestParam("file") MultipartFile img, @PathVariable Integer userId ){
//        String url=System.getProperty("user.dir");//获取项目绝对路径
//        User user = iUserService.getById(userId);
//
//        if(!img.isEmpty()){
//            UUID id=UUID.randomUUID();//生成文件名
//            try {
//                //保存上传的资源文件路径，路径在部署jar包同级目录。
//                String path = url+"/static/img/";
//                File dir = new File(path);
//                if(!dir.exists()){
//                    dir.mkdirs();
//                }
//                //删除原本头像
//                String oldImagePath = user.getAvatar();
//                if (oldImagePath != null) {
//                    oldImagePath = oldImagePath.replace(request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath()), url);
//                    File oldFile = new File(oldImagePath);
//                    if (oldFile.exists()) {
//                        oldFile.delete();
//                    }
//                    else {
//                        System.out.println("旧头像删除失败 路径: " + oldImagePath );
//                    }
//                }
//                //参数就是图片保存在服务器的本地地址
//                img.transferTo(new File(path+id+".png"));
//                String imgHtml = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath())+"/static/img/"+id+".png";
//                System.out.println(imgHtml);
//                User byId = iUserService.getById(userId);
//                byId.setAvatar(imgHtml);
//                iUserService.updateById(byId);
//                return  Result.suc(iUserService.getById(userId));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return Result.fail();
//        }
//        return Result.fail();
//    }
}
