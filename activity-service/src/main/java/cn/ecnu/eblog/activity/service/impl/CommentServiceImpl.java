package cn.ecnu.eblog.activity.service.impl;

import cn.ecnu.eblog.activity.mapper.CommentMapper;
import cn.ecnu.eblog.activity.service.CommentService;
import cn.ecnu.eblog.common.constant.MessageConstant;
import cn.ecnu.eblog.common.exception.AccessException;
import cn.ecnu.eblog.common.exception.RequestExcetption;
import cn.ecnu.eblog.common.feign.ArticleClient;
import cn.ecnu.eblog.common.pojo.dto.CommentDTO;
import cn.ecnu.eblog.common.pojo.entity.activity.CommentDO;
import cn.ecnu.eblog.common.pojo.result.Result;
import com.alibaba.nacos.shaded.org.checkerframework.checker.index.qual.SameLen;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class CommentServiceImpl extends MPJBaseServiceImpl<CommentMapper, CommentDO> implements CommentService {
    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleClient articleClient;

    @Override
    public void insertComment(CommentDTO commentDTO) {
        CommentDO commentDO = new CommentDO();
        BeanUtils.copyProperties(commentDTO, commentDO);
        // 判断是否合法
        if (commentDO.getId() != null || commentDO.getArticleId() == null || !articleClient.existsArticle(commentDO.getArticleId()).getData() || commentDO.getRootCommentId() == null || commentDO.getParentCommentId() == null) {
            log.info("id: {} 用户评论失败", commentDO.getUserId());
            return;
        }
        if (commentDO.getRootCommentId() != 0){
            CommentDO root = commentService.getById(commentDO.getRootCommentId());
            if (root == null || !Objects.equals(root.getArticleId(), commentDO.getArticleId())){
                log.info("id: {} 用户评论失败", commentDO.getUserId());
                return;
            }
        }
        if (Objects.equals(commentDO.getParentCommentId(), commentDO.getRootCommentId())){
            commentDO.setParentCommentId(0L);
        }
        if (commentDO.getParentCommentId() != 0){
            CommentDO parent = commentService.getById(commentDO.getParentCommentId());
            if (parent == null || !Objects.equals(parent.getArticleId(), commentDO.getArticleId())){
                log.info("id: {} 用户评论失败", commentDO.getUserId());
                return;
            }
        }
        commentService.save(commentDO);
    }
}