package cn.ecnu.eblog.article.mapper;

import cn.ecnu.eblog.common.pojo.entity.article.ArticleDO;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends MPJBaseMapper<ArticleDO> {
}
