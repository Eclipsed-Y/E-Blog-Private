package cn.ecnu.eblog.article.controller;

import cn.ecnu.eblog.article.service.ArticleService;
import cn.ecnu.eblog.common.annotation.Inner;
import cn.ecnu.eblog.common.context.BaseContext;
import cn.ecnu.eblog.common.pojo.dto.ArticleDTO;
import cn.ecnu.eblog.common.pojo.dto.ArticlePageQueryDTO;
import cn.ecnu.eblog.common.pojo.dto.UserInfoDTO;
import cn.ecnu.eblog.common.pojo.entity.article.ArticleDO;
import cn.ecnu.eblog.common.pojo.result.PageResult;
import cn.ecnu.eblog.common.pojo.result.Result;
import cn.ecnu.eblog.common.pojo.vo.ArticleDetailVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 根据分类查看所有文章
     * @param articlePageQueryDTO
     * @return
     */
    @GetMapping
    public Result<PageResult> getByCategoryId(ArticlePageQueryDTO articlePageQueryDTO) {
        log.info("id: {} 用户分页查询", BaseContext.getCurrentId());
        PageResult pageResult = articleService.getByCategoryId(articlePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id查看文章细节
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<ArticleDetailVO> getArticleDetail(@PathVariable("id") Long id){
        log.info("id: {} 用户查看id: {} 文章", BaseContext.getCurrentId(), id);
        ArticleDetailVO articleDetailVO = articleService.getArticleDetail(id);
        return Result.success(articleDetailVO);
    }

    /**
     * 查询文章是否存在，内部接口
     * @param id
     * @return
     */
    @GetMapping("/exists/{id}")
    @Inner
    public Result<Boolean> existsArticle(@PathVariable("id") Long id){
        log.info("id: {} 用户内部调用查询文章是否存在", BaseContext.getCurrentId());
        boolean exists = articleService.exists(new QueryWrapper<ArticleDO>().eq("id", id).eq("deleted", 0).eq("status", 1));
        return Result.success(exists);
    }

    /**
     * 内部接口，修改用户信息
     * @param userInfoDTO
     * @return
     */
    @PutMapping("/userInfo")
    @Inner
    public Result<?> updateUserInfo(@RequestBody UserInfoDTO userInfoDTO){
        log.info("id: {} 用户内部调用修改用户信息", BaseContext.getCurrentId());
        articleService.updateUserInfo(userInfoDTO);
        return Result.success();
    }

    /**
     * 新增文章
     * @param articleDTO
     * @return
     */
    @PostMapping()
    public Result<Long> insertArticle(@RequestBody ArticleDTO articleDTO){
        log.info("id: {} 用户新增文章", BaseContext.getCurrentId());
        long id = articleService.insertArticle(articleDTO);
        return Result.success(id);
    }

    /**
     * 更新文章
     * @param articleDTO
     * @return
     */
    @PutMapping
    public Result<Long> updateArticle(@RequestBody ArticleDTO articleDTO){
        log.info("id: {} 用户修改文章", BaseContext.getCurrentId());
        long id = articleService.updateArticle(articleDTO);
        return Result.success(id);
    }

    /**
     * 删除文章
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<?> deleteArticle(@PathVariable("id") Long id){
        log.info("id: {} 用户删除文章", BaseContext.getCurrentId());
        articleService.deleteArticle(id);
        return Result.success();
    }
}
