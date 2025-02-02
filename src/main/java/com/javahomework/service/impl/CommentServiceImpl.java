package com.javahomework.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javahomework.entity.Comment;
import com.javahomework.mapper.CommentMapper;
import com.javahomework.service.ICommentService;
import org.springframework.stereotype.Service;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author com
 * @since 2024-09-06
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
