package ibf2022.ssf.newsapi.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ibf2022.ssf.newsapi.models.News;
import ibf2022.ssf.newsapi.services.NewsServices;

@Controller
@RequestMapping(path = "/top-headlines")
public class TopNewsController {
    @Autowired
    private NewsServices newsSvc;

    @GetMapping
    public String getEverything(Model model) {
        Optional<List<News>> opt = newsSvc.getTopHeadlines();
        List<News> newsList = opt.get();
        model.addAttribute("newsList", newsList);
        return "top-headlines";
    }
}
