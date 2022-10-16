package com.zhr.blog01.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhr.blog01.dao.mapper.CommentMapper;
import com.zhr.blog01.dao.pojo.Comment;
import com.zhr.blog01.service.CommentService;
import com.zhr.blog01.service.SysUserService;
import com.zhr.blog01.vo.params.CommentVo;
import com.zhr.blog01.vo.params.Result;
import com.zhr.blog01.vo.params.UserVo;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    SysUserService sysUserService;


    public CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        //时间格式化
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        Long authorId = comment.getAuthorId();
        UserVo userVo = sysUserService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        //评论的评论
        List<CommentVo> commentVoList = findCommentsByParentId(comment.getId());
        commentVo.setChildrens(commentVoList);
        if (comment.getLevel() > 1) {
            Long toUid = comment.getToUid();
            UserVo toUserVo = sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, id);
        queryWrapper.eq(Comment::getLevel, 2);
        List<Comment> comments = this.commentMapper.selectList(queryWrapper);
        return copyList(comments);
    }

    public List<CommentVo> copyList(List<Comment> commentList) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    @Override
    public Result commentsByArticleId(Long id) {
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getArticleId, id);
        commentLambdaQueryWrapper.eq(Comment::getLevel, 1);
        List<Comment> comments = commentMapper.selectList(commentLambdaQueryWrapper);
        return Result.success(copyList(comments));
    }
}
