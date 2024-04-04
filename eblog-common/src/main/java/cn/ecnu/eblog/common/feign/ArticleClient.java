package cn.ecnu.eblog.common.feign;

import cn.ecnu.eblog.common.pojo.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("article-service")
public interface ArticleClient {
    @GetMapping(value = "/article/exists/{id}", headers = {"userId={id}", "from=E"})
    Result<Boolean> existsArticle(@PathVariable("id") Long id);
}
